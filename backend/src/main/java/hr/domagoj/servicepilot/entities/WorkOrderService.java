package hr.domagoj.servicepilot.entities;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "work_order_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderService extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name_snapshot", nullable = false, length = 100)
    private String serviceNameSnapshot;

    @Column(name = "base_price_snapshot", precision = 10, scale = 2)
    private BigDecimal basePriceSnapshot;

    @Column(name = "estimated_duration_minutes_snapshot")
    private Integer estimatedDurationMinutesSnapshot;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_catalog_id", nullable = false)
    private ServiceCatalog serviceCatalog;
}
