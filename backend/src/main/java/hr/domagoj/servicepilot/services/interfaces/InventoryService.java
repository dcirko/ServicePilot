package hr.domagoj.servicepilot.services.interfaces;

import hr.domagoj.servicepilot.DTOs.InventoryMovementDTO;
import hr.domagoj.servicepilot.DTOs.PartDTO;
import java.util.List;

public interface InventoryService {
    List<PartDTO> getAllParts();
    PartDTO getPartById(Long id);
    PartDTO createPart(PartDTO partDTO);
    PartDTO updatePart(Long id, PartDTO partDTO);
}
