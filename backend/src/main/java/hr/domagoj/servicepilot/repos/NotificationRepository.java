package hr.domagoj.servicepilot.repos;

import hr.domagoj.servicepilot.entities.Notification;
import hr.domagoj.servicepilot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
