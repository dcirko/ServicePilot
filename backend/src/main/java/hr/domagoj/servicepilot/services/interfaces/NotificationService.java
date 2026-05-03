package hr.domagoj.servicepilot.services.interfaces;

import hr.domagoj.servicepilot.DTOs.NotificationDTO;
import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getNotificationsForUser(Long userId);
    //void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
}
