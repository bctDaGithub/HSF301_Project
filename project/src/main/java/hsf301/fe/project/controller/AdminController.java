package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUsersService usersService;

    private String checkAdminAccess(HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/user/login";
        } else if (!"ADMIN".equals(loggedInUser.getRole())) {
            return "redirect:/error/403";
        }
        return null; 
    }

    @GetMapping("/home")
    public String showAdminHome(HttpSession session) {
        String accessCheck = checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck; 
        }
        return "admin/home"; 
    }

    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam(name = "search", required = false) String search,
                                @RequestParam(name = "role", required = false) String role,
                                HttpSession session, Model model) {
        String accessCheck = checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck; 
        }

        List<Users> usersList = usersService.getAllUsers().stream()
                .filter(user -> !"ADMIN".equals(user.getRole()))
                .collect(Collectors.toList());

        if (search != null && !search.isEmpty()) {
            usersList = usersList.stream()
                    .filter(user -> user.getName().toLowerCase().contains(search.toLowerCase()) || 
                                    user.getEmail().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (role != null && !role.isEmpty()) {
            usersList = usersList.stream()
                    .filter(user -> user.getRole().equalsIgnoreCase(role))
                    .collect(Collectors.toList());
        }

        model.addAttribute("users", usersList);
        return "admin/dashboard";
    }

    @PostMapping("/ban")
    public String banUser(@RequestParam("userId") int userId, HttpSession session) {
        String accessCheck = checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck; 
        }

        usersService.setUserActiveStatus(userId, false);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/unban")
    public String unbanUser(@RequestParam("userId") int userId, HttpSession session) {
        String accessCheck = checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck; 
        }

        usersService.setUserActiveStatus(userId, true);
        return "redirect:/admin/dashboard";
    }
}
