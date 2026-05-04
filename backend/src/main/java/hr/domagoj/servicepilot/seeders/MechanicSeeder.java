package hr.domagoj.servicepilot.seeders;

import hr.domagoj.servicepilot.entities.Mechanic;
import hr.domagoj.servicepilot.entities.User;
import hr.domagoj.servicepilot.enums.AvailabilityStatus;
import hr.domagoj.servicepilot.enums.EmploymentType;
import hr.domagoj.servicepilot.repos.MechanicRepository;
import hr.domagoj.servicepilot.repos.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Order(3)
public class MechanicSeeder implements ApplicationRunner {
    private final MechanicRepository mechanicRepository;
    private final UserRepository userRepository;

    public MechanicSeeder(MechanicRepository mechanicRepository, UserRepository userRepository) {
        this.mechanicRepository = mechanicRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        mechanics().forEach(this::seedMechanic);
    }

    private void seedMechanic(MechanicSeed seed) {
        User user = userRepository.findByEmail(seed.userEmail())
                .orElseThrow(() -> new IllegalStateException("User not found: " + seed.userEmail()));

        if (mechanicRepository.findByUser(user).isPresent()) {
            return;
        }

        mechanicRepository.save(Mechanic.builder()
                .specialization(seed.specialization())
                .experienceYears(seed.experienceYears())
                .hourlyRate(seed.hourlyRate())
                .employmentType(seed.employmentType())
                .availabilityStatus(seed.availabilityStatus())
                .active(true)
                .notes(seed.notes())
                .user(user)
                .build());
    }

    private List<MechanicSeed> mechanics() {
        return List.of(
                new MechanicSeed(
                        "marko.horvat@servicepilot.local",
                        "Diesel engines and diagnostics",
                        7,
                        new BigDecimal("28.00"),
                        EmploymentType.FULL_TIME,
                        AvailabilityStatus.AVAILABLE,
                        "Experienced mechanic for diesel vehicles and engine diagnostics."
                ),
                new MechanicSeed(
                        "ivan.novak@servicepilot.local",
                        "Brakes, suspension and regular service",
                        4,
                        new BigDecimal("24.00"),
                        EmploymentType.FULL_TIME,
                        AvailabilityStatus.AVAILABLE,
                        "Handles regular maintenance, brakes and suspension repairs."
                )
        );
    }

    private record MechanicSeed(
            String userEmail,
            String specialization,
            Integer experienceYears,
            BigDecimal hourlyRate,
            EmploymentType employmentType,
            AvailabilityStatus availabilityStatus,
            String notes
    ) {
    }
}
