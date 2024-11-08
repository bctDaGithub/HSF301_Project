package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Flower;
import hsf301.fe.project.service.defines.IFlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
public class HomeController {
    @Autowired
    private IFlowerService flowerService;

    @GetMapping("/showHomePage")
    public String showHomePage(Model model) {
        List<Flower> flowers = flowerService.getAllFlowers();
        model.addAttribute("flowers", flowers);
        return "home";
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/showHomePage";
    }
}