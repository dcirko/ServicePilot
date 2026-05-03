package hr.domagoj.servicepilot.services.interfaces;

import hr.domagoj.servicepilot.DTOs.VehicleDTO;
import java.util.List;

public interface VehicleService {
    List<VehicleDTO> getAllVehicles();
    VehicleDTO getVehicleById(Long id);
    VehicleDTO createVehicle(VehicleDTO vehicleDTO);
    VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO);
    void deleteVehicle(Long id);
}
