package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.*;
import hsf301.fe.project.service.defines.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private ICartItemService cartItemService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderFlowerService orderFlowerService;

    private String checkCustomerAccess(HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        } else if (!"CUSTOMER".equals(loggedInUser.getRole())) {
            return "redirect:/error/403";
        }
        return null;
    }

    @GetMapping("/home")
    public String showAdminHome(HttpSession session) {
        String accessCheck = checkCustomerAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        return "Customer/home";
    }

    @GetMapping("/carts")
    public String showCarts(HttpSession session, Model model) {
        String accessCheck = checkCustomerAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        Cart cart = cartService.getCartByUserId(((Users) session.getAttribute("loggedInUser")).getId());
        List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getId());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartItem", new CartItem());
        return "Customer/carts";
    }
    @GetMapping("/checkout")
    public String ShowCheckout(@ModelAttribute("cartItemIds") String cartItemIds, Model model,HttpSession session) {
        String accessCheck = checkCustomerAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        // Chuyển chuỗi ID thành danh sách
        List<Integer> selectedIds = Arrays.stream(cartItemIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        model.addAttribute("oderFlowers", cartItemService.getCartItemsById(selectedIds));
        model.addAttribute("oder", new Order() );
        model.addAttribute("oderFlower", new OrderFlower());
        // Xử lý thanh toán (ví dụ: tạo đơn hàng với các ID sản phẩm đã chọn)
        return "Customer/checkout";
    }
}

