# API Map

Security note: except for public auth endpoints, current API access is authenticated but not role-specific. Required roles below are recommended/intended unless marked public.

## Auth

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `POST` | `/api/auth/register` | Register public customer user | Public | `RegisterRequest` | `AuthResponse` | Implemented |
| `POST` | `/api/auth/login` | Login and issue cookies | Public | `LoginRequest` | `AuthResponse` | Implemented |
| `POST` | `/api/auth/refresh` | Rotate refresh token and issue new cookies | Public via refresh cookie | Cookie | `AuthResponse` | Implemented |
| `POST` | `/api/auth/logout` | Revoke refresh token and clear cookies | Authenticated | Cookie | No content | Implemented |
| `GET` | `/api/auth/me` | Return current user | Authenticated | None | `CurrentUserResponse` | Implemented |
| `GET` | `/api/auth/csrf` | Return CSRF token | Public | None | TODO | Planned/TODO; whitelisted but no controller method |

## Users

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/users` | List users/employees | `ADMIN` | None | `UserDTO[]` | Planned |
| `POST` | `/api/users` | Create employee user | `ADMIN` | Planned DTO | `UserDTO` | Planned |
| `PUT` | `/api/users/{id}` | Update user | `ADMIN` | Planned DTO | `UserDTO` | Planned |
| `PATCH` | `/api/users/{id}/deactivate` | Deactivate user | `ADMIN` | None | `UserDTO` | Planned |

## Customers

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/customers` | List customers | `ADMIN`, `SERVICE_ADVISOR` | None | `CustomerDTO[]` | Implemented |
| `GET` | `/api/customers/{id}` | Get customer | `ADMIN`, `SERVICE_ADVISOR`, owner | None | `CustomerDTO` | Implemented |
| `POST` | `/api/customers` | Create customer profile | `ADMIN`, `SERVICE_ADVISOR` | `CustomerDTO` | `CustomerDTO` | Partial; missing required user relation |
| `PUT` | `/api/customers/{id}` | Update customer | `ADMIN`, `SERVICE_ADVISOR`, owner | `CustomerDTO` | `CustomerDTO` | Implemented with same relation caveat |
| `DELETE` | `/api/customers/{id}` | Delete customer | `ADMIN` | None | Void | Implemented |

## Vehicles

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/vehicles` | List vehicles | `ADMIN`, `SERVICE_ADVISOR` | None | `VehicleDTO[]` | Implemented |
| `GET` | `/api/vehicles/{id}` | Get vehicle | `ADMIN`, `SERVICE_ADVISOR`, owner | None | `VehicleDTO` | Implemented |
| `POST` | `/api/vehicles` | Create vehicle | `ADMIN`, `SERVICE_ADVISOR`, owner | `VehicleDTO` | `VehicleDTO` | Implemented |
| `PUT` | `/api/vehicles/{id}` | Update vehicle | `ADMIN`, `SERVICE_ADVISOR`, owner | `VehicleDTO` | `VehicleDTO` | Implemented |
| `DELETE` | `/api/vehicles/{id}` | Delete vehicle | `ADMIN`, `SERVICE_ADVISOR` | None | Void | Implemented |

## Mechanics

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/mechanics` | List mechanics | `ADMIN`, `SERVICE_ADVISOR` | None | `MechanicDTO[]` | Implemented |
| `POST` | `/api/mechanics` | Create mechanic profile | `ADMIN` | Planned DTO | `MechanicDTO` | Planned |
| `PUT` | `/api/mechanics/{id}` | Update mechanic | `ADMIN` | Planned DTO | `MechanicDTO` | Planned |

