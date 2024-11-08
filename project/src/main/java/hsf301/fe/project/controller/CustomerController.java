package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Cart;
import hsf301.fe.project.pojo.CartItem;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.ICartItemService;
import hsf301.fe.project.service.defines.ICartService;
import hsf301.fe.project.service.defines.IUsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private ICartItemService cartItemService;

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

}
