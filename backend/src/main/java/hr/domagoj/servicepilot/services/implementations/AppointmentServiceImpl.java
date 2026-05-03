package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.AppointmentDTO;
import hr.domagoj.servicepilot.entities.Appointment;
import hr.domagoj.servicepilot.repos.*;
import hr.domagoj.servicepilot.services.interfaces.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    /*private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;
    private final ServiceCatalogRepository serviceCatalogRepository;
*/
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  CustomerRepository customerRepository,
                                  VehicleRepository vehicleRepository,
                                  MechanicRepository mechanicRepository,
                                  ServiceCatalogRepository serviceCatalogRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public AppointmentDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        return null;
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto) {
       return null;
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private AppointmentDTO toDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getScheduledStart(),
                appointment.getScheduledEnd(),
                appointment.getIssueDescription(),
                appointment.getStatus(),
                appointment.getNotes(),
                appointment.getCustomer().getId(),
                appointment.getVehicle().getId(),
                appointment.getAssignedMechanic() != null ? appointment.getAssignedMechanic().getId() : null,
                appointment.getRequestedService() != null ? appointment.getRequestedService().getId() : null
        );
    }
}
