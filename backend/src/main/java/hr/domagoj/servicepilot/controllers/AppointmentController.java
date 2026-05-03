package hr.domagoj.servicepilot.controllers;

import hr.domagoj.servicepilot.DTOs.AppointmentDTO;
import hr.domagoj.servicepilot.services.implementations.AppointmentServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentServiceImpl appointmentService;

    public AppointmentController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public AppointmentDTO getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    public AppointmentDTO createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.createAppointment(appointmentDTO);
    }

    @PutMapping("/{id}")
    public AppointmentDTO updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.updateAppointment(id, appointmentDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }
}
