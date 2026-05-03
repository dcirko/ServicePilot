package hr.domagoj.servicepilot.repos;

import hr.domagoj.servicepilot.entities.Customer;
import hr.domagoj.servicepilot.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByCustomer(Customer customer);
}
