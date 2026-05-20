package hr.domagoj.servicepilot.services.interfaces;

import hr.domagoj.servicepilot.DTOs.AuthResponse;
import hr.domagoj.servicepilot.DTOs.CurrentUserResponse;
import hr.domagoj.servicepilot.DTOs.LoginRequest;
import hr.domagoj.servicepilot.DTOs.RegisterRequest;
import hr.domagoj.servicepilot.security.CustomUserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse);

    AuthResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse);

    AuthResponse refresh(HttpServletRequest httpRequest, HttpServletResponse httpResponse);

    void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse);

    CurrentUserResponse currentUser(CustomUserPrincipal principal);
}
