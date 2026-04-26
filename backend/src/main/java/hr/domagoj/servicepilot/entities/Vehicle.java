package hr.domagoj.servicepilot.entities;
import hr.domagoj.servicepilot.enums.EngineType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String manufacturer;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(length = 30)
    private String vin;

    @Column(name = "registration_plate", length = 30)
    private String registrationPlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "engine_type", length = 20)
    private EngineType engineType;

    private Integer mileage;

    @Column(length = 50)
    private String color;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
