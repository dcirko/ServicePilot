package hr.domagoj.servicepilot.seeders;

import hr.domagoj.servicepilot.entities.Customer;
import hr.domagoj.servicepilot.entities.Vehicle;
import hr.domagoj.servicepilot.enums.EngineType;
import hr.domagoj.servicepilot.repos.CustomerRepository;
import hr.domagoj.servicepilot.repos.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(5)
public class VehicleSeeder implements ApplicationRunner {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public VehicleSeeder(VehicleRepository vehicleRepository, CustomerRepository customerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        vehicles().forEach(this::seedVehicle);
    }

    private void seedVehicle(VehicleSeed seed) {
        if (vehicleRepository.findByRegistrationPlate(seed.registrationPlate()).isPresent()) {
            return;
        }

        Customer customer = customerRepository.findByEmail(seed.customerEmail())
                .orElseThrow(() -> new IllegalStateException("Customer not found: " + seed.customerEmail()));

        vehicleRepository.save(Vehicle.builder()
                .manufacturer(seed.manufacturer())
                .model(seed.model())
                .year(seed.year())
                .vin(seed.vin())
                .registrationPlate(seed.registrationPlate())
                .engineType(seed.engineType())
                .mileage(seed.mileage())
                .color(seed.color())
                .notes(seed.notes())
                .customer(customer)
                .build());
    }

    private List<VehicleSeed> vehicles() {
        return List.of(
                new VehicleSeed(
                        "ivan.horvat@example.com",
                        "Opel",
                        "Astra K",
                        2019,
                        "W0VBD6EB2KG123456",
                        "ZG-1234-AB",
                        EngineType.DIESEL,
                        145000,
                        "Gray",
                        "1.6 CDTI, regular maintenance customer."
                ),
                new VehicleSeed(
                        "ana.kovac@example.com",
                        "Volkswagen",
                        "Golf VII",
                        2017,
                        "WVWZZZAUZHW123456",
                        "ZG-5678-CD",
                        EngineType.PETROL,
                        112000,
                        "White",
                        "Reported occasional brake noise."
                ),
                new VehicleSeed(
                        "ana.kovac@example.com",
                        "Toyota",
                        "Yaris",
                        2021,
                        "JTDKG3D3XM0123456",
                        "ZG-9001-EF",
                        EngineType.HYBRID,
                        58000,
                        "Red",
                        "Hybrid vehicle, mostly city driving."
                ),
                new VehicleSeed(
                        "petar.maric@example.com",
                        "BMW",
                        "320d",
                        2018,
                        "WBA8C91060A123456",
                        "ZG-2222-GH",
                        EngineType.DIESEL,
                        172000,
                        "Black",
                        "Customer asked for detailed diagnostic reports."
                )
        );
    }

    private record VehicleSeed(
            String customerEmail,
            String manufacturer,
            String model,
            Integer year,
            String vin,
            String registrationPlate,
            EngineType engineType,
            Integer mileage,
            String color,
            String notes
    ) {
    }
}
