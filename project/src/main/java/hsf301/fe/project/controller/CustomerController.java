package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.*;
import hsf301.fe.project.service.defines.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        return "order";
    }
    @GetMapping("/orders")
    public String showOrders(HttpSession session, Model model) {
        String accessCheck = checkCustomerAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        List<Order>  allOrders = orderService.getOrdersByCustomerId(((Users) session.getAttribute("loggedInUser")).getId());
        List<Order> pendingOrders = allOrders.stream()
                .filter(order -> "pending".equals(order.getStatus()))
                .collect(Collectors.toList());

        List<Order> completedOrders = allOrders.stream()
                .filter(order -> "success".equals(order.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("allOrders", allOrders);
        model.addAttribute("order", new Order());
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("completedOrders", completedOrders);
        return "Customer/orders";
    }

    @GetMapping("/orderDetails/{orderId}")
    public String getOrderDetails(@PathVariable int orderId, Model model) {
        Order order = orderService.getOrderById(orderId); // Lấy dữ liệu từ service
        List<OrderFlower> orderFlowers = orderFlowerService.getOrderFlowersByOrder(order);
        model.addAttribute("order", order);
        model.addAttribute("orderFlowers", orderFlowers);
        model.addAttribute("orderFlower", new OrderFlower());
        return "Customer/oderDetail"; // Trả về view popup
    }


}

