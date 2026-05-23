package hr.domagoj.servicepilot.DTOs;

import java.util.List;

public record CurrentUserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Boolean enabled,
        List<String> roles
) {}
