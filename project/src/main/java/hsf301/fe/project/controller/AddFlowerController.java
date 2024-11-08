package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Flower;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.IFlowerService;
import hsf301.fe.project.service.implement.SaveFileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/add")
public class AddFlowerController {
    @Autowired
    private IFlowerService flowerService;

    @Autowired
    private SaveFileService saveImage;

    @GetMapping
    public String showAddFlowerPage() {
        return "Seller/addFlower";  // Đảm bảo rằng trang addFlower.html ở đúng thư mục src/main/resources/templates/Seller/
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
}
