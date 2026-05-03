package hr.domagoj.servicepilot.DTOs;

import hr.domagoj.servicepilot.enums.EngineType;

public record VehicleDTO(
        Long id,
        String manufacturer,
        String model,
        Integer year,
        String vin,
        String registrationPlate,
        EngineType engineType,
        Integer mileage,
        String color,
        String notes,
        Long customerId
) {}
