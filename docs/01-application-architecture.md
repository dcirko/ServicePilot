# Application Architecture

## High-level Architecture

ServicePilot follows the intended flow:

`Angular frontend -> REST API -> Spring Boot controllers -> Services -> Repositories -> PostgreSQL database`

The backend is the main implemented layer. The frontend currently exists as an Angular scaffold, so frontend-to-backend integration is planned rather than implemented.

```mermaid
flowchart LR
    Browser["Browser"]
    Angular["Angular frontend\nfrontend/src"]
    REST["REST API\n/api/*"]
    Controllers["Spring Boot controllers\ncontrollers package"]
    Services["Service interfaces + implementations\nservices package"]
    Repos["Spring Data JPA repositories\nrepos package"]
    Entities["JPA entities\nentities package"]
    DB["PostgreSQL\nservicepilot database"]
    Security["Spring Security\nJWT + HttpOnly cookies"]

    Browser --> Angular
    Angular --> REST
    REST --> Security
    Security --> Controllers
    Controllers --> Services
    Services --> Repos
    Repos --> Entities
    Entities --> DB
```

## Backend Architecture

Backend root package: `hr.domagoj.servicepilot`.

| Layer | Location | Current role |
| --- | --- | --- |
| Application entry | `BackendApplication.java` | Starts Spring Boot |
| Controllers | `controllers` | Expose REST endpoints under `/api/...` |
| DTOs | `DTOs` | Public request/response records |
| Services | `services/interfaces`, `services/implementations` | Business operations and DTO mapping |
| Repositories | `repos` | Spring Data JPA persistence |
| Entities | `entities` | JPA database model |
| Enums | `enums` | Domain state values |
| Security | `config`, `security` | Auth filter chain, JWT creation/validation, cookies |
| Exceptions | `exceptions` | API error model and handlers |
| Seeders | `seeders` | Startup demo data through `ApplicationRunner` |

The controllers depend directly on implementation classes such as `CustomerServiceImpl`, not the service interfaces. This is implemented and compiles as code structure, but using interfaces in controllers is a planned cleanup if the project wants stricter layering.

## Frontend Architecture

Frontend root: `frontend`.

Current files:

- `frontend/package.json`: Angular 21, TypeScript, Tailwind/PostCSS dependencies.
- `frontend/src/main.ts`: bootstraps `App`.
- `frontend/src/app/app.config.ts`: provides router with `routes`.
- `frontend/src/app/app.routes.ts`: `routes` is currently an empty array.
- `frontend/src/app/app.ts`: root standalone component.
- `frontend/src/app/app.html`: default Angular generated placeholder.
- `frontend/src/styles.css`: default global style placeholder.

Current frontend status is partial scaffold. No pages, API services, guards, forms, interceptors, or state management were found.

## Database Architecture

Implemented database approach:

- PostgreSQL is configured in `docker-compose.yml` as service `db`.
- Backend uses `spring.datasource.url=jdbc:postgresql://localhost:5432/servicepilot`.
- JPA entities map to tables through annotations.
- `spring.jpa.hibernate.ddl-auto=update` is enabled.
- Flyway dependencies exist in `backend/pom.xml`, but `spring.flyway.enabled=false` in `application.properties`.
- No `backend/src/main/resources/db/migration` directory or migration files were found.
- `backend/src/main/resources/db/ServicePilotDiagram.png` exists, but it is a diagram asset, not an executable migration.

Important assumption: the current schema is generated/updated by Hibernate in development. Versioned schema management is planned.

## Security/Auth Architecture

Implemented security files:

- `SecurityConfig`: Spring Security filter chain, CORS, CSRF token repository, stateless sessions, authenticated default policy.
- `PasswordConfig`: BCrypt `PasswordEncoder`.
- `JwtService`: custom HS256 JWT implementation using `SecurityProperties`.
- `JwtAuthenticationFilter`: reads access token from cookie and authenticates requests.
- `CookieService`: writes HttpOnly access and refresh token cookies.
- `RefreshTokenService`: creates, hashes, rotates, and revokes refresh tokens stored in the database.
- `CustomUserDetailsService` and `CustomUserPrincipal`: load users and expose `ROLE_...` authorities.

Current authorization behavior:

- Public endpoints: `/api/auth/register`, `/api/auth/login`, `/api/auth/refresh`, `/api/auth/csrf`.
- All other requests require authentication.
- `@EnableMethodSecurity` is present, but no controller/service `@PreAuthorize` rules were found.
- `/api/auth/csrf` is permitted in security, but no matching controller endpoint was found. Marked TODO.

## Layer Communication

```mermaid
sequenceDiagram
    participant UI as Angular UI
    participant API as REST endpoint
    participant Sec as JwtAuthenticationFilter
    participant Ctrl as Controller
    participant Svc as Service
    participant Repo as Repository
    participant DB as PostgreSQL

    UI->>API: HTTP request to /api/*
    API->>Sec: Security filter chain
    Sec->>Sec: Read SP_ACCESS_TOKEN cookie
    Sec->>Ctrl: Authenticated request
    Ctrl->>Svc: Call service method with DTO/id
    Svc->>Repo: Query/save entity
    Repo->>DB: SQL via JPA/Hibernate
    DB-->>Repo: Rows/entities
    Repo-->>Svc: Entity result
    Svc-->>Ctrl: DTO result
    Ctrl-->>UI: JSON response
```

## Configuration Files

| File | Purpose | Status |
| --- | --- | --- |
| `backend/pom.xml` | Spring Boot 3.3.5, Java 21, JPA, Security, Web, Validation, PostgreSQL, Flyway, Quartz | Implemented |
| `backend/src/main/resources/application.properties` | Server, database, JPA, Flyway disabled, JWT/cookie/CORS settings | Implemented |
| `docker-compose.yml` | Local PostgreSQL 16 service | Implemented |
| `frontend/package.json` | Angular 21 project scripts and dependencies | Implemented |
| `frontend/angular.json` | Angular build/serve/test configuration | Implemented |

