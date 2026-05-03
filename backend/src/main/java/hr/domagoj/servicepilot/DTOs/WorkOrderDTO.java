package hr.domagoj.servicepilot.DTOs;

import hr.domagoj.servicepilot.enums.WorkOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WorkOrderDTO(
        Long id,
        String issueDescription,
        String diagnosis,
        String notes,
        BigDecimal estimatedLaborHours,
        BigDecimal actualLaborHours,
        Integer currentMileage,
        WorkOrderStatus status,
        LocalDateTime openedAt,
        LocalDateTime completedAt,
        Long appointmentId,
        Long customerId,
        Long vehicleId,
        Long assignedMechanicId
) {}
