package hr.domagoj.servicepilot.controllers;

import hr.domagoj.servicepilot.DTOs.LoginRequest;
import hr.domagoj.servicepilot.DTOs.RegisterRequest;
import hr.domagoj.servicepilot.DTOs.UserDTO;
import hr.domagoj.servicepilot.services.implementations.AuthServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

}
