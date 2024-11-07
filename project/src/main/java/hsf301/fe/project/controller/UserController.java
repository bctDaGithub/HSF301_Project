package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.IUsersService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@SessionAttributes("loggedInUser")
public class UserController {

    @Autowired
    private IUsersService usersService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Users user, Model model) {
        try {
            usersService.createNewUsers(user);
            return "redirect:/user/login";
        } catch (Exception e) {
            model.addAttribute("registrationError", "Registration failed. Please try again.");
            return "user/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session, Model model) {
        Users user = usersService.authenticateUser(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            String role = user.getRole();

            // Điều hướng dựa trên role
            if ("ADMIN".equals(role)) {
                return "redirect:/admin/dashboard";
            } else if ("CUSTOMER".equals(role)) {
                return "redirect:/customer/dashboard";
            } else if ("SELLER".equals(role)) {
                return "redirect:/seller/dashboard";
            } else {
                model.addAttribute("loginError", "Role not recognized.");
                return "user/login";
            }
        } else {
            model.addAttribute("loginError", "Invalid email or password.");
            return "user/login";
        }
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
            return "user/profile";
        } else {
            return "redirect:/user/login";
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}
