package hr.domagoj.servicepilot.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.domagoj.servicepilot.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

    private final SecurityProperties securityProperties;
    private final ObjectMapper objectMapper;

    public JwtService(SecurityProperties securityProperties, ObjectMapper objectMapper) {
        this.securityProperties = securityProperties;
        this.objectMapper = objectMapper;
    }

    public String createAccessToken(CustomUserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(securityProperties.getJwt().getAccessTokenExpirationMinutes() * 60);

        Map<String, Object> header = Map.of(
                "alg", "HS256",
                "typ", "JWT"
        );
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", principal.getEmail());
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", expiresAt.getEpochSecond());
        payload.put("userId", principal.getId());
        payload.put("email", principal.getEmail());
        payload.put("roles", principal.getRoles());
        payload.put("tokenType", ACCESS_TOKEN_TYPE);

        String unsignedToken = encodeJson(header) + "." + encodeJson(payload);
        return unsignedToken + "." + sign(unsignedToken);
    }

    public AccessTokenClaims validateAccessToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new UnauthorizedException("Invalid access token");
        }

        String unsignedToken = parts[0] + "." + parts[1];
        String expectedSignature = sign(unsignedToken);
        if (!MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
            throw new UnauthorizedException("Invalid access token signature");
        }

        Map<String, Object> claims = decodePayload(parts[1]);
        String subject = stringClaim(claims, "sub");
        String email = stringClaim(claims, "email");
        String tokenType = stringClaim(claims, "tokenType");
        long expiration = longClaim(claims, "exp");
        Long userId = longClaim(claims, "userId");
        List<String> roles = listClaim(claims, "roles");

        if (!ACCESS_TOKEN_TYPE.equals(tokenType)) {
            throw new UnauthorizedException("Invalid access token type");
        }
        if (subject == null || email == null || !subject.equals(email)) {
            throw new UnauthorizedException("Invalid access token subject");
        }
        if (Instant.now().getEpochSecond() >= expiration) {
            throw new UnauthorizedException("Access token expired");
        }

        return new AccessTokenClaims(userId, email, roles);
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            return URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to encode JWT", ex);
        }
    }

    private Map<String, Object> decodePayload(String payload) {
        try {
            return objectMapper.readValue(URL_DECODER.decode(payload), new TypeReference<>() {
            });
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid access token payload");
        }
    }

    private String sign(String unsignedToken) {
        try {
            byte[] secret = securityProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
            if (secret.length < 32) {
                throw new IllegalStateException("JWT secret must be at least 256 bits");
            }
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret, HMAC_SHA256));
            return URL_ENCODER.encodeToString(mac.doFinal(unsignedToken.getBytes(StandardCharsets.UTF_8)));
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to sign JWT", ex);
        }
    }

    private String stringClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        return value instanceof String string ? string : null;
    }

    private Long longClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (value instanceof Number number) {
            return number.longValue();
        }
        throw new UnauthorizedException("Invalid access token claim: " + name);
    }

    private List<String> listClaim(Map<String, Object> claims, String name) {
        Object value = claims.get(name);
        if (value instanceof List<?> list && list.stream().allMatch(String.class::isInstance)) {
            return list.stream().map(String.class::cast).toList();
        }
        throw new UnauthorizedException("Invalid access token claim: " + name);
    }

    public record AccessTokenClaims(Long userId, String email, List<String> roles) {
    }
}
