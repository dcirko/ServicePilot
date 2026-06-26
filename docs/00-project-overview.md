# ServicePilot Project Overview

## What ServicePilot Is

ServicePilot is a full-stack auto service management application for repair shops and service centers. The repository currently contains a Spring Boot backend and an Angular frontend scaffold.

The application is intended to help a workshop manage customers, vehicles, appointments, mechanics, work orders, service catalog items, parts inventory, notifications, and eventually dashboards and documents.

## Problem It Solves

Auto service workshops usually coordinate many related pieces of information: customer contact data, vehicle history, scheduled visits, assigned mechanics, work done, parts used, service status, and billing artifacts. ServicePilot is meant to centralize these workflows so service staff can see and update the same operational data.

## Users

| Role | Real-world user | Current status |
| --- | --- | --- |
| `ADMIN` | Owner, manager, or system administrator | Implemented as a seeded role in `backend/src/main/java/hr/domagoj/servicepilot/seeders/RoleSeeder.java`; no admin UI or role-protected endpoints yet |
| `SERVICE_ADVISOR` | Front desk advisor or service manager | Implemented as a seeded role; no role-specific workflow enforcement yet |
| `MECHANIC` | Technician performing repairs | Implemented as a role and `Mechanic` entity; mechanics can be listed through `/api/mechanics` |
| `CUSTOMER` | Vehicle owner | Implemented as a role and `Customer` entity; public registration creates a `User` with this role |

## Main Business Modules

| Module | Current status | Evidence |
| --- | --- | --- |
| Authentication | Implemented, with caveats | `AuthController`, `AuthServiceImpl`, `JwtService`, `RefreshTokenService`, `CookieService` |
| Users and roles | Partial | `User`, `Role`, `RoleSeeder`, `UserSeeder`; no user management controller |
| Customers | Partial | `Customer`, `CustomerController`, `CustomerServiceImpl`; API create does not set required `user` relation |
| Vehicles | Implemented backend CRUD | `Vehicle`, `VehicleController`, `VehicleServiceImpl` |
| Mechanics | Partial | `Mechanic`, `MechanicController`, `MechanicServiceImpl`; list-only API |
| Appointments | Partial | Entity and controller exist, but create/update return `null` in `AppointmentServiceImpl` |
| Work orders | Partial | CRUD exists for main `WorkOrder`, but lifecycle actions and line-item APIs are missing |
| Work order services/tasks | Entity/repository implemented, API planned | `WorkOrderService`, `WorkOrderServiceRepository`; no controller/service methods exposed |
| Parts/inventory | Partial | Parts CRUD exists; inventory movement entity exists but no endpoint for stock movements |
| Notifications | Partial | Notification entity and read/delete endpoints exist; mark-as-read is commented out |
| Dashboard | Planned | Mentioned in `README.md`; no backend or frontend module found |
| Documents/invoices | Planned | No invoice/document entity or controller found |
| Frontend application | Planned/partial scaffold | Angular 21 project exists, but routes are empty and template is Angular default |

## Current Implementation Snapshot

Implemented backend foundations:

- Spring Boot application entry point: `backend/src/main/java/hr/domagoj/servicepilot/BackendApplication.java`.
- REST controllers under `backend/src/main/java/hr/domagoj/servicepilot/controllers`.
- DTO records under `backend/src/main/java/hr/domagoj/servicepilot/DTOs`.
- JPA entities under `backend/src/main/java/hr/domagoj/servicepilot/entities`.
- Spring Data repositories under `backend/src/main/java/hr/domagoj/servicepilot/repos`.
- Service interfaces and implementations under `backend/src/main/java/hr/domagoj/servicepilot/services`.
- Security configuration and JWT/cookie auth under `backend/src/main/java/hr/domagoj/servicepilot/config` and `backend/src/main/java/hr/domagoj/servicepilot/security`.
- Demo seeders under `backend/src/main/java/hr/domagoj/servicepilot/seeders`.
- PostgreSQL Docker service in `docker-compose.yml`.

Implemented frontend foundations:

- Angular application scaffold under `frontend`.
- Main bootstrap in `frontend/src/main.ts`.
- Empty route array in `frontend/src/app/app.routes.ts`.
- Default generated Angular root component in `frontend/src/app/app.ts` and `frontend/src/app/app.html`.

## Finished Application Target

When finished, ServicePilot should support:

- Public customer registration and login.
- Admin-only employee creation for `ADMIN`, `SERVICE_ADVISOR`, and `MECHANIC`.
- Customer and vehicle management.
- Appointment scheduling and confirmation.
- Conversion from appointment to work order.
- Work order lifecycle tracking.
- Mechanic assignment and mechanic work queue.
- Work order service tasks and used parts.
- Inventory stock updates and movement history.
- Dashboard summaries for operational state.
- Notifications for appointments, work order updates, and low stock.
- Invoice or service document generation if included in scope.

## Implemented vs Planned

| Area | Implemented | Planned/TODO |
| --- | --- | --- |
| Auth | Register, login, refresh, logout, current user, refresh-token persistence | CSRF endpoint is whitelisted but not implemented; frontend auth flow missing |
| Roles | Role table and seeded role names | Role-based endpoint restrictions and admin user management |
| Customers | Entity, repository, DTO, CRUD controller/service | Fix/create flow to attach required `User`; customer portal |
| Vehicles | Entity, repository, DTO, CRUD controller/service | Customer-scoped views and service history |
| Appointments | Entity, DTO, list/read/delete endpoints | Create/update implementation, validation, calendar UI, appointment-to-work-order |
| Work orders | Entity, DTO, CRUD controller/service | Status transition endpoints, line item APIs, mechanic workflow |
| Inventory | Parts entity and CRUD | Stock movements, low-stock alerts, consumption from work orders |
| Frontend | Angular shell | All real pages, services, guards, forms, state |
| Database | JPA entities and Hibernate `ddl-auto=update` | Versioned Flyway migrations; `README.md` says Flyway, but `application.properties` disables it |

## Current Project Status

ServicePilot currently has a meaningful backend domain model and several REST modules, but it is not yet a complete workflow application. The strongest implemented areas are authentication, roles, vehicle CRUD, parts CRUD, and main work-order CRUD. The biggest gaps are frontend screens, role authorization, appointment creation/update, customer-user consistency, work-order line-item APIs, inventory movement APIs, and database migrations.

