# Frontend Map

## Current Angular Structure

Frontend root: `frontend`.

| File/folder | Current purpose |
| --- | --- |
| `frontend/package.json` | Angular scripts and dependencies |
| `frontend/angular.json` | Angular build, serve, and test configuration |
| `frontend/src/main.ts` | Bootstraps the standalone Angular app |
| `frontend/src/index.html` | Hosts `<app-root>` |
| `frontend/src/styles.css` | Global styles placeholder |
| `frontend/src/app/app.ts` | Root standalone component named `App` |
| `frontend/src/app/app.html` | Default Angular starter template |
| `frontend/src/app/app.css` | Component stylesheet placeholder |
| `frontend/src/app/app.routes.ts` | Empty `Routes` array |
| `frontend/src/app/app.config.ts` | Provides router and browser error listeners |
| `frontend/src/app/app.spec.ts` | Generated root component tests |

## Pages

Status: planned.

No ServicePilot-specific pages were found. The only visible template is the default Angular starter page.

Planned pages:

| Page | Purpose | Priority |
| --- | --- | --- |
| Login | Authenticate users | MVP |
| Register | Public customer registration | MVP |
| Dashboard | Operational summary | Important |
| Customers | Customer list/detail/form | MVP |
| Vehicles | Vehicle list/detail/form | MVP |
| Appointments | Calendar/list/detail/form | MVP |
| Work orders | Work order list/detail/workflow | MVP |
| Inventory | Parts and stock movement management | Important |
| Admin/users | Admin employee management | MVP |
| Mechanics | Mechanic list and availability | Important |
| Customer portal | Customer's own vehicles, appointments, service history | Advanced |
| Notifications | User notifications | Important |

## Components

Status: planned.

Suggested component groups:

| Component group | Examples |
| --- | --- |
| Layout | App shell, sidebar, top bar, user menu |
| Auth | Login form, register form, session status |
| Data tables | Customers table, vehicles table, work orders table, parts table |
| Detail panels | Customer detail, vehicle detail, work-order detail |
| Forms | Customer form, vehicle form, appointment form, part form |
| Workflow controls | Work-order status control, assignment control, part usage form |
| Feedback | Toasts, validation messages, loading/error states |

## Services

Status: planned.

No Angular API services were found. Planned services should mirror backend modules:

- `AuthApiService`
- `CustomersApiService`
- `VehiclesApiService`
- `AppointmentsApiService`
- `WorkOrdersApiService`
- `InventoryApiService`
- `MechanicsApiService`
- `NotificationsApiService`
- `DashboardApiService`

Because backend auth uses HttpOnly cookies, Angular HTTP calls should use credentials and should handle CSRF once the CSRF endpoint is implemented.

## Routing

Current routing:

```ts
export const routes: Routes = [];
```

Planned route map:

| Route | Page | Guard |
| --- | --- | --- |
| `/login` | Login | Public |
| `/register` | Register | Public |
| `/dashboard` | Dashboard | Authenticated |
| `/customers` | Customers | Staff |
| `/customers/:id` | Customer detail | Staff or owner |
| `/vehicles` | Vehicles | Staff |
| `/vehicles/:id` | Vehicle detail | Staff or owner |
| `/appointments` | Appointments | Authenticated scoped |
| `/work-orders` | Work orders | Authenticated scoped |
| `/inventory` | Inventory | Staff |
| `/admin/users` | Users/admin | `ADMIN` |
| `/portal` | Customer portal | `CUSTOMER` |

## Forms

Status: planned.

`@angular/forms` is installed, but no domain forms exist. Reactive forms are expected based on README planning.

Needed forms:

- Login and registration.
- Customer create/update.
- Vehicle create/update.
- Appointment create/update.
- Work-order create/update.
- Work-order service task add/edit.
- Work-order part usage add/edit.
- Part create/update.
- Employee create/update.

## State Management

Status: not implemented.

No state library or custom shared store was found. A conservative first implementation can use:

- Angular services with signals for auth/session state.
- Router guards based on current user roles.
- Component-local state for forms and tables.
- Later adoption of a richer store only if screens become complex.

## Finished Frontend Behavior

When finished, the first screen after login should be the operational dashboard, not a marketing page. Staff users should be able to move quickly between customers, vehicles, appointments, and work orders. Mechanics should land on assigned work. Customers should see only their own vehicles, appointment requests, work-order history, and notifications.

The UI should be dense and practical: tables, filters, detail drawers/pages, clear status indicators, and fast actions for repeated service-desk workflows.

