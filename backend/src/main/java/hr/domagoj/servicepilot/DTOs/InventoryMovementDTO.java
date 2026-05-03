package hr.domagoj.servicepilot.DTOs;

import hr.domagoj.servicepilot.enums.MovementType;
import java.time.LocalDateTime;

public record InventoryMovementDTO(
        Long id,
        MovementType movementType,
        Integer quantity,
        Integer previousStock,
        Integer newStock,
        String note,
        Long partId,
        Long workOrderId,
        Long createdByUserId,
        LocalDateTime createdAt
) {}
