package hr.domagoj.servicepilot.DTOs;

import hr.domagoj.servicepilot.enums.NotificationType;
import hr.domagoj.servicepilot.enums.ReferenceType;
import java.time.LocalDateTime;

public record NotificationDTO(
        Long id,
        NotificationType type,
        String title,
        String message,
        ReferenceType referenceType,
        Long referenceId,
        Boolean read,
        LocalDateTime readAt,
        LocalDateTime createdAt
) {}
