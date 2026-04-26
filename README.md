# ServicePilot

ServicePilot is a full-stack auto service management platform designed to streamline workshop operations, including customer and vehicle management, appointment scheduling, work order tracking, service history, and parts inventory management.

---

## Project Goal

The goal of ServicePilot is to simulate a real-world internal system used by auto repair workshops and service centers. The platform focuses on backend-heavy business logic, relational data modeling, and full-stack architecture.

---

## Planned Features

* User authentication and role-based access control
* Customer management
* Vehicle management
* Appointment scheduling
* Work order lifecycle tracking
* Vehicle service history
* Parts and inventory management
* Mechanic workload tracking
* Dashboard and analytics
* Notifications and alerts

---

## Roles

* **Admin**
  Manages users, mechanics, services, inventory, and system settings.

* **Receptionist / Manager**
  Creates appointments, manages customers and vehicles, opens work orders, and tracks service progress.

* **Mechanic**
  Views assigned work orders, updates status, adds notes, and records used parts.

---

## Tech Stack

### Backend

* Java 25
* Spring Boot 4
* Spring Security
* Spring Data JPA
* PostgreSQL
* Flyway
* Docker
* Maven

### Frontend

* Angular 21
* TypeScript
* Tailwind v4
* Angular Router
* Reactive Forms


## Database Management

Database schema changes are handled using **Flyway migrations**.

Benefits:

* Versioned database structure
* Reproducible schema changes
* Consistent environments

---

## Development Setup

### Requirements

* Java 25
* Maven
* Docker

---

### Run database

```bash
docker compose up -d
```

---

### Run backend

```bash
mvnw.cmd spring-boot:run
```

---

## Why this project

ServicePilot demonstrates:

* backend architecture
* database design
* real-world business logic
* REST API development
* full-stack integration
* clean project structure

---

## Author

**Domagoj Čirko**
GitHub: https://github.com/dcirko
