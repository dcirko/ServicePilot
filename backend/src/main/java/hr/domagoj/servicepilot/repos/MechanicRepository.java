package hr.domagoj.servicepilot.repos;

import hr.domagoj.servicepilot.entities.Mechanic;
import hr.domagoj.servicepilot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    Optional<Mechanic> findByUser(User user);
}
