package hr.domagoj.servicepilot.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private final Jwt jwt = new Jwt();
    private final Cookie cookie = new Cookie();

    @Setter
    @Getter
    public static class Jwt {
        private String secret = "CHANGE_ME_TO_LONG_SECRET_AT_LEAST_256_BITS";
        private long accessTokenExpirationMinutes = 15;
        private long refreshTokenExpirationDays = 30;

    }

    @Setter
    @Getter
    public static class Cookie {
        private String accessTokenName = "SP_ACCESS_TOKEN";
        private String refreshTokenName = "SP_REFRESH_TOKEN";
        private boolean secure = false;
        private String sameSite = "Lax";
        private String domain = "";
        private String path = "/";

    }
}
