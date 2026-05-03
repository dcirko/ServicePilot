package hr.domagoj.servicepilot.services.interfaces;

import hr.domagoj.servicepilot.DTOs.CustomerDTO;
import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Long id);
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    void deleteCustomer(Long id);
}
