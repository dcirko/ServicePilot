package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.NotificationDTO;
import hr.domagoj.servicepilot.entities.Notification;
import hr.domagoj.servicepilot.entities.User;
import hr.domagoj.servicepilot.repos.NotificationRepository;
import hr.domagoj.servicepilot.repos.UserRepository;
import hr.domagoj.servicepilot.services.interfaces.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<NotificationDTO> getNotificationsForUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toDTO)
                .toList();
    }


    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    private NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getReferenceType(),
                notification.getReferenceId(),
                notification.getRead(),
                notification.getReadAt(),
                notification.getCreatedAt()
        );
    }
}
