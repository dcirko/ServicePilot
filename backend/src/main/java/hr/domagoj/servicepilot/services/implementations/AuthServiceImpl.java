package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.*;
import hr.domagoj.servicepilot.entities.Customer;
import hr.domagoj.servicepilot.entities.Role;
import hr.domagoj.servicepilot.entities.User;
import hr.domagoj.servicepilot.exceptions.BadRequestException;
import hr.domagoj.servicepilot.exceptions.ResourceNotFoundException;
import hr.domagoj.servicepilot.exceptions.UnauthorizedException;
import hr.domagoj.servicepilot.repos.CustomerRepository;
import hr.domagoj.servicepilot.repos.RoleRepository;
import hr.domagoj.servicepilot.repos.UserRepository;
import hr.domagoj.servicepilot.security.CookieService;
import hr.domagoj.servicepilot.security.CustomUserPrincipal;
import hr.domagoj.servicepilot.security.JwtService;
import hr.domagoj.servicepilot.services.interfaces.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private static final String DEFAULT_REGISTER_ROLE = "CUSTOMER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CookieService cookieService;
    private final CustomerRepository customerRepository;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            CookieService cookieService,
            CustomerRepository customerRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.cookieService = cookieService;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if (userRepository.existsByEmail(request.email().trim().toLowerCase())) {
            throw new BadRequestException("Email is already registered");
        }

        Role role = roleRepository.findByName(DEFAULT_REGISTER_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found: " + DEFAULT_REGISTER_ROLE));

        User user = userRepository.save(User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phone(request.phone())
                .active(true)
                .role(role)
                .build());

        Customer customer = new Customer();
        customer.setEmail(request.email());
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setPhone(request.phone());
        customer.setUser(user);

        customerRepository.save(customer);

        return issueAuthCookies(user, httpRequest, httpResponse);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!Boolean.TRUE.equals(user.getActive()) || !passwordMatches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        return issueAuthCookies(user, httpRequest, httpResponse);
    }

    @Override
    @Transactional
    public AuthResponse refresh(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String rawRefreshToken = cookieService.getRefreshToken(httpRequest)
                .orElseThrow(() -> new UnauthorizedException("Refresh token cookie is missing"));

        RefreshTokenService.RotatedRefreshToken rotated = refreshTokenService.rotate(rawRefreshToken, clientIp(httpRequest));
        User user = userRepository.findById(rotated.user().getId())
                .orElseThrow(() -> new UnauthorizedException("Refresh token user no longer exists"));
        CustomUserPrincipal principal = CustomUserPrincipal.from(user);

        cookieService.addAccessTokenCookie(httpResponse, jwtService.createAccessToken(principal));
        cookieService.addRefreshTokenCookie(httpResponse, rotated.rawToken());

        return new AuthResponse(toCurrentUser(principal));
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        cookieService.getRefreshToken(httpRequest)
                .ifPresent(rawToken -> refreshTokenService.revoke(rawToken, clientIp(httpRequest)));
        cookieService.clearAuthCookies(httpResponse);
    }

    @Override
    public CurrentUserResponse currentUser(CustomUserPrincipal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Unauthenticated");
        }
        return toCurrentUser(principal);
    }

    private AuthResponse issueAuthCookies(User user, HttpServletRequest request, HttpServletResponse response) {
        CustomUserPrincipal principal = CustomUserPrincipal.from(user);
        RefreshTokenService.CreatedRefreshToken refreshToken = refreshTokenService.create(user, clientIp(request));

        cookieService.addAccessTokenCookie(response, jwtService.createAccessToken(principal));
        cookieService.addRefreshTokenCookie(response, refreshToken.rawToken());

        return new AuthResponse(toCurrentUser(principal));
    }

    private CurrentUserResponse toCurrentUser(CustomUserPrincipal principal) {
        return new CurrentUserResponse(
                principal.getId(),
                principal.getFirstName(),
                principal.getLastName(),
                principal.getEmail(),
                principal.isEnabled(),
                principal.getRoles()
        );
    }

    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }
        if (passwordEncoder.matches(rawPassword, encodedPassword)) {
            return true;
        }
        if (encodedPassword.startsWith("{bcrypt}")) {
            return passwordEncoder.matches(rawPassword, encodedPassword.substring("{bcrypt}".length()));
        }
        return false;
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

}
