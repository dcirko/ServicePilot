package hr.domagoj.servicepilot.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Service
public class CookieService {

    private final SecurityProperties securityProperties;

    public CookieService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public Optional<String> getAccessToken(HttpServletRequest request) {
        return getCookieValue(request, securityProperties.getCookie().getAccessTokenName());
    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return getCookieValue(request, securityProperties.getCookie().getRefreshTokenName());
    }

    public void addAccessTokenCookie(HttpServletResponse response, String token) {
        addCookie(
                response,
                securityProperties.getCookie().getAccessTokenName(),
                token,
                Duration.ofMinutes(securityProperties.getJwt().getAccessTokenExpirationMinutes())
        );
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String token) {
        addCookie(
                response,
                securityProperties.getCookie().getRefreshTokenName(),
                token,
                Duration.ofDays(securityProperties.getJwt().getRefreshTokenExpirationDays())
        );
    }

    public void clearAuthCookies(HttpServletResponse response) {
        clearCookie(response, securityProperties.getCookie().getAccessTokenName());
        clearCookie(response, securityProperties.getCookie().getRefreshTokenName());
    }

    private Optional<String> getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void addCookie(HttpServletResponse response, String name, String value, Duration maxAge) {
        response.addHeader(HttpHeaders.SET_COOKIE, baseCookie(name, value)
                .maxAge(maxAge)
                .build()
                .toString());
    }

    private void clearCookie(HttpServletResponse response, String name) {
        response.addHeader(HttpHeaders.SET_COOKIE, baseCookie(name, "")
                .maxAge(Duration.ZERO)
                .build()
                .toString());
    }

    private ResponseCookie.ResponseCookieBuilder baseCookie(String name, String value) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(securityProperties.getCookie().isSecure())
                .sameSite(securityProperties.getCookie().getSameSite())
                .path(securityProperties.getCookie().getPath());

        String domain = securityProperties.getCookie().getDomain();
        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }
        return builder;
    }
}
