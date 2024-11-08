package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.Notification;
import hsf301.fe.project.pojo.Users;
import hsf301.fe.project.repository.INotificationRepository;
import hsf301.fe.project.service.defines.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private INotificationRepository notificationRepository;

    @Override
    public List<Notification> findByUserId(int userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void markAsRead(int notiId) {
        notificationRepository.markAsRead(notiId);
    }

    @Override
    @Transactional
    public void addNewNotification(String title, String msg, Users account) {
        Notification noti = new Notification(title, msg, account);
        noti.setRead(false);
        notificationRepository.save(noti);
    }
}
