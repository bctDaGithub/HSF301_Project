package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Flower;
import hsf301.fe.project.service.defines.IFlowerService;
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
        List<Flower> flowers = flowerService.getAllFlowers();
        model.addAttribute("flowers", flowers);
        return "flower/flowers";
    }

    @GetMapping("/{id}")
    public String getFlowerById(@PathVariable int id, Model model) {
        Optional<Flower> flower = flowerService.getFlowerById(id);
        model.addAttribute("flower", flower.orElse(null));
        return "flower/flower-detail";
    }

    @GetMapping("/create")
    public String showCreateFlowerForm(Model model) {
        model.addAttribute("flower", new Flower());
        return "flower/flower-form";
    }

    @PostMapping
    public String createFlower(@ModelAttribute Flower flower) {
        flowerService.saveFlower(flower);
        return "redirect:/flowers";
    }

    @GetMapping("/edit/{id}")
    public String showEditFlowerForm(@PathVariable int id, Model model) {
        Optional<Flower> flower = flowerService.getFlowerById(id);
        model.addAttribute("flower", flower.orElse(null));
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
