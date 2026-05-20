package hr.domagoj.servicepilot.seeders;

import hr.domagoj.servicepilot.entities.Role;
import hr.domagoj.servicepilot.entities.User;
import hr.domagoj.servicepilot.repos.RoleRepository;
import hr.domagoj.servicepilot.repos.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(2)
public class UserSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        users().forEach(this::seedUser);
    }

    private void seedUser(UserSeed seed) {
        if (userRepository.findByEmail(seed.email()).isPresent()) {
            return;
        }

        Role role = roleRepository.findByName(seed.roleName())
                .orElseThrow(() -> new IllegalStateException("Role not found: " + seed.roleName()));

        userRepository.save(User.builder()
                .firstName(seed.firstName())
                .lastName(seed.lastName())
                .email(seed.email())
                .password(passwordEncoder.encode(seed.password()))
                .phone(seed.phone())
                .active(true)
                .role(role)
                .build());
    }

    private List<UserSeed> users() {
        return List.of(
                new UserSeed(
                        "Admin",
                        "ServicePilot",
                        "admin@servicepilot.local",
                        "Admin123!",
                        "+385911001001",
                        RoleSeeder.ADMIN
                ),
                new UserSeed(
                        "Sara",
                        "Kolar",
                        "sara.kolar@servicepilot.local",
                        "Advisor123!",
                        "+385911001101",
                        RoleSeeder.SERVICE_ADVISOR
                ),
                new UserSeed(
                        "Marko",
                        "Horvat",
                        "marko.horvat@servicepilot.local",
                        "Mechanic123!",
                        "+385911001201",
                        RoleSeeder.MECHANIC
                ),
                new UserSeed(
                        "Ivan",
                        "Novak",
                        "ivan.novak@servicepilot.local",
                        "Mechanic123!",
                        "+385911001202",
                        RoleSeeder.MECHANIC
                )
        );
    }

    private record UserSeed(
            String firstName,
            String lastName,
            String email,
            String password,
            String phone,
            String roleName
    ) {
    }
}
