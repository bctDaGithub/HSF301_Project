package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.*;
import hsf301.fe.project.service.defines.ICartItemService;
import hsf301.fe.project.service.defines.ICartService;
import hsf301.fe.project.service.implement.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ICartService cartService;
    @Autowired
    private ICartItemService cartItemService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable int userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return null;
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addItemToCart(@PathVariable int userId, @RequestBody CartItem item) {
        Cart updatedCart = cartService.addItemToCart(userId, item);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) {
        Cart updatedCart = cartService.updateCart(cart);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable int userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/carts")
    public String showCartPage(HttpSession session, Model model) {
        Cart cart = cartService.getCartByUserId(((Users) session.getAttribute("loggedInUser")).getId());
        List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getId());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartItem", new CartItem());
        return "/Customer/carts";
    }
    @PostMapping("/checkout")
    public String checkoutSelectedItems(@RequestParam("cartItemIds") List<Integer> cartItemIds,
                                        @RequestParam("totalPrice") Double totalPrice,
                                        HttpSession httpSession,
                                        Model model) {
        Users users = (Users) httpSession.getAttribute("loggedInUser");

        // Chuyển đổi totalPrice từ Double sang Long trước khi gọi service
        Long totalPriceLong = totalPrice.longValue();

        // Gọi phương thức processOrder với totalPriceLong kiểu Long
        return orderService.processOrder(users, cartItemIds, totalPriceLong, model, httpSession);
    }
}