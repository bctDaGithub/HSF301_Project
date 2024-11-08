package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.implement.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService; // Service để xử lý logic đặt hàng

    @PostMapping("/checkout")
    public String checkoutSelectedItems(@RequestParam("totalPrice") Double totalPrice, HttpSession httpSession, Model model) {
        Users users= (Users) httpSession.getAttribute("loggedInUser");
        return orderService.processOrder(users, totalPrice, model,httpSession);

    }
}
