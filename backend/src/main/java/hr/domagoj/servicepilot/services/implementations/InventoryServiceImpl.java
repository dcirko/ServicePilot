package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.InventoryMovementDTO;
import hr.domagoj.servicepilot.DTOs.PartDTO;
import hr.domagoj.servicepilot.entities.InventoryMovement;
import hr.domagoj.servicepilot.entities.Part;
import hr.domagoj.servicepilot.repos.InventoryMovementRepository;
import hr.domagoj.servicepilot.repos.PartRepository;
import hr.domagoj.servicepilot.repos.UserRepository;
import hr.domagoj.servicepilot.repos.WorkOrderRepository;
import hr.domagoj.servicepilot.services.interfaces.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final PartRepository partRepository;
    private final InventoryMovementRepository movementRepository;
    private final WorkOrderRepository workOrderRepository;
    private final UserRepository userRepository;

    public InventoryServiceImpl(PartRepository partRepository,
                                InventoryMovementRepository movementRepository,
                                WorkOrderRepository workOrderRepository,
                                UserRepository userRepository) {
        this.partRepository = partRepository;
        this.movementRepository = movementRepository;
        this.workOrderRepository = workOrderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PartDTO> getAllParts() {
        return partRepository.findAll().stream().map(this::toPartDTO).toList();
    }

    @Override
    public PartDTO getPartById(Long id) {
        return partRepository.findById(id).map(this::toPartDTO).orElseThrow(() -> new RuntimeException("Part not found"));
    }

    @Override
    public PartDTO createPart(PartDTO dto) {
        Part part = Part.builder()
                .name(dto.name())
                .partNumber(dto.partNumber())
                .category(dto.category())
                .manufacturer(dto.manufacturer())
                .quantityInStock(dto.quantityInStock() != null ? dto.quantityInStock() : Integer.valueOf(0))
                .reorderThreshold(dto.reorderThreshold())
                .unitPrice(dto.unitPrice())
                .supplier(dto.supplier())
                .active(dto.active() != null ? dto.active() : Boolean.TRUE)
                .build();
        return toPartDTO(partRepository.save(part));
    }

    @Override
    public PartDTO updatePart(Long id, PartDTO dto) {
        Part part = partRepository.findById(id).orElseThrow(() -> new RuntimeException("Part not found"));
        part.setName(dto.name());
        part.setPartNumber(dto.partNumber());
        part.setCategory(dto.category());
        part.setManufacturer(dto.manufacturer());
        part.setQuantityInStock(dto.quantityInStock());
        part.setReorderThreshold(dto.reorderThreshold());
        part.setUnitPrice(dto.unitPrice());
        part.setSupplier(dto.supplier());
        part.setActive(dto.active());
        return toPartDTO(partRepository.save(part));
    }


    private PartDTO toPartDTO(Part part) {
        return new PartDTO(
                part.getId(),
                part.getName(),
                part.getPartNumber(),
                part.getCategory(),
                part.getManufacturer(),
                part.getQuantityInStock(),
                part.getReorderThreshold(),
                part.getUnitPrice(),
                part.getSupplier(),
                part.getActive()
        );
    }

    private InventoryMovementDTO toMovementDTO(InventoryMovement movement) {
        return new InventoryMovementDTO(
                movement.getId(),
                movement.getMovementType(),
                movement.getQuantity(),
                movement.getPreviousStock(),
                movement.getNewStock(),
                movement.getNote(),
                movement.getPart().getId(),
                movement.getWorkOrder() != null ? movement.getWorkOrder().getId() : null,
                movement.getCreatedByUser() != null ? movement.getCreatedByUser().getId() : null,
                movement.getCreatedAt()
        );
    }
}
