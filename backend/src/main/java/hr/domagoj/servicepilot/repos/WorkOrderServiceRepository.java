package hr.domagoj.servicepilot.repos;

import hr.domagoj.servicepilot.entities.WorkOrder;
import hr.domagoj.servicepilot.entities.WorkOrderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderServiceRepository extends JpaRepository<WorkOrderService, Long> {
    List<WorkOrderService> findByWorkOrder(WorkOrder workOrder);
}
