package hr.domagoj.servicepilot.entities;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "parts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Part extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "part_number", unique = true, length = 100)
    private String partNumber;

    @Column(length = 100)
    private String category;

    @Column(length = 100)
    private String manufacturer;

    @Column(name = "quantity_in_stock", nullable = false)
    private Integer quantityInStock = 0;

    @Column(name = "reorder_threshold")
    private Integer reorderThreshold;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(length = 150)
    private String supplier;

    @Column(nullable = false)
    private Boolean active = true;
}