package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private INotificationService notificationService;

    @ModelAttribute
    public void addGlobalAttributes(HttpSession session, Model model) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("isAdmin", session.getAttribute("isAdmin"));
            model.addAttribute("isCustomer", session.getAttribute("isCustomer"));
            model.addAttribute("isSeller", session.getAttribute("isSeller"));

            long unreadCount = notificationService.findByUserId(loggedInUser.getId()).stream().filter(n -> !n.isRead()).count();
            model.addAttribute("notificationCount", unreadCount);
        }
    }
}
