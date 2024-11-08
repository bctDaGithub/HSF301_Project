package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.CartItem;
import hsf301.fe.project.pojo.Flower;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.IFlowerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/flowers")
public class FlowerController {

    @Autowired
    private IFlowerService flowerService;


    @GetMapping
    public String getAllFlowers(Model model) {
        try {
            model.addAttribute("flower", new Flower());
            List<Flower> flowers = flowerService.getAllFlowers();
            model.addAttribute("flowers", flowers);
            System.out.printf("Hello1");
            return "flower/flowers";
        } catch (Exception e) {
            // Xử lý lỗi và trả về thông báo
            model.addAttribute("errorMessage", "Error retrieving flowers: " + e.getMessage());
            return "error/403"; // Đảm bảo rằng có trang lỗi tương ứng
        }
    }

    @GetMapping("/{id}")
    public String getFlowerById(@PathVariable int id, Model model) {
        Flower flower = flowerService.getFlowerById(id);
        model.addAttribute("flower", flower);
        model.addAttribute("cartItem", new CartItem());
        return "flower/flower-detail";
    }

    @GetMapping("/create")
    public String showCreateFlowerForm(Model model) {
        model.addAttribute("flower", new Flower());
        return "flower/flower-form";
    }

    @PostMapping
    public String createFlower(@ModelAttribute Flower flower, HttpSession Session, HttpServletRequest request) {
        flower.setSeller(((Users) Session.getAttribute("loggedInUser")));
        flowerService.saveFlower(flower);
        String referer = request.getHeader("Referer");
        return referer != null ? "redirect:" + referer : "Seller/flowers";
    }

    @GetMapping("/edit/{id}")
    public String showEditFlowerForm(@PathVariable int id, Model model) {
        Flower flower = flowerService.getFlowerById(id);
        model.addAttribute("flower", flower);
        return "flower/flower-form";
    }

    @PostMapping("/update/{id}")
    public String updateFlower(@PathVariable int id, @ModelAttribute Flower flowerDetails) {
        flowerService.updateFlower(id, flowerDetails);
        return "redirect:/flowers";
    }

    @GetMapping("/delete/{id}")
    public String deleteFlower(@PathVariable int id) {
        flowerService.deleteFlower(id);
        return "redirect:/flowers";
    }
}
