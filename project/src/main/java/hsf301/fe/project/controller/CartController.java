package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Cart;
import hsf301.fe.project.pojo.CartItem;
import hsf301.fe.project.pojo.Flower;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.ICartItemService;
import hsf301.fe.project.service.defines.ICartService;
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
}