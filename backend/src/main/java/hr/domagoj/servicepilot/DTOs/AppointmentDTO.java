package hr.domagoj.servicepilot.DTOs;

import hr.domagoj.servicepilot.enums.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentDTO(
        Long id,
        LocalDateTime scheduledStart,
        LocalDateTime scheduledEnd,
        String issueDescription,
        AppointmentStatus status,
        String notes,
        Long customerId,
        Long vehicleId,
        Long assignedMechanicId,
        Long requestedServiceId
) {}
