package hr.domagoj.servicepilot.seeders;

import hr.domagoj.servicepilot.entities.Customer;
import hr.domagoj.servicepilot.repos.CustomerRepository;
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

    public CustomerSeeder(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        customers().forEach(this::seedCustomer);
    }

    private void seedCustomer(CustomerSeed seed) {
        if (customerRepository.findByEmail(seed.email()).isPresent()) {
            return;
        }

        customerRepository.save(Customer.builder()
                .firstName(seed.firstName())
                .lastName(seed.lastName())
                .email(seed.email())
                .phone(seed.phone())
                .address(seed.address())
                .notes(seed.notes())
                .build());
    }

    private List<CustomerSeed> customers() {
        return List.of(
                new CustomerSeed(
                        "Ivan",
                        "Horvat",
                        "ivan.horvat@example.com",
                        "+385911234001",
                        "Ilica 15, Zagreb",
                        "Prefers morning appointments."
                ),
                new CustomerSeed(
                        "Ana",
                        "Kovač",
                        "ana.kovac@example.com",
                        "+385911234002",
                        "Vukovarska 20, Zagreb",
                        "Customer owns two vehicles."
                ),
                new CustomerSeed(
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
            String firstName,
            String lastName,
            String email,
            String phone,
            String address,
            String notes
    ) {
    }
}
