package hr.domagoj.servicepilot.DTOs;

import java.math.BigDecimal;

public record PartDTO(
        Long id,
        String name,
        String partNumber,
        String category,
        String manufacturer,
        Integer quantityInStock,
        Integer reorderThreshold,
        BigDecimal unitPrice,
        String supplier,
        Boolean active
) {}
