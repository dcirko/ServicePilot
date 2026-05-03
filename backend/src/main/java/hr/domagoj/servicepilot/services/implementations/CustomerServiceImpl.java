package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.CustomerDTO;
import hr.domagoj.servicepilot.entities.Customer;
import hr.domagoj.servicepilot.repos.CustomerRepository;
import hr.domagoj.servicepilot.services.interfaces.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = Customer.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .phone(dto.phone())
                .address(dto.address())
                .notes(dto.notes())
                .build();
        return toDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setAddress(dto.address());
        customer.setNotes(dto.notes());
        return toDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getNotes()
        );
    }
}
