package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Order;
import hsf301.fe.project.service.defines.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    // Hiển thị danh sách các đơn hàng
    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/list"; // Trả về trang hiển thị danh sách đơn hàng
    }

    // Hiển thị chi tiết một đơn hàng
    @GetMapping("/{id}")
    public String getOrderById(@PathVariable("id") int orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/detail"; // Trả về trang hiển thị chi tiết đơn hàng
    }

    // Tạo đơn hàng mới
    @GetMapping("/create")
    public String createOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order/create"; // Trả về trang tạo đơn hàng mới
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") Order order) {
        orderService.createOrder(order);
        return "redirect:/orders"; // Sau khi tạo, chuyển hướng về trang danh sách đơn hàng
    }

    // Cập nhật đơn hàng
    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable("id") int orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/edit"; // Trả về trang chỉnh sửa đơn hàng
    }

    @PostMapping("/edit")
    public String updateOrder(@ModelAttribute("order") Order order) {
        orderService.updateOrder(order);
        return "redirect:/orders";
    }

    // Xóa đơn hàng
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") int orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/orders";
    }
}
