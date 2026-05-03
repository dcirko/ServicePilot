package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.WorkOrderDTO;
import hr.domagoj.servicepilot.entities.WorkOrder;
import hr.domagoj.servicepilot.repos.*;
import hr.domagoj.servicepilot.services.interfaces.WorkOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {
    private final WorkOrderRepository workOrderRepository;
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;

    public WorkOrderServiceImpl(WorkOrderRepository workOrderRepository,
                                AppointmentRepository appointmentRepository,
                                CustomerRepository customerRepository,
                                VehicleRepository vehicleRepository,
                                MechanicRepository mechanicRepository) {
        this.workOrderRepository = workOrderRepository;
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.vehicleRepository = vehicleRepository;
        this.mechanicRepository = mechanicRepository;
    }

    @Override
    public List<WorkOrderDTO> getAllWorkOrders() {
        return workOrderRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public WorkOrderDTO getWorkOrderById(Long id) {
        return workOrderRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Work order not found"));
    }

    @Override
    public WorkOrderDTO createWorkOrder(WorkOrderDTO dto) {
        WorkOrder workOrder = WorkOrder.builder()
                .issueDescription(dto.issueDescription())
                .diagnosis(dto.diagnosis())
                .notes(dto.notes())
                .estimatedLaborHours(dto.estimatedLaborHours())
                .actualLaborHours(dto.actualLaborHours())
                .currentMileage(dto.currentMileage())
                .status(dto.status())
                .openedAt(dto.openedAt())
                .completedAt(dto.completedAt())
                .appointment(dto.appointmentId() != null ? appointmentRepository.findById(dto.appointmentId()).orElse(null) : null)
                .customer(customerRepository.findById(dto.customerId())
                        .orElseThrow(() -> new RuntimeException("Customer not found")))
                .vehicle(vehicleRepository.findById(dto.vehicleId())
                        .orElseThrow(() -> new RuntimeException("Vehicle not found")))
                .assignedMechanic(dto.assignedMechanicId() != null ? mechanicRepository.findById(dto.assignedMechanicId()).orElse(null) : null)
                .build();
        return toDTO(workOrderRepository.save(workOrder));
    }

    @Override
    public WorkOrderDTO updateWorkOrder(Long id, WorkOrderDTO dto) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work order not found"));
        workOrder.setIssueDescription(dto.issueDescription());
        workOrder.setDiagnosis(dto.diagnosis());
        workOrder.setNotes(dto.notes());
        workOrder.setEstimatedLaborHours(dto.estimatedLaborHours());
        workOrder.setActualLaborHours(dto.actualLaborHours());
        workOrder.setCurrentMileage(dto.currentMileage());
        workOrder.setStatus(dto.status());
        workOrder.setOpenedAt(dto.openedAt());
        workOrder.setCompletedAt(dto.completedAt());
        workOrder.setAppointment(dto.appointmentId() != null ? appointmentRepository.findById(dto.appointmentId()).orElse(null) : null);
        workOrder.setCustomer(customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found")));
        workOrder.setVehicle(vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found")));
        workOrder.setAssignedMechanic(dto.assignedMechanicId() != null ? mechanicRepository.findById(dto.assignedMechanicId()).orElse(null) : null);
        return toDTO(workOrderRepository.save(workOrder));
    }

    @Override
    public void deleteWorkOrder(Long id) {
        workOrderRepository.deleteById(id);
    }

    private WorkOrderDTO toDTO(WorkOrder workOrder) {
        return new WorkOrderDTO(
                workOrder.getId(),
                workOrder.getIssueDescription(),
                workOrder.getDiagnosis(),
                workOrder.getNotes(),
                workOrder.getEstimatedLaborHours(),
                workOrder.getActualLaborHours(),
                workOrder.getCurrentMileage(),
                workOrder.getStatus(),
                workOrder.getOpenedAt(),
                workOrder.getCompletedAt(),
                workOrder.getAppointment() != null ? workOrder.getAppointment().getId() : null,
                workOrder.getCustomer().getId(),
                workOrder.getVehicle().getId(),
                workOrder.getAssignedMechanic() != null ? workOrder.getAssignedMechanic().getId() : null
        );
    }
}
