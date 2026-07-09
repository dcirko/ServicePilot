# Frontend Map

## Current Angular Structure

Frontend root: `frontend`.

| File/folder | Current purpose |
| --- | --- |
| `frontend/package.json` | Angular scripts and dependencies |
| `frontend/angular.json` | Angular build, serve, test configuration, and `/api` proxy setup |
| `frontend/proxy.conf.json` | Proxies `/api` to the Spring backend on `localhost:8080` during development |
| `frontend/src/main.ts` | Bootstraps the standalone Angular app |
| `frontend/src/index.html` | Hosts `<app-root>` |
| `frontend/src/styles.css` | Global styles |
| `frontend/src/app/app.ts` | Root standalone component that controls shell visibility |
| `frontend/src/app/app.html` | Shows auth pages without shell and app pages with sidebar/topbar/footer shell |
| `frontend/src/app/app.routes.ts` | Routes login, register, home, and dashboard |
| `frontend/src/app/app.config.ts` | Provides router, HTTP client, XSRF configuration, and auth interceptor |
| `frontend/src/app/pages` | Login, register, home, and dashboard pages |
| `frontend/src/app/core/layout` | Sidebar, topbar, and footer components |
| `frontend/src/app/core/services/auth.ts` | Auth API service for CSRF, login, register, logout, and current user |
| `frontend/src/app/core/http` | Auth interceptor, refresh-session helper, and HTTP context flags |
| `frontend/src/app/core/domain/auth` | Auth request/response TypeScript types |
| `frontend/src/app/app.spec.ts` | Generated test; currently needs updating for the custom app shell |

## Pages

Status: partial.

Implemented pages:

| Page | Purpose | Status |
| --- | --- | --- |
| Login | Authenticate users | Implemented |
| Register | Public customer registration | Implemented |
| Home | Existing page route | Implemented shell/page, final purpose unclear |
| Dashboard | Initial authenticated shell destination | Implemented placeholder |

Planned pages:

| Page | Purpose | Priority |
| --- | --- | --- |
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

Implemented component groups:

| Component group | Examples |
| --- | --- |
| Layout | Sidebar, topbar, footer, app shell |
| Auth | Login form, registration form |
| Session display | Topbar current-user name, initials, role, logout action |

Planned component groups:

| Component group | Examples |
| --- | --- |
| Data tables | Customers table, vehicles table, work orders table, parts table |
| Detail panels | Customer detail, vehicle detail, work-order detail |
| Forms | Customer form, vehicle form, appointment form, part form |
| Workflow controls | Work-order status control, assignment control, part usage form |
| Feedback | Toasts, validation messages, loading/error states |

## Services And HTTP

Implemented:

- `AuthService` for CSRF, login, register, logout, and current user.
- Angular XSRF configuration for `XSRF-TOKEN` / `X-XSRF-TOKEN`.
- Auth interceptor for `/api` requests:
  - adds `withCredentials`;
  - refreshes the session on eligible `401` responses;
  - retries the original request once;
  - redirects to `/login` if refresh fails.

Planned services should mirror backend modules:

- `CustomersApiService`
- `VehiclesApiService`
- `AppointmentsApiService`
- `WorkOrdersApiService`
- `InventoryApiService`
- `MechanicsApiService`
- `NotificationsApiService`
- `DashboardApiService`

Because backend auth uses HttpOnly cookies, Angular should not store access tokens in local storage. API calls should rely on cookies, XSRF protection, and the shared interceptor.

## Routing

Current routing:

```ts
export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'home', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'dashboard', component: Dashboard },
];
```

Planned route map:

| Route | Page | Guard |
| --- | --- | --- |
| `/login` | Login | Public, redirect away when already authenticated |
| `/register` | Register | Public, redirect away when already authenticated |
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

Implemented:

- Login form.
- Registration form.

Needed forms:

- Customer create/update.
- Vehicle create/update.
- Appointment create/update.
- Work-order create/update.
- Work-order service task add/edit.
- Work-order part usage add/edit.
- Part create/update.
- Employee create/update.

## State Management

Status: partial.

Implemented:

- App shell visibility uses signals in the root component.
- Sidebar collapse state uses signals.
- Topbar current user uses a signal so `/me` responses render immediately.

Needed next:

- Shared session/current-user service for topbar, guards, and future role-aware navigation.
- Router guards based on authenticated state and current user roles.
- Component-local state for forms and tables.
- Later adoption of a richer store only if screens become complex.

## Finished Frontend Behavior

When finished, the first screen after login should be the operational dashboard, not a marketing page. Staff users should be able to move quickly between customers, vehicles, appointments, and work orders. Mechanics should land on assigned work. Customers should see only their own vehicles, appointment requests, work-order history, and notifications.

The UI should be dense and practical: tables, filters, detail drawers/pages, clear status indicators, and fast actions for repeated service-desk workflows.
