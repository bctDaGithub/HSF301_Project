package hsf301.fe.project.controller;

import hsf301.fe.project.pojo.Notification;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.service.defines.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @ModelAttribute
    public void addAttributes(HttpSession session, Model model) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            logger.info("Logged in user ID: {}", loggedInUser.getId());
            List<Notification> notifications = notificationService.findByUserId(loggedInUser.getId());
            long unreadCount = notifications.stream().filter(n -> !n.isRead()).count();
            model.addAttribute("notifications", notifications);
            model.addAttribute("notificationCount", unreadCount);
            logger.info("Notifications added to model: {}", notifications.size());
        } else {
            logger.warn("No user logged in");
        }
    }

    @GetMapping
    public String getNotifications(HttpSession session, Model model) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warn("No user logged in for notifications");
            return "redirect:/user/login";
        }

        logger.info("Fetching notifications for user ID: {}", loggedInUser.getId());
        List<Notification> notifications = notificationService.findByUserId(loggedInUser.getId());
        model.addAttribute("notifications", notifications);
        logger.info("Number of notifications fetched: {}", notifications.size());
        return "notifications/list";
    }

    @PostMapping("/mark-as-read")
    @ResponseBody
    public String markAsRead(@RequestParam("notiId") int notiId, HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            logger.warn("No user logged in for marking notifications as read");
            return "No user logged in";
        }

        logger.info("Marking notification ID: {} as read for user ID: {}", notiId, loggedInUser.getId());
        notificationService.markAsRead(notiId);
        return "Notification marked as read";
    }
}
