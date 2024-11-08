package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.CartItem;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.ICartItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("cartItem")
public class CartItemController {

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
    @PostMapping("/add")
    public String addCartItem(@ModelAttribute CartItem item, HttpSession session,
                              @RequestParam("flowerId") int flowerId, HttpServletRequest request) {
        String accessCheck = checkCustomerAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        String referer = request.getHeader("Referer");
        int userId = ((Users) session.getAttribute("loggedInUser")).getId();
        cartItemService.addCartItem(item,flowerId, userId);
        return referer != null ? "redirect:" + referer : "/Customer/carts";
    }

    @PutMapping("/update")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem item) {
        CartItem updatedItem = cartItemService.updateCartItem(item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{cartItemId}/delete")
    public ResponseEntity<Void> deleteCartItem(@PathVariable int cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}