package hr.domagoj.servicepilot.controllers;

import hr.domagoj.servicepilot.DTOs.InventoryMovementDTO;
import hr.domagoj.servicepilot.DTOs.PartDTO;
import hr.domagoj.servicepilot.services.implementations.InventoryServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory")
public class InventoryController {
    private final InventoryServiceImpl inventoryService;

    public InventoryController(InventoryServiceImpl inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/parts")
    public List<PartDTO> getAllParts() {
        return inventoryService.getAllParts();
    }

    @GetMapping("/parts/{id}")
    public PartDTO getPartById(@PathVariable Long id) {
        return inventoryService.getPartById(id);
    }

    @PostMapping("/parts")
    public PartDTO createPart(@RequestBody PartDTO partDTO) {
        return inventoryService.createPart(partDTO);
    }

    @PutMapping("/parts/{id}")
    public PartDTO updatePart(@PathVariable Long id, @RequestBody PartDTO partDTO) {
        return inventoryService.updatePart(id, partDTO);
    }
}
