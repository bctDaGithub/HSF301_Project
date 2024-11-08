package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.OrderFlower;
import hsf301.fe.project.service.defines.IOrderFlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order-flowers")
public class OrderFlowerController {

    @Autowired
    private IOrderFlowerService orderFlowerService;

    // Hiển thị danh sách các OrderFlower
    @GetMapping
    public String listOrderFlowers(Model model) {
        List<OrderFlower> orderFlowers = orderFlowerService.getAllOrderFlowers();
        model.addAttribute("orderFlowers", orderFlowers);
        return "orderflower/list"; // Trả về trang hiển thị danh sách OrderFlower
    }

    // Hiển thị chi tiết một OrderFlower
    @GetMapping("/{id}")
    public String getOrderFlowerById(@PathVariable("id") int orderFlowerId, Model model) {
        OrderFlower orderFlower = orderFlowerService.getOrderFlowerById(orderFlowerId);
        model.addAttribute("orderFlower", orderFlower);
        return "orderflower/detail"; // Trả về trang hiển thị chi tiết OrderFlower
    }

    // Tạo mới OrderFlower
    @GetMapping("/create")
    public String createOrderFlowerForm(Model model) {
        model.addAttribute("orderFlower", new OrderFlower());
        return "orderflower/create"; // Trả về trang tạo OrderFlower mới
    }

    @PostMapping("/create")
    public String createOrderFlower(@ModelAttribute("orderFlower") OrderFlower orderFlower) {
        orderFlowerService.createOrderFlower(orderFlower);
        return "redirect:/order-flowers"; // Sau khi tạo, chuyển hướng về trang danh sách OrderFlower
    }

    // Cập nhật OrderFlower
    @GetMapping("/edit/{id}")
    public String editOrderFlowerForm(@PathVariable("id") int orderFlowerId, Model model) {
        OrderFlower orderFlower = orderFlowerService.getOrderFlowerById(orderFlowerId);
        model.addAttribute("orderFlower", orderFlower);
        return "orderflower/edit"; // Trả về trang chỉnh sửa OrderFlower
    }

    @PostMapping("/edit")
    public String updateOrderFlower(@ModelAttribute("orderFlower") OrderFlower orderFlower) {
        orderFlowerService.updateOrderFlower(orderFlower);
        return "redirect:/order-flowers";
    }

    // Xóa OrderFlower
    @GetMapping("/delete/{id}")
    public String deleteOrderFlower(@PathVariable("id") int orderFlowerId) {
        orderFlowerService.deleteOrderFlower(orderFlowerId);
        return "redirect:/order-flowers";
    }
}
