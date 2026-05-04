package hr.domagoj.servicepilot.seeders;

import hr.domagoj.servicepilot.entities.ServiceCatalog;
import hr.domagoj.servicepilot.repos.ServiceCatalogRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Order(6)
public class ServiceCatalogSeeder implements ApplicationRunner {

    private final ServiceCatalogRepository serviceCatalogRepository;

    public ServiceCatalogSeeder(ServiceCatalogRepository serviceCatalogRepository) {
        this.serviceCatalogRepository = serviceCatalogRepository;
    }

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        services().forEach(this::seedService);
    }

    private void seedService(ServiceSeed seed) {
        if (serviceCatalogRepository.findByName(seed.name()).isPresent()) {
            return;
        }

        serviceCatalogRepository.save(ServiceCatalog.builder()
                .name(seed.name())
                .description(seed.description())
                .estimatedDurationMinutes(seed.estimatedDurationMinutes())
                .basePrice(seed.basePrice())
                .active(true)
                .build());
    }

    private List<ServiceSeed> services() {
        return List.of(
                new ServiceSeed(
                        "Vehicle diagnostics",
                        "Computer diagnostics and basic fault code reading.",
                        45,
                        new BigDecimal("35.00")
                ),
                new ServiceSeed(
                        "Small service",
                        "Engine oil and oil filter replacement with basic inspection.",
                        90,
                        new BigDecimal("80.00")
                ),
                new ServiceSeed(
                        "Full service",
                        "Oil, filters and detailed vehicle inspection.",
                        180,
                        new BigDecimal("180.00")
                ),
                new ServiceSeed(
                        "Brake pad replacement",
                        "Replacement of front or rear brake pads.",
                        120,
                        new BigDecimal("95.00")
                ),
                new ServiceSeed(
                        "Air conditioning service",
                        "A/C inspection, cleaning and refill.",
                        75,
                        new BigDecimal("70.00")
                )
        );
    }

    private record ServiceSeed(
            String name,
            String description,
            Integer estimatedDurationMinutes,
            BigDecimal basePrice
    ) {
    }
}
