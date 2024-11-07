package hsf301.fe.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class CustomErrorController {

    @GetMapping("/403")
    public String handle403() {
        return "error/403"; 
    }
}
