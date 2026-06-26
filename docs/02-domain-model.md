# Domain Model

## Domain Areas

ServicePilot already models most core auto-service domains in backend entities. Some are fully wired to APIs, while others exist only as entities/repositories.

| Domain area | Current status |
| --- | --- |
| Users | Implemented backend auth/user entity; user management planned |
| Roles | Implemented |
| Customers | Partial |
| Vehicles | Implemented backend CRUD |
| Appointments | Partial |
| Work orders | Partial |
| Work order items/tasks | Entity/repository implemented; API planned |
| Parts/inventory | Partial |
| Invoices/documents | Planned |
| Notifications | Partial |

## Existing Entities

### BaseEntity

- File: `backend/src/main/java/hr/domagoj/servicepilot/entities/BaseEntity.java`
- Status: implemented.
- Purpose: shared audit timestamps for entities.
- Fields: `createdAt`, `updatedAt`.
- Business meaning: records when data was created or changed.
- Relationships: none.

### Role

- File: `entities/Role.java`
- Status: implemented.
- Fields: `id`, `name`, inherited timestamps.
- Relationships: referenced by `User.role`.
- Business meaning: application role such as `ADMIN`, `SERVICE_ADVISOR`, `MECHANIC`, `CUSTOMER`.
- Seed data: `RoleSeeder` creates all four roles.

### User

- File: `entities/User.java`
- Status: implemented for authentication; user-management module is planned.
- Fields: `id`, `firstName`, `lastName`, `email`, `password`, `phone`, `active`, `role`.
- Relationships: many users belong to one role through `role_id`.
- Business meaning: login identity for employees and customers.
- Notes: `getRoles()` returns a singleton set from the single `role` field, so the model is single-role per user.

### RefreshToken

- File: `entities/RefreshToken.java`
- Status: implemented.
- Fields: `id`, `user`, `tokenHash`, `expiresAt`, `revokedAt`, `replacedByTokenHash`, `createdByIp`, `revokedByIp`.
- Relationships: many refresh tokens belong to one user.
- Business meaning: persistent refresh-token sessions for cookie auth.
- Indexes implemented: `idx_refresh_tokens_token_hash` and `idx_refresh_tokens_user_id`.

### Customer

- File: `entities/Customer.java`
- Status: partial.
- Fields: `id`, `firstName`, `lastName`, `email`, `phone`, `address`, `notes`, `user`.
- Relationships: one customer profile is linked to one `User` by unique, non-null `user_id`.
- Business meaning: customer profile used for vehicles, appointments, and work orders.
- Risk: `CustomerServiceImpl.createCustomer` builds a `Customer` without setting `user`, while the entity requires `user_id` to be non-null. This can fail at runtime unless the database is already relaxed or code is updated later.
- Planned: registration should create or connect a customer profile for public `CUSTOMER` users.

### Mechanic

- File: `entities/Mechanic.java`
- Status: partial.
- Fields: `id`, `specialization`, `experienceYears`, `hourlyRate`, `employmentType`, `availabilityStatus`, `active`, `notes`, `user`.
- Relationships: one mechanic profile is linked to one `User` by unique, non-null `user_id`.
- Business meaning: employee technician who can be assigned to appointments and work orders.
- Current API: `GET /api/mechanics` lists mechanics.
- Planned: admin create/update, schedule, workload, and assigned work queue.

### Vehicle

- File: `entities/Vehicle.java`
- Status: implemented backend CRUD.
- Fields: `id`, `manufacturer`, `model`, `year`, `vin`, `registrationPlate`, `engineType`, `mileage`, `color`, `notes`, `customer`.
- Relationships: many vehicles belong to one customer.
- Business meaning: vehicle being serviced.
- Current API: `VehicleController` exposes CRUD.

### Appointment

- File: `entities/Appointment.java`
- Status: partial.
- Fields: `id`, `scheduledStart`, `scheduledEnd`, `issueDescription`, `status`, `notes`, `customer`, `vehicle`, `assignedMechanic`, `requestedService`.
- Relationships: appointment belongs to customer and vehicle; optionally assigned mechanic and requested service catalog entry.
- Business meaning: scheduled service visit or request.
- Current API: controller exposes CRUD, but `AppointmentServiceImpl.createAppointment` and `updateAppointment` currently return `null`.

### ServiceCatalog

- File: `entities/ServiceCatalog.java`
- Status: implemented as entity/repository/seed data.
- Fields: `id`, `name`, `description`, `estimatedDurationMinutes`, `basePrice`, `active`.
- Relationships: referenced by `Appointment.requestedService` and `WorkOrderService.serviceCatalog`.
- Business meaning: reusable service definition such as diagnostics or brake pad replacement.
- Planned: public/admin API for managing service catalog items.

