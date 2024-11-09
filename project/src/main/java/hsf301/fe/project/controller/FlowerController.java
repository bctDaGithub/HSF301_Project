package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.CartItem;
import hsf301.fe.project.pojo.Flower;
import hsf301.fe.project.pojo.FlowerDTO;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.IFlowerService;
import hsf301.fe.project.service.defines.ISaveFileService;
import hsf301.fe.project.service.implement.SaveFileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/flowers")
public class FlowerController {

    @Autowired
    private IFlowerService flowerService;

    @Autowired
    private SaveFileService saveImage;
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

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createFlower(
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("quantity") int quantity,
            @RequestParam("condition") String condition,
            @RequestParam("price") double price,
            @RequestParam(name = "imageUrl") MultipartFile imageUrl,
            HttpSession session, HttpServletRequest request) {

        // Gán thông tin người bán từ session
        Users seller = (Users) session.getAttribute("loggedInUser");

        // Tạo đối tượng Flower và thiết lập các thuộc tính
        Flower flower1 = new Flower();
        flower1.setName(name);
        flower1.setType(type);
        flower1.setQuantity(quantity);
        flower1.setCondition(condition);
        flower1.setPrice(price);

        if (!imageUrl.isEmpty()) {
            try {
                String imagePath = saveImage.uploadImage(imageUrl);
                flower1.setImageUrl(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        flower1.setSeller(seller);
        flowerService.saveFlower(flower1);

        String referer = request.getHeader("Referer");
        return referer != null ? "redirect:" + referer : "Seller/flowers";
    }



    @GetMapping("/edit/{id}")
    public String showEditFlowerForm(@PathVariable int id, Model model) {
        Flower flower = flowerService.getFlowerById(id);
        model.addAttribute("flower", flower);
        return "Seller/editFlower";
    }

    @PostMapping("/update/{id}")
    public String updateFlower(@PathVariable int id, @ModelAttribute Flower flowerDetails) {
        flowerService.updateFlower(id, flowerDetails);
        return "redirect:/Seller/flowers";
    }

    @GetMapping("/delete/{id}")
    public String deleteFlower(@PathVariable int id) {
        flowerService.deleteFlower(id);
        return "redirect:/flowers";
    }
}
