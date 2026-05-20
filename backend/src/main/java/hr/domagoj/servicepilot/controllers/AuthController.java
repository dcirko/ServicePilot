package hr.domagoj.servicepilot.controllers;

import hr.domagoj.servicepilot.DTOs.AuthResponse;
import hr.domagoj.servicepilot.DTOs.CurrentUserResponse;
import hr.domagoj.servicepilot.DTOs.LoginRequest;
import hr.domagoj.servicepilot.DTOs.RegisterRequest;
import hr.domagoj.servicepilot.security.CustomUserPrincipal;
import hr.domagoj.servicepilot.services.implementations.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        return authService.register(request, httpRequest, httpResponse);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        return authService.login(request, httpRequest, httpResponse);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return authService.refresh(httpRequest, httpResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        authService.logout(httpRequest, httpResponse);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public CurrentUserResponse me(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return authService.currentUser(principal);
    }
}
