# User Roles And Permissions

## Current Security Reality

Roles are implemented in the database and exposed as Spring Security authorities with the `ROLE_` prefix in `CustomUserPrincipal`.

Current endpoint access is not role-specific:

- `/api/auth/register`, `/api/auth/login`, `/api/auth/refresh`, and `/api/auth/csrf` are public in `SecurityConfig`.
- All other endpoints require authentication.
- `@EnableMethodSecurity` is enabled, but no `@PreAuthorize` rules were found.

Therefore, role permissions below are intended/planned permissions unless an endpoint is simply marked authenticated.

## Important Registration Rule

Public registration must create a `CUSTOMER` user by default. This is implemented in `AuthServiceImpl` through `DEFAULT_REGISTER_ROLE = "CUSTOMER"`.

Employees such as `ADMIN`, `SERVICE_ADVISOR`, and `MECHANIC` should be created by an `ADMIN`, not through public registration. This is planned because no admin user-management API exists yet.

## Role Matrix

| Role | Real-world person | Should be able to do | Should not be allowed to do | Pages/modules | Backend endpoints |
| --- | --- | --- | --- | --- | --- |
| `ADMIN` | Owner, manager, system administrator | Manage users, roles, employees, mechanics, service catalog, inventory, all customers/vehicles/work orders, dashboard | Use public registration to create employee accounts; bypass audit for critical changes | Dashboard, Admin/users, Mechanics, Customers, Vehicles, Appointments, Work orders, Inventory, Settings | Planned full access; currently any authenticated endpoint |
| `SERVICE_ADVISOR` | Receptionist, service advisor, service manager | Create customers/vehicles, schedule appointments, open work orders, assign mechanics, update customer-facing statuses, view inventory | Manage admins, change global security, delete audit/history records | Dashboard, Customers, Vehicles, Appointments, Work orders, Mechanics, Inventory read | Planned access to customer/vehicle/appointment/work-order APIs |
| `MECHANIC` | Technician repairing vehicles | View assigned work orders, update diagnosis/notes/status, record service tasks and parts used | Manage users, see unrelated customer data beyond assigned work, change prices/admin settings | Mechanic work queue, Work order detail, Parts lookup | Planned assigned-work endpoints; currently `/api/mechanics` is authenticated but not role-limited |
| `CUSTOMER` | Vehicle owner | Register/login, view own profile, vehicles, appointments, service history, request appointments, receive notifications | View other customers, assign mechanics, manage inventory, create employees | Customer portal, My vehicles, My appointments, Service history, Notifications | Planned customer-scoped endpoints; current CRUD endpoints are authenticated but not owner-scoped |

## Recommended Endpoint Policy

| Module | Recommended roles |
| --- | --- |
| Auth register/login/refresh | Public |
| Auth me/logout | Any authenticated user |
| Users/admin | `ADMIN` |
| Customers CRUD | `ADMIN`, `SERVICE_ADVISOR`; `CUSTOMER` read/update own profile only |
| Vehicles CRUD | `ADMIN`, `SERVICE_ADVISOR`; `CUSTOMER` own vehicles only |
| Appointments | `ADMIN`, `SERVICE_ADVISOR`; `CUSTOMER` own appointment requests; `MECHANIC` read assigned |
| Work orders | `ADMIN`, `SERVICE_ADVISOR`; `MECHANIC` assigned only; `CUSTOMER` read own completed/visible history |
| Inventory parts | `ADMIN`, `SERVICE_ADVISOR`; `MECHANIC` read and consume parts through work order |
| Inventory movements | `ADMIN`, `SERVICE_ADVISOR`; `MECHANIC` through assigned work order only |
| Notifications | Current user only, plus admin support access if needed |
| Dashboard | `ADMIN`, `SERVICE_ADVISOR`; mechanic/customer dashboards scoped to role |

## Current Gaps

- No user-management controller for admin-created employees.
- No role-specific method security.
- No owner checks for customer-specific data.
- No customer profile creation during registration.
- No frontend route guards.
- No CSRF endpoint despite whitelist.

