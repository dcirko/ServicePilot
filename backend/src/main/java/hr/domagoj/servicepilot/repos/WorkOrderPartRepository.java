package hr.domagoj.servicepilot.repos;

import hr.domagoj.servicepilot.entities.WorkOrder;
import hr.domagoj.servicepilot.entities.WorkOrderPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderPartRepository extends JpaRepository<WorkOrderPart, Long> {
    List<WorkOrderPart> findByWorkOrder(WorkOrder workOrder);
}
