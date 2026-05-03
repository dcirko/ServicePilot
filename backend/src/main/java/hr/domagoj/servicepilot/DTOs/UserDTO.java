package hr.domagoj.servicepilot.DTOs;

public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Boolean active,
        String roleName
) {}