### WorkOrder

- File: `entities/WorkOrder.java`
- Status: partial.
- Fields: `id`, `issueDescription`, `diagnosis`, `notes`, `estimatedLaborHours`, `actualLaborHours`, `currentMileage`, `status`, `openedAt`, `completedAt`, `appointment`, `customer`, `vehicle`, `assignedMechanic`.
- Relationships: optional one-to-one appointment; many work orders per customer, vehicle, mechanic.
- Business meaning: active service job.
- Current API: CRUD exists in `WorkOrderController`.
- Planned: lifecycle endpoints, appointment conversion, task/part attachment, mechanic work queue.

### WorkOrderService

- File: `entities/WorkOrderService.java`
- Status: entity/repository implemented; workflow API planned.
- Fields: `id`, `serviceNameSnapshot`, `basePriceSnapshot`, `estimatedDurationMinutesSnapshot`, `notes`, `workOrder`, `serviceCatalog`.
- Relationships: many service line items belong to one work order and one service catalog item.
- Business meaning: service task performed on a work order, with snapshots to preserve historical price/name.

### WorkOrderPart

- File: `entities/WorkOrderPart.java`
- Status: entity/repository implemented; workflow API planned.
- Fields: `id`, `quantityUsed`, `unitPriceSnapshot`, `workOrder`, `part`.
- Relationships: many part usages belong to one work order and one part.
- Business meaning: parts consumed by a repair.

### Part

- File: `entities/Part.java`
- Status: partial.
- Fields: `id`, `name`, `partNumber`, `category`, `manufacturer`, `quantityInStock`, `reorderThreshold`, `unitPrice`, `supplier`, `active`.
- Relationships: referenced by `WorkOrderPart` and `InventoryMovement`.
- Business meaning: stock item in the workshop inventory.
- Current API: parts CRUD under `/api/inventory/parts`.
- Planned: stock movement and low-stock workflows.

### InventoryMovement

- File: `entities/InventoryMovement.java`
- Status: entity/repository implemented; API planned.
- Fields: `id`, `movementType`, `quantity`, `previousStock`, `newStock`, `note`, `part`, `workOrder`, `createdByUser`.
- Relationships: movement belongs to a part; may reference a work order and a user.
- Business meaning: audit trail for inventory changes.

### Notification

- File: `entities/Notification.java`
- Status: partial.
- Fields: `id`, `type`, `title`, `message`, `referenceType`, `referenceId`, `read`, `readAt`, `user`.
- Relationships: notification optionally belongs to a user.
- Business meaning: alert or status message for a user.
- Current API: list by user and delete; mark-as-read is commented out in `NotificationService`.

## Existing Enums

| Enum | Values | Used by |
| --- | --- | --- |
| `AppointmentStatus` | `REQUESTED`, `CONFIRMED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`, `NO_SHOW` | `Appointment` |
| `WorkOrderStatus` | `CREATED`, `SCHEDULED`, `IN_PROGRESS`, `WAITING_FOR_PARTS`, `COMPLETED`, `CANCELLED` | `WorkOrder` |
| `EngineType` | `PETROL`, `DIESEL`, `ELECTRIC`, `HYBRID` | `Vehicle` |
| `EmploymentType` | `FULL_TIME`, `PART_TIME`, `CONTRACT` | `Mechanic` |
| `AvailabilityStatus` | `AVAILABLE`, `BUSY`, `ON_LEAVE`, `OFFLINE` | `Mechanic` |
| `MovementType` | `IN`, `OUT`, `ADJUSTMENT`, `RETURN` | `InventoryMovement` |
| `NotificationType` | `INFO`, `WARNING`, `SUCCESS`, `ERROR`, `APPOINTMENT`, `WORK_ORDER`, `SYSTEM` | `Notification` |
| `ReferenceType` | `WORK_ORDER`, `APPOINTMENT`, `VEHICLE`, `CUSTOMER` | `Notification` |

## Planned Core Entities

| Entity | Status | Reason |
| --- | --- | --- |
| `Invoice` or `WorkOrderDocument` | Planned | Needed for billing or printable service report |
| `InvoiceLine` or document line item | Planned | Needed to snapshot labor/services/parts totals |
| `AuditLog` | Planned/TODO | Useful for admin/security traceability |
| `CustomerVehicleAccess` | Assumption/planned only if multiple account access is needed | Could support shared vehicle access or fleet accounts |

