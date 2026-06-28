package hr.domagoj.servicepilot.controllers;

import hr.domagoj.servicepilot.DTOs.MechanicDTO;
import hr.domagoj.servicepilot.services.implementations.MechanicServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mechanics")
@Tag(name = "Mechanics")
public class MechanicController {
    private final MechanicServiceImpl mechanicService;
    public MechanicController(MechanicServiceImpl mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping()
    public List<MechanicDTO> getAllMechanics() {
        return mechanicService.getAllMechanics();
    }
}
