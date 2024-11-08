package hsf301.fe.project.service.defines;

import hsf301.fe.project.pojo.Notification;
import hsf301.fe.project.pojo.Users;

import java.util.List;

public interface INotificationService {
    List<Notification> findByUserId(int userId);
    void markAsRead(int notiId);
    public void addNewNotification(String title, String msg, Users account);
}
