package hsf301.fe.project.controller;

import hsf301.fe.project.config.VnpayConfig;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.pojo.Wallet;
import hsf301.fe.project.service.implement.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private VnpayConfig vnpayConfig;

    @Autowired
    private WalletService walletService;

    @GetMapping("/wallets")
    public ResponseEntity<Wallet> getWalletByUserId(HttpSession httpSession) {
        Users user = (Users) httpSession.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        Wallet wallet = walletService.getWalletByUserId(user.getId());
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/add-funds")
    public String showAddFundsPage(HttpSession session) {
        Users users = (Users) session.getAttribute("loggedInUser");
        if (users != null) {
            return "user/wallet"; // Tên file HTML trong thư mục templates
        }
        return "user/login";
    }

    @PostMapping("/add-funds")
    public void addFunds(@RequestParam("amount") Long amountStr, HttpSession session, HttpServletResponse response) throws IOException {
        if (amountStr == null || amountStr <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid amount.");
            return;
        }

        try {
            // Kiểm tra người dùng đã đăng nhập
            Users user = (Users) session.getAttribute("loggedInUser");
            if (user == null) {
                response.sendRedirect("/user/login");
                return;
            }

            Integer userId = user.getId();
            // Tính toán số tiền
            long amount = amountStr * 100;

            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "billpayment";
            String vnp_TxnRef = vnpayConfig.getRandomNumber(8);
            String vnp_TmnCode = vnpayConfig.getVnp_TmnCode();

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Nạp tiền vào ví: " + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");

            // Thiết lập URL callback mặc định cho VNPay
            String defaultCallbackUrl = vnpayConfig.getVnp_ReturnUrl() + "?userId=" + userId;
            vnp_Params.put("vnp_ReturnUrl", defaultCallbackUrl);
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");

            // Tạo ngày và giờ tạo
            ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
            ZonedDateTime now = ZonedDateTime.now(vietnamZone);
            String vnp_CreateDate = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            // Thiết lập ngày hết hạn
            String vnp_ExpireDate = now.plusMinutes(15).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Tạo URL thanh toán
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII)).append('&');
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII)).append('&');
                }
            }
            // Xóa ký tự `&` cuối cùng
            hashData.setLength(hashData.length() - 1);
            query.setLength(query.length() - 1);

            // Tạo vnp_SecureHash
            String vnp_SecureHash = vnpayConfig.hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);
            String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + query.toString();

            // Tự động chuyển hướng tới trang thanh toán VNPay
            response.sendRedirect(paymentUrl);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/vnpay_return")
    public void handleVNPayReturn(
            @RequestParam Map<String, String> params,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String callbackUrl,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TxnRef = params.get("vnp_TxnRef");
        String vnp_Amount = params.get("vnp_Amount");

        boolean isSuccess = "00".equals(vnp_ResponseCode);

        try {
            if (isSuccess && userId != null) {
                walletService.addFunds(Integer.parseInt(userId), Long.parseLong(vnp_Amount) / 100);
            }
            String redirectUrl;
            if (callbackUrl != null && !callbackUrl.isEmpty()) {
                if (!callbackUrl.startsWith("http://") && !callbackUrl.startsWith("https://")) {
                    callbackUrl = "http://" + callbackUrl;
                }
                redirectUrl = UriComponentsBuilder.fromUriString(callbackUrl)
                        .queryParam("success", isSuccess)
                        .queryParam("txnRef", vnp_TxnRef)
                        .queryParam("amount", Long.parseLong(vnp_Amount) / 100)
                        .toUriString();
            } else {
                redirectUrl = UriComponentsBuilder.fromUriString("/wallet/result")
                        .queryParam("success", isSuccess)
                        .queryParam("txnRef", vnp_TxnRef)
                        .queryParam("amount", Long.parseLong(vnp_Amount) / 100)
                        .toUriString();
            }
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            String redirectUrl;
            redirectUrl = UriComponentsBuilder.fromUriString("/wallet/result")
                    .queryParam("success", false)
                    .queryParam("txnRef", vnp_TxnRef)
                    .queryParam("amount", Long.parseLong(vnp_Amount) / 100)
                    .toUriString();
            response.sendRedirect(redirectUrl);
        }
    }

    @GetMapping("/result")
    public String paymentResult(@RequestParam boolean success,
                                @RequestParam String txnRef,
                                @RequestParam long amount,
                                Model model) {
        model.addAttribute("success", success);
        model.addAttribute("txnRef", txnRef);
        model.addAttribute("amount", amount);
        return "user/payment-result";
    }
}
