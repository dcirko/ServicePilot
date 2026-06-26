# Feature Map

| Feature | Status | Priority | Backend work needed | Frontend work needed | Database work needed | Notes and risks |
| --- | --- | --- | --- | --- | --- | --- |
| Public registration | Implemented backend | MVP | Create linked `Customer` profile during/after registration | Registration form | Ensure `customers.user_id` consistency | Currently creates only `User` with `CUSTOMER` role |
| Login/logout/refresh | Implemented backend | MVP | Add tests; resolve CSRF endpoint TODO | Login page, auth service, route guards | Refresh-token cleanup/index review | Cookie auth exists |
| Current user session | Implemented backend | MVP | None beyond tests/roles | Header/session state | None | `GET /api/auth/me` exists |
| Role model | Implemented data, partial enforcement | MVP | Add `@PreAuthorize` policies | Guard routes by role | Seed roles already implemented | Current endpoints only require authentication |
| Admin user management | Planned | MVP | `UserController`, employee create/update/deactivate | Admin users page | Possibly audit fields | Required because employees must not use public registration |
| Customers CRUD | Partial | MVP | Fix create/update relation to `User`; validation | Customers list/detail/form | Index email/user_id | Current `CustomerDTO` lacks `userId` |
| Vehicles CRUD | Implemented backend | MVP | Validation and customer ownership checks | Vehicles list/detail/form | Unique/index VIN or plate decision | CRUD exists |
| Appointments list/read/delete | Implemented backend | MVP | Add query filters | Calendar/list/detail | Index date/status | Create/update are not implemented |
| Appointments create/update | Partial | MVP | Implement service methods, validation, status rules | Appointment form/calendar | FK indexes | Currently returns `null` |
| Appointment to work order | Planned | Important | Dedicated conversion service/endpoint | Conversion action in appointment detail | Unique appointment-work-order relation already modeled | Current work-order create can accept `appointmentId` but does not own the workflow |
| Work order CRUD | Partial | MVP | Add validation and status transition rules | Work orders list/detail/form | Index status/mechanic/date | Main CRUD exists |
| Work order lifecycle | Planned/partial | MVP | Transition endpoints, timestamps, assignment logic | Status controls and mechanic queue | Possibly history/audit table | Enum exists, workflow rules missing |
| Work order services/tasks | Planned API, implemented entity | Important | Add service/controller methods | Task list/editor | FK indexes | Entity/repository exist |
| Work order parts | Planned API, implemented entity | Important | Add part usage endpoints and stock transaction | Used parts editor | FK indexes | Must integrate with inventory movements |
| Parts CRUD | Implemented backend | MVP | Add delete/archive if desired; validation | Inventory parts list/form | Unique/index `part_number` | Basic CRUD exists |
| Inventory movements | Planned API, implemented entity | Important | Movement endpoints and stock adjustment service | Stock movement history | Movement indexes | `toMovementDTO` exists but no public service method |
| Low stock alerts | Planned | Important | Query parts below threshold, notification job | Dashboard/alerts | Index stock/threshold if needed | Quartz dependency exists but no scheduled jobs found |
| Notifications list/delete | Partial | Important | Owner checks, mark-as-read endpoint | Notifications panel | Index `user_id`, `read`, `created_at` | Mark-as-read is commented out |
| Dashboard | Planned | Important | Aggregation endpoints | Dashboard page/cards/tables | Read-optimized indexes | Mentioned in README only |
| Documents/invoices | Planned | Advanced | Entities, generation service, endpoints | Document/invoice views | Invoice/document tables | No code found |
| Customer portal | Planned | Advanced | Customer-scoped APIs | Portal pages | Ownership indexes | Needs owner authorization |
| Future AI assistant | Planned later | Later | Define scope after core workflows | Assistant panel | Audit/knowledge data if needed | No current implementation |

## Biggest Risks

- Appointment create/update endpoints exist but return `null`.
- Customer API create does not satisfy the required `Customer.user` relationship.
- Authorization is authentication-only today.
- Work order line items and inventory movements are modeled but not operational.
- Flyway is listed as a dependency and mentioned in README, but migrations are disabled and absent.
- Frontend is not a ServicePilot UI yet.

