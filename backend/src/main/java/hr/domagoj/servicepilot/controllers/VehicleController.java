package hr.domagoj.servicepilot.controllers;

import hr.domagoj.servicepilot.DTOs.VehicleDTO;
import hr.domagoj.servicepilot.services.implementations.VehicleServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleServiceImpl vehicleService;

    public VehicleController(VehicleServiceImpl vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public VehicleDTO getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    @PostMapping
    public VehicleDTO createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.createVehicle(vehicleDTO);
    }

    @PutMapping("/{id}")
    public VehicleDTO updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.updateVehicle(id, vehicleDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }
}
