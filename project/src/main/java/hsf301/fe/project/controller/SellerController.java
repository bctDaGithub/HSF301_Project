package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Flower;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.IFlowerService;
import hsf301.fe.project.service.defines.IUsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/Seller")
public class SellerController {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private IFlowerService flowerService;

    private String checkSellerAccess(HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        } else if (!"SELLER".equals(loggedInUser.getRole())) {
            return "redirect:/error/403";
        }
        return null;
    }


    @GetMapping("/home")
    public String showAdminHome(HttpSession session) {
        String accessCheck = checkSellerAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        return "Seller/home";
    }

    @GetMapping("/flowers")
    public String showFlowers(HttpSession session, Model model) {
        String accessCheck = checkSellerAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        List<Flower> flowers = flowerService.getFlowersBySellerId(((Users) session.getAttribute("loggedInUser")).getId());
        model.addAttribute("flowers", flowers);
        model.addAttribute("flower", new Flower());
        return "Seller/flowers";
    }

}
