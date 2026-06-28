package hr.domagoj.servicepilot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI servicePilotOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ServicePilot API")
                        .description("API documentation for ServicePilot auto service management platform. Authentication uses JWT access and refresh tokens stored in HTTP-only cookies; mutating requests may also require the CSRF token header.")
                        .version("v1")
                        .contact(new Contact().name("Domagoj Cirko")))
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Local development")));
    }
}
