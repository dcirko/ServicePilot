package hr.domagoj.servicepilot.seeders;

import hr.domagoj.servicepilot.entities.Customer;
import hr.domagoj.servicepilot.entities.User;
import hr.domagoj.servicepilot.repos.CustomerRepository;
import hr.domagoj.servicepilot.repos.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(4)
public class CustomerSeeder implements ApplicationRunner {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerSeeder(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        customers().forEach(this::seedCustomer);
    }

    private void seedCustomer(CustomerSeed seed) {

        User user = userRepository.findByEmail(seed.userEmail())
                .orElseThrow(() -> new IllegalStateException("User not found: " + seed.userEmail()));

        Customer customer = customerRepository.findByEmail(seed.email())
                .orElseGet(Customer::new);

        customer.setUser(user);
        customer.setFirstName(seed.firstName());
        customer.setLastName(seed.lastName());
        customer.setEmail(seed.email());
        customer.setPhone(seed.phone());
        customer.setAddress(seed.address());
        customer.setNotes(seed.notes());

        customerRepository.save(customer);
    }

    private List<CustomerSeed> customers() {
        return List.of(
                new CustomerSeed(
                        "ivan@gmail.com",
                        "Ivan",
                        "Horvat",
                        "ivan.horvat@example.com",
                        "+385911234001",
                        "Ilica 15, Zagreb",
                        "Prefers morning appointments."
                ),
                new CustomerSeed(
                        "ana@gmail.com",
                        "Ana",
                        "Kovač",
                        "ana.kovac@example.com",
                        "+385911234002",
                        "Vukovarska 20, Zagreb",
                        "Customer owns two vehicles."
                ),
                new CustomerSeed(
                        "petar@gmail.com",
                        "Petar",
                        "Marić",
                        "petar.maric@example.com",
                        "+385911234003",
                        "Maksimirska 100, Zagreb",
                        "Usually requests detailed service reports."
                )
        );
    }

    private record CustomerSeed(
            String userEmail,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address,
            String notes
    ) {
    }
}
