package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.*;
import hsf301.fe.project.repository.FlowerRepository;
import hsf301.fe.project.repository.OrderFlowerRepository;
import hsf301.fe.project.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    WalletService walletService;
    @Autowired
    private OrderRepository orderRepository; // Repository để lưu thông tin đơn hàng
    @Autowired
    private OrderFlowerRepository orderFlowerRepository;

    @Autowired
    private FlowerRepository flowerRepository;

    @Transactional
    public String processOrder(Users users, Double totalPrice, Model model, HttpSession httpSession) {
        // Lấy ví của khách hàng
        Wallet walletUser = walletService.getWalletByUserId(users.getId());

        // Kiểm tra số dư ví
        if (walletUser.getAmount() < totalPrice) {
            model.addAttribute("message", "Vui lòng nạp thêm tiền vào ví để thanh toán.");
            return "checkout";
        }

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setCustomer(users);
        order.setSeller(users);
        order.setDate(new Date());
        order.setTotalPrice(totalPrice);
        order.setStatus("success");

        // Lưu vào bảng orders
        orderRepository.save(order);

        // Lấy thông tin hoa trong giỏ hàng từ session
        List<CartItem> cartItems = (List<CartItem>) httpSession.getAttribute("cartItem");

        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                OrderFlower orderFlower = new OrderFlower();
                orderFlower.setOrder(order); // Gán đơn hàng cho OrderFlower
                orderFlower.setFlower(flowerRepository.findById(cartItem.getId()).orElseThrow());
                orderFlower.setQuantity(cartItem.getQuantity());
                orderFlowerRepository.save(orderFlower); // Lưu chi tiết hoa vào bảng OrderFlower
            }
        }

        // Trừ tiền trong ví của khách hàng
        walletUser.setAmount((long) (walletUser.getAmount() - totalPrice));
        walletService.save1(walletUser); // Cập nhật số dư ví của khách hàng

        // Thông báo cho người dùng
        model.addAttribute("message", "Đặt hàng thành công!");
        return "Customer/orderConfirmation"; // Điều hướng đến trang xác nhận đơn hàng
    }
}
