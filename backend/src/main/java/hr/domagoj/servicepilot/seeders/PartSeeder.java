package hr.domagoj.servicepilot.seeders;

import hr.domagoj.servicepilot.entities.Part;
import hr.domagoj.servicepilot.repos.PartRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Order(7)
public class PartSeeder implements ApplicationRunner {

    private final PartRepository partRepository;

    public PartSeeder(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        parts().forEach(this::seedPart);
    }

    private void seedPart(PartSeed seed) {
        if (partRepository.findByPartNumber(seed.partNumber()).isPresent()) {
            return;
        }

        partRepository.save(Part.builder()
                .name(seed.name())
                .partNumber(seed.partNumber())
                .category(seed.category())
                .manufacturer(seed.manufacturer())
                .quantityInStock(seed.quantityInStock())
                .reorderThreshold(seed.reorderThreshold())
                .unitPrice(seed.unitPrice())
                .supplier(seed.supplier())
                .active(true)
                .build());
    }

    private List<PartSeed> parts() {
        return List.of(
                new PartSeed(
                        "Engine oil 5W-30 1L",
                        "OIL-5W30-1L",
                        "Fluids",
                        "Castrol",
                        24,
                        6,
                        new BigDecimal("11.50"),
                        "AutoParts Zagreb"
                ),
                new PartSeed(
                        "Oil filter",
                        "FIL-OIL-001",
                        "Filters",
                        "Bosch",
                        12,
                        5,
                        new BigDecimal("9.90"),
                        "AutoParts Zagreb"
                ),
                new PartSeed(
                        "Air filter",
                        "FIL-AIR-001",
                        "Filters",
                        "Mann Filter",
                        8,
                        4,
                        new BigDecimal("14.50"),
                        "Mann Distributor"
                ),
                new PartSeed(
                        "Front brake pads",
                        "BRK-PAD-FR-001",
                        "Brakes",
                        "Brembo",
                        6,
                        3,
                        new BigDecimal("42.00"),
                        "Brake Supply HR"
                ),
                new PartSeed(
                        "Cabin filter",
                        "FIL-CAB-001",
                        "Filters",
                        "Bosch",
                        10,
                        4,
                        new BigDecimal("12.00"),
                        "AutoParts Zagreb"
                )
        );
    }

    private record PartSeed(
            String name,
            String partNumber,
            String category,
            String manufacturer,
            Integer quantityInStock,
            Integer reorderThreshold,
            BigDecimal unitPrice,
            String supplier
    ) {
    }
}
