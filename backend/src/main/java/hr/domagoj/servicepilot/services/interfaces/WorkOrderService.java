package hr.domagoj.servicepilot.services.interfaces;

import hr.domagoj.servicepilot.DTOs.WorkOrderDTO;
import java.util.List;

public interface WorkOrderService {
    List<WorkOrderDTO> getAllWorkOrders();
    WorkOrderDTO getWorkOrderById(Long id);
    WorkOrderDTO createWorkOrder(WorkOrderDTO workOrderDTO);
    WorkOrderDTO updateWorkOrder(Long id, WorkOrderDTO workOrderDTO);
    void deleteWorkOrder(Long id);
}