## Appointments

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/appointments` | List appointments | `ADMIN`, `SERVICE_ADVISOR`, scoped roles | None | `AppointmentDTO[]` | Implemented |
| `GET` | `/api/appointments/{id}` | Get appointment | `ADMIN`, `SERVICE_ADVISOR`, owner/assigned mechanic | None | `AppointmentDTO` | Implemented |
| `POST` | `/api/appointments` | Create appointment | `ADMIN`, `SERVICE_ADVISOR`, `CUSTOMER` own | `AppointmentDTO` | `AppointmentDTO` | Partial; service returns `null` |
| `PUT` | `/api/appointments/{id}` | Update appointment | `ADMIN`, `SERVICE_ADVISOR` | `AppointmentDTO` | `AppointmentDTO` | Partial; service returns `null` |
| `DELETE` | `/api/appointments/{id}` | Delete/cancel appointment | `ADMIN`, `SERVICE_ADVISOR` | None | Void | Implemented as delete |
| `POST` | `/api/appointments/{id}/convert-to-work-order` | Convert appointment to work order | `ADMIN`, `SERVICE_ADVISOR` | Planned DTO | `WorkOrderDTO` | Planned |

## Work Orders

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/work-orders` | List work orders | `ADMIN`, `SERVICE_ADVISOR`, scoped `MECHANIC` | None | `WorkOrderDTO[]` | Implemented |
| `GET` | `/api/work-orders/{id}` | Get work order | `ADMIN`, `SERVICE_ADVISOR`, assigned mechanic, owner | None | `WorkOrderDTO` | Implemented |
| `POST` | `/api/work-orders` | Create work order | `ADMIN`, `SERVICE_ADVISOR` | `WorkOrderDTO` | `WorkOrderDTO` | Implemented main record |
| `PUT` | `/api/work-orders/{id}` | Update work order | `ADMIN`, `SERVICE_ADVISOR`, assigned mechanic limited | `WorkOrderDTO` | `WorkOrderDTO` | Implemented main record |
| `DELETE` | `/api/work-orders/{id}` | Delete work order | `ADMIN` | None | Void | Implemented |
| `PATCH` | `/api/work-orders/{id}/status` | Transition status | `ADMIN`, `SERVICE_ADVISOR`, assigned mechanic limited | Planned DTO | `WorkOrderDTO` | Planned |
| `POST` | `/api/work-orders/{id}/services` | Add service task | `ADMIN`, `SERVICE_ADVISOR`, assigned mechanic limited | Planned DTO | Planned DTO | Planned |
| `POST` | `/api/work-orders/{id}/parts` | Add used part | `ADMIN`, `SERVICE_ADVISOR`, assigned mechanic limited | Planned DTO | Planned DTO | Planned |

## Inventory

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/inventory/parts` | List parts | Authenticated, ideally staff | None | `PartDTO[]` | Implemented |
| `GET` | `/api/inventory/parts/{id}` | Get part | Authenticated, ideally staff | None | `PartDTO` | Implemented |
| `POST` | `/api/inventory/parts` | Create part | `ADMIN`, `SERVICE_ADVISOR` | `PartDTO` | `PartDTO` | Implemented |
| `PUT` | `/api/inventory/parts/{id}` | Update part | `ADMIN`, `SERVICE_ADVISOR` | `PartDTO` | `PartDTO` | Implemented |
| `GET` | `/api/inventory/movements` | List stock movements | `ADMIN`, `SERVICE_ADVISOR` | Filters | `InventoryMovementDTO[]` | Planned |
| `POST` | `/api/inventory/movements` | Create stock movement | `ADMIN`, `SERVICE_ADVISOR` | `InventoryMovementDTO` | `InventoryMovementDTO` | Planned |

## Notifications

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/notifications/user/{userId}` | List notifications for a user | Current user or admin | None | `NotificationDTO[]` | Implemented, owner check planned |
| `DELETE` | `/api/notifications/{id}` | Delete notification | Current user or admin | None | Void | Implemented, owner check planned |
| `PATCH` | `/api/notifications/{id}/read` | Mark notification as read | Current user or admin | None | `NotificationDTO` | Planned/commented TODO |

## Dashboard

| Method | URL | Purpose | Required role | Request DTO | Response DTO | Status |
| --- | --- | --- | --- | --- | --- | --- |
| `GET` | `/api/dashboard/summary` | Counts and operational summary | `ADMIN`, `SERVICE_ADVISOR` | None | Planned DTO | Planned |
| `GET` | `/api/dashboard/mechanic/{id}` | Mechanic workload | `ADMIN`, `SERVICE_ADVISOR`, assigned mechanic | None | Planned DTO | Planned |

