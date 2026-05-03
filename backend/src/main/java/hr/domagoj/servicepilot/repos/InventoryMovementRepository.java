package hr.domagoj.servicepilot.repos;

import hr.domagoj.servicepilot.entities.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
}
