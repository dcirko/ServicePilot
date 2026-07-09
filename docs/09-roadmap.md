# Roadmap

## Phase 1: Project Foundation

| Item | Details |
| --- | --- |
| Goal | Make the project consistent, buildable, and documented |
| Main tasks | Confirm real tech versions, align README, keep this docs map updated |
| Backend tasks | Add baseline tests for context load and key services; decide exception strategy for CRUD services |
| Frontend tasks | Keep app shell/route structure aligned with docs; update generated tests for the custom shell |
| Database tasks | Decide whether Hibernate update is temporary; start Flyway migrations |
| Acceptance criteria | Backend compiles, docs match code, application can start with local PostgreSQL |

## Phase 2: Authentication And Roles

| Item | Details |
| --- | --- |
| Goal | Complete secure login/session and role enforcement |
| Main tasks | Add route guards, role-based authorization, employee creation rules, and auth tests |
| Backend tasks | Add `UserController` for admin-created employees; add `@PreAuthorize`; add auth/security integration tests |
| Frontend tasks | Add route guards, shared session/current-user service, and role-aware navigation |
| Database tasks | Add user/role migrations and indexes |
| Acceptance criteria | Public registration creates only customer users and linked customer profiles; admin creates employees; protected endpoints reject unauthorized roles |

## Phase 3: Customers And Vehicles

| Item | Details |
| --- | --- |
| Goal | Solid customer and vehicle records |
| Main tasks | Fix customer-user relationship, validate vehicles, add ownership checks |
| Backend tasks | Update customer DTO/API design; validate customer/vehicle consistency; add filters by customer |
| Frontend tasks | Customers and vehicles list/detail/forms |
| Database tasks | Index `customers.user_id`, `customers.email`, `vehicles.customer_id`; decide unique constraints for VIN/plate |
| Acceptance criteria | Staff can create customers and vehicles; customers can only see their own records |

## Phase 4: Appointments

| Item | Details |
| --- | --- |
| Goal | Implement real scheduling |
| Main tasks | Implement appointment create/update and calendar/list workflows |
| Backend tasks | Complete `AppointmentServiceImpl.createAppointment` and `updateAppointment`; validate dates, vehicle ownership, mechanic availability |
| Frontend tasks | Appointment list/calendar, create/edit modal or page, status display |
| Database tasks | Index `appointments.scheduled_start`, `customer_id`, `vehicle_id`, `assigned_mechanic_id`, `status` |
| Acceptance criteria | A service advisor or customer can create a valid appointment and staff can confirm/update it |

## Phase 5: Work Orders

| Item | Details |
| --- | --- |
| Goal | Make work orders the center of workshop execution |
| Main tasks | Add lifecycle transitions, assignment, appointment conversion, tasks, and parts |
| Backend tasks | Dedicated appointment-to-work-order endpoint; status transition service; endpoints for `WorkOrderService` and `WorkOrderPart` |
| Frontend tasks | Work order list/detail, status controls, mechanic assignment, service/parts sections |
| Database tasks | FK indexes on work-order child tables; optional status history table |
| Acceptance criteria | Staff can convert an appointment into a work order, assign a mechanic, update status, add services and parts, and complete the job |

## Phase 6: Inventory

| Item | Details |
| --- | --- |
| Goal | Track stock and usage reliably |
| Main tasks | Add stock movements and integrate part usage |
| Backend tasks | Inventory movement service/endpoints; transactional stock deduction when adding work-order parts; low-stock queries |
| Frontend tasks | Inventory table, stock movement history, low-stock warnings |
| Database tasks | Index movement part/date/user/work order columns |
| Acceptance criteria | Every stock change has a movement record and work-order part usage adjusts inventory consistently |

## Phase 7: Dashboard

| Item | Details |
| --- | --- |
| Goal | Give staff a useful operational overview |
| Main tasks | Add summary metrics and role-specific dashboards |
| Backend tasks | Dashboard aggregation service for appointments, active work orders, mechanic workload, low stock |
| Frontend tasks | Dashboard page with actionable cards/tables |
| Database tasks | Add indexes needed by dashboard queries |
| Acceptance criteria | Dashboard shows today's appointments, active work orders, work waiting for parts, low stock, and mechanic load |

## Phase 8: Polish, Testing, Deployment Later

| Item | Details |
| --- | --- |
| Goal | Prepare ServicePilot for reliable demo/deployment |
| Main tasks | Test coverage, error handling, API consistency, deployment setup |
| Backend tasks | Integration tests, validation annotations, consistent exception handling, API docs |
| Frontend tasks | Loading/error states, responsive layout, form validation, role-specific navigation |
| Database tasks | Production-ready migrations, seed strategy, backups |
| Acceptance criteria | Clean test/build pipeline, documented setup, predictable deployment path |

## Recommended Next Implementation Step

Implement the remaining Phase 2 foundations before adding more domain screens: add frontend auth/role guards, promote current-user state into a shared session service, create admin user-management, enforce role-based access, and add auth/security tests. Immediately after that, complete appointment create/update because it blocks the rest of the service workflow.

