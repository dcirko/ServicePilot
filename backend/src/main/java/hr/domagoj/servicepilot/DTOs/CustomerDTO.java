package hr.domagoj.servicepilot.DTOs;

public record CustomerDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        String notes
) {}
