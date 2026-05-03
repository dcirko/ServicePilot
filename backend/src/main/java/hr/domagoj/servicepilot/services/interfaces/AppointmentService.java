package hr.domagoj.servicepilot.services.interfaces;

import hr.domagoj.servicepilot.DTOs.AppointmentDTO;
import java.util.List;

public interface AppointmentService {
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO getAppointmentById(Long id);
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);
}
