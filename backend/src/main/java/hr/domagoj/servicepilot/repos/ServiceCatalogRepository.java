package hr.domagoj.servicepilot.repos;

import hr.domagoj.servicepilot.entities.ServiceCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    Optional<ServiceCatalog> findByName(String name);
}
