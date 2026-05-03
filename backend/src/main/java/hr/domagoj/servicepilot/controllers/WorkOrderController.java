package hr.domagoj.servicepilot.controllers;

import hr.domagoj.servicepilot.DTOs.WorkOrderDTO;
import hr.domagoj.servicepilot.services.implementations.WorkOrderServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {
    private final WorkOrderServiceImpl workOrderService;

    public WorkOrderController(WorkOrderServiceImpl workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public List<WorkOrderDTO> getAllWorkOrders() {
        return workOrderService.getAllWorkOrders();
    }

    @GetMapping("/{id}")
    public WorkOrderDTO getWorkOrderById(@PathVariable Long id) {
        return workOrderService.getWorkOrderById(id);
    }

    @PostMapping
    public WorkOrderDTO createWorkOrder(@RequestBody WorkOrderDTO workOrderDTO) {
        return workOrderService.createWorkOrder(workOrderDTO);
    }

    @PutMapping("/{id}")
    public WorkOrderDTO updateWorkOrder(@PathVariable Long id, @RequestBody WorkOrderDTO workOrderDTO) {
        return workOrderService.updateWorkOrder(id, workOrderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkOrder(@PathVariable Long id) {
        workOrderService.deleteWorkOrder(id);
    }
}
