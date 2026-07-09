# ServicePilot Tasks

## TODO

### Auth And Security

- [x] Implement backend logout flow.
- [x] Make `/api/auth/logout` robust when the access token is expired by permitting logout and skipping it in `JwtAuthenticationFilter`.
- [x] Implement `/api/auth/csrf`.
- [x] Add frontend auth interceptor for `/api` credentials, 401 refresh, and one retry.
- [x] Display current logged-in user in the topbar.
- [x] Fix topbar current-user rendering by storing async user state in a signal.
- [ ] Add route guards for authenticated routes.
- [ ] Add role-based route guards for admin, service advisor, mechanic, and customer areas.
- [ ] Add backend role enforcement with `@PreAuthorize` or equivalent endpoint policies.
- [ ] Add a shared frontend session/current-user service so topbar, guards, and future pages use one source of truth.
- [ ] Decide whether `AuthService.me()` should fetch CSRF first or call `/me` directly after the XSRF cookie is initialized.
- [ ] Implement forgot-password flow.

### Frontend

- [x] Add login page and reactive login form.
- [x] Add register page and reactive registration form.
- [x] Add app shell with sidebar, topbar, footer, and dashboard route.
- [ ] Add admin sidebar and service advisor sidebar variants.
- [ ] Add mechanic and customer navigation variants.
- [ ] Add API services for customers, vehicles, appointments, work orders, inventory, mechanics, notifications, and dashboard.
- [ ] Add shared loading/error patterns for API-backed pages.

### Backend Domain Work

- [x] Public registration creates a linked `Customer` profile for the new `CUSTOMER` user.
- [ ] Fix staff-created customer flow: `CustomerServiceImpl.createCustomer` still does not attach the required `Customer.user` relation.
- [ ] Implement `AppointmentServiceImpl.createAppointment`.
- [ ] Implement `AppointmentServiceImpl.updateAppointment`.
- [ ] Add appointment validation for date ranges, vehicle/customer consistency, and mechanic availability.
- [ ] Add owner checks for customer, vehicle, appointment, notification, and customer-visible work-order data.
- [ ] Add user-management API for admin-created employees.
- [ ] Add work-order status transition endpoints and validation.
- [ ] Add work-order service-task and used-part endpoints.
- [ ] Add inventory movement endpoints and transactional stock adjustment.
- [ ] Add notification mark-as-read endpoint.

### Database And Operations

- [x] Enable Flyway and add initial customer/user repair migrations.
- [ ] Decide whether to keep `spring.jpa.hibernate.ddl-auto=update` during development or move fully to Flyway-managed schema changes.
- [ ] Add indexes for the next workflow-heavy queries: appointments by date/status, vehicles by customer, work orders by status/mechanic, notifications by user/read state.
- [ ] Add refresh-token cleanup for expired/revoked tokens.

## Known Issues

### Open

- [ ] Staff-created customers can fail or produce inconsistent data because `CustomerDTO` has no `userId` and `CustomerServiceImpl.createCustomer` does not set `Customer.user`.
- [ ] Appointment create/update endpoints exist but currently return `null`.
- [ ] Backend authorization is still authentication-only for most endpoints; roles exist but are not enforced with method or route policies.
- [ ] Frontend routes are not guarded yet, so protected screens can be opened directly until the backend rejects their API calls.
- [ ] Generated frontend unit test still references the old starter template and is no longer aligned with the shell UI.
- [ ] Flyway is enabled, but Hibernate `ddl-auto=update` is still enabled too; this can hide missing migrations.

### Resolved

- [x] Topbar current user did not render immediately after `/me` resolved. Fixed by storing current user in a signal.
- [x] Logout could fail when the access token was expired but the refresh cookie still existed. Fixed by making `/api/auth/logout` public with CSRF protection and skipping it in `JwtAuthenticationFilter`.
- [x] `/api/auth/csrf` was documented as whitelisted but missing. It is implemented in `AuthController`.
- [x] Frontend auth calls needed repeated `withCredentials`. The auth interceptor now applies credentials to `/api` requests.
