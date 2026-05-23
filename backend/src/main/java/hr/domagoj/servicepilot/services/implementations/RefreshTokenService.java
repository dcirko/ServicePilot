package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.entities.RefreshToken;
import hr.domagoj.servicepilot.entities.User;
import hr.domagoj.servicepilot.exceptions.UnauthorizedException;
import hr.domagoj.servicepilot.repos.RefreshTokenRepository;
import hr.domagoj.servicepilot.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private static final int REFRESH_TOKEN_BYTES = 64;

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, SecurityProperties securityProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.securityProperties = securityProperties;
    }

    @Transactional
    public CreatedRefreshToken create(User user, String ipAddress) {
        String rawToken = generateRawToken();
        String tokenHash = hash(rawToken);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .expiresAt(Instant.now().plusSeconds(securityProperties.getJwt().getRefreshTokenExpirationDays() * 24 * 60 * 60))
                .createdByIp(ipAddress)
                .build();
        refreshTokenRepository.save(refreshToken);
        return new CreatedRefreshToken(rawToken, tokenHash);
    }

    @Transactional
    public RotatedRefreshToken rotate(String rawToken, String ipAddress) {
        String oldHash = hash(rawToken);
        RefreshToken oldToken = refreshTokenRepository.findByTokenHash(oldHash)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (!oldToken.isActive()) {
            throw new UnauthorizedException("Refresh token expired or revoked");
        }

        CreatedRefreshToken newToken = create(oldToken.getUser(), ipAddress);
        oldToken.setRevokedAt(Instant.now());
        oldToken.setRevokedByIp(ipAddress);
        oldToken.setReplacedByTokenHash(newToken.tokenHash());
        return new RotatedRefreshToken(oldToken.getUser(), newToken.rawToken());
    }

    @Transactional
    public void revoke(String rawToken, String ipAddress) {
        String tokenHash = hash(rawToken);
        refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(refreshToken -> {
            if (refreshToken.getRevokedAt() == null) {
                refreshToken.setRevokedAt(Instant.now());
                refreshToken.setRevokedByIp(ipAddress);
            }
        });
    }

    public String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(digest.digest(rawToken.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to hash refresh token", ex);
        }
    }

    private String generateRawToken() {
        byte[] bytes = new byte[REFRESH_TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public record CreatedRefreshToken(String rawToken, String tokenHash) {
    }

    public record RotatedRefreshToken(User user, String rawToken) {
    }
}
