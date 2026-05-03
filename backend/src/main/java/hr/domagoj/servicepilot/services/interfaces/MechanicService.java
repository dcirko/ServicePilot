package hr.domagoj.servicepilot.services.interfaces;

import hr.domagoj.servicepilot.DTOs.MechanicDTO;
import hr.domagoj.servicepilot.entities.Mechanic;
import hr.domagoj.servicepilot.entities.User;

import java.util.List;
import java.util.Optional;

public interface MechanicService {
    MechanicDTO findByUser(User user);
    List<MechanicDTO> getAllMechanics();
}
