package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.MechanicDTO;
import hr.domagoj.servicepilot.entities.Mechanic;
import hr.domagoj.servicepilot.entities.User;
import hr.domagoj.servicepilot.repos.MechanicRepository;
import hr.domagoj.servicepilot.services.interfaces.MechanicService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MechanicServiceImpl implements MechanicService {
    private final MechanicRepository mechanicRepository;
    public MechanicServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }
    
    @Override
    public MechanicDTO findByUser(User user) {
        Optional<Mechanic> mechanic = mechanicRepository.findByUser(user);
        return mechanic.map(this::toDTO).orElseThrow(() -> new RuntimeException("Mechanic not found for user."));
    }

    @Override
    public List<MechanicDTO> getAllMechanics() {
        return mechanicRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }
    
    private MechanicDTO toDTO(Mechanic mechanic) {
        return new MechanicDTO(
                mechanic.getSpecialization(),
                mechanic.getExperienceYears(),
                mechanic.getHourlyRate(),
                mechanic.getEmploymentType(),
                mechanic.getAvailabilityStatus(),
                mechanic.getActive(),
                mechanic.getNotes()
        );
    }
}
