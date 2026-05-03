package hr.domagoj.servicepilot.DTOs;

import hr.domagoj.servicepilot.enums.AvailabilityStatus;
import hr.domagoj.servicepilot.enums.EmploymentType;

import java.math.BigDecimal;

public record MechanicDTO(
        String specialization,
        Integer experienceYears,
        BigDecimal hourlyRate,
        EmploymentType employmentType,
        AvailabilityStatus availabilityStatus,
        Boolean active,
        String notes
) {}
