package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.VehicleDTO;
import hr.domagoj.servicepilot.entities.Vehicle;
import hr.domagoj.servicepilot.repos.CustomerRepository;
import hr.domagoj.servicepilot.repos.VehicleRepository;
import hr.domagoj.servicepilot.services.interfaces.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, CustomerRepository customerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public VehicleDTO getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    @Override
    public VehicleDTO createVehicle(VehicleDTO dto) {
        Vehicle vehicle = Vehicle.builder()
                .manufacturer(dto.manufacturer())
                .model(dto.model())
                .year(dto.year())
                .vin(dto.vin())
                .registrationPlate(dto.registrationPlate())
                .engineType(dto.engineType())
                .mileage(dto.mileage())
                .color(dto.color())
                .notes(dto.notes())
                .customer(customerRepository.findById(dto.customerId())
                        .orElseThrow(() -> new RuntimeException("Customer not found")))
                .build();
        return toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleDTO updateVehicle(Long id, VehicleDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        vehicle.setManufacturer(dto.manufacturer());
        vehicle.setModel(dto.model());
        vehicle.setYear(dto.year());
        vehicle.setVin(dto.vin());
        vehicle.setRegistrationPlate(dto.registrationPlate());
        vehicle.setEngineType(dto.engineType());
        vehicle.setMileage(dto.mileage());
        vehicle.setColor(dto.color());
        vehicle.setNotes(dto.notes());
        vehicle.setCustomer(customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found")));
        return toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    private VehicleDTO toDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getManufacturer(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getVin(),
                vehicle.getRegistrationPlate(),
                vehicle.getEngineType(),
                vehicle.getMileage(),
                vehicle.getColor(),
                vehicle.getNotes(),
                vehicle.getCustomer().getId()
        );
    }
}
