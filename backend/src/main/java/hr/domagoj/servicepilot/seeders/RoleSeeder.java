package hr.domagoj.servicepilot.seeders;

import hr.domagoj.servicepilot.entities.Role;
import hr.domagoj.servicepilot.repos.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(1)
public class RoleSeeder implements ApplicationRunner {

    public static final String ADMIN = "ADMIN";
    public static final String SERVICE_ADVISOR = "SERVICE_ADVISOR";
    public static final String MECHANIC = "MECHANIC";

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        roles().forEach(this::seedRole);
    }

    private void seedRole(String name) {
        roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(name)
                        .build()));
    }

    private List<String> roles() {
        return List.of(
                ADMIN,
                SERVICE_ADVISOR,
                MECHANIC
        );
    }
}
