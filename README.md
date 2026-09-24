# RideLink – Backend Microservices for a Ride-Sharing Platform

[![CI Pipeline](https://github.com/ridelink/ridelink-backend/actions/workflows/ci.yml/badge.svg)](https://github.com/ridelink/ridelink-backend/actions)
**Module**: IT3130 – Application Development  
**Assessment**: Group Assignment (30%)  

---

## 1. Project Overview

**RideLink** is an enterprise-grade backend microservices platform engineered for an on-demand ride-sharing service. The platform is architectured into four cohesive, independently deployable Spring Boot microservices, each adhering to the strict **Database-Per-Service** pattern backed by **Microsoft SQL Server (MS SQL)**.

---

## 2. Microservice Ownership & Allocation

| Member | Microservice | Primary Responsibility | Port | Persistence Boundary |
| :--- | :--- | :--- | :--- | :--- |
| **Member 1** | `account-service` | Passenger/Driver accounts, BCrypt password hashing, JWT token issuance & validation, RBAC (`PASSENGER`, `DRIVER`, `ADMIN`), account status management. | `8081` | `account_db` |
| **Member 2** | `driver-vehicle-service` | Driver operational profile, vehicle registration, availability toggle (`AVAILABLE`, `BUSY`, `UNAVAILABLE`), location tracking, available drivers query. | `8082` | `driver_db` |
| **Member 3** | `ride-service` | Ride booking, lifecycle state machine (`REQUESTED` → `ASSIGNED` → `ACCEPTED` → `IN_PROGRESS` → `COMPLETED` / `CANCELLED`), synchronous interservice orchestration. | `8083` | `ride_db` |
| **Member 4** | `fare-payment-service` | Documented pricing engine ($\text{Rs. } 150 + \text{dist} \times \text{Rs. } 100/\text{km}$), simulated payments, transaction ledger, and itemized receipt generation. | `8084` | `fare_db` |

---

## 3. Technology Stack

- **Language / Runtime**: Java 22
- **Framework**: Spring Boot 3.5.6
- **Data Persistence**: Spring Data JPA & Hibernate
- **Database Engine**: Microsoft SQL Server (MS SQL) with `mssql-jdbc`
- **Security**: Spring Security 6.x & JJWT (JSON Web Token) HMAC-SHA256
- **API Documentation**: OpenAPI 3.0 / Swagger UI (`springdoc-openapi`)
- **Testing**: JUnit 5, Mockito, AssertJ, H2 (In-Memory for test isolation)
- **API Client**: Postman Collection (v2.1) & Environment
- **Version Control & CI**: Git, GitHub Flow, and GitHub Actions

---

## 4. System Architecture

                                  ┌────────────────────────┐
                                  │  Postman / Swagger UI  │
                                  └───────────┬────────────┘
                                              │ HTTP / JSON
           ┌─────────────────────┬────────────┴────────────┬─────────────────────┐
           │                     │                         │                     │
           ▼                     ▼                         ▼                     ▼
┌────────────────────┐ ┌────────────────────┐   ┌────────────────────┐ ┌────────────────────┐
│   Account Service  │ │Driver & Vehicle Svc│   │Ride Management Svc │ │Fare & Payment Svc  │
│   Port: 8081       │ │Port: 8082          │   │Port: 8083          │ │Port: 8084          │
└──────────┬─────────┘ └─────────┬──────────┘   └─────────┬──────────┘ └─────────┬──────────┘
           │                     │                        │ REST                 │
           │                     │                        ├─────────────────────►│ (POST /estimate)
           │                     │◄───────────────────────┘ (GET /available)     │ (POST /final)
           ▼                     ▼                        ▼                      ▼
┌────────────────────┐ ┌────────────────────┐   ┌────────────────────┐ ┌────────────────────┐
│   account_db       │ │   driver_db        │   │     ride_db        │ │     fare_db        │
│ (MS SQL Server)    │ │ (MS SQL Server)    │   │  (MS SQL Server)   │ │ (MS SQL Server)    │
└────────────────────┘ └────────────────────┘   └────────────────────┘ └────────────────────┘

---

## 5. Prerequisites & Database Setup

### 5.1 Prerequisites

1. **Java Development Kit (JDK)**: JDK 22 installed (`java -version`).
2. **Maven**: The included `mvnw.cmd` wrapper downloads Maven automatically when needed.
3. **Microsoft SQL Server**: Installed locally (Developer/Express) or via Docker on port `1433`.
4. **SQL Server Management Studio (SSMS)** or `sqlcmd`.

### 5.2 Initializing the 4 MS SQL Databases

Open **SSMS**, connect to your SQL Server instance, and run the provided SQL script:

sql
-- Located at: scripts/create-databases.sql
USE master;
GO

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'account_db') CREATE DATABASE [account_db];
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'driver_db') CREATE DATABASE [driver_db];
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'ride_db') CREATE DATABASE [ride_db];
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'fare_db') CREATE DATABASE [fare_db];
GO

---

## 6. Configuration & Environment Variables

No secrets or passwords are hardcoded in the repository. All services support the following environment variables (with safe local defaults):

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `DB_HOST` | `localhost` | MS SQL Server hostname/IP |
| `DB_PORT` | `1433` | MS SQL Server port |
| `DB_USERNAME` | `sa` | Database user account |
| `DB_PASSWORD` | `Password123!` | Database password |
| `JWT_SECRET` | `404E63526655...` | 256-bit cryptographic HMAC key |
| `DRIVER_SERVICE_HOST` | `localhost` | Host for Driver Service |
| `FARE_SERVICE_HOST` | `localhost` | Host for Fare Service |

---

## 7. How to Run the Services

### 7.1 Startup Order

To ensure interservice communication succeeds during testing, start the services in this recommended order:

1. **Account Service** (Port `8081`)
2. **Driver & Vehicle Service** (Port `8082`)
3. **Fare & Payment Service** (Port `8084`)
4. **Ride Management Service** (Port `8083`)

### 7.2 Terminal Startup Commands

Open 4 separate terminal windows or command prompts:

   .\mvnw.cmd -pl account-service spring-boot:run

   .\mvnw.cmd -pl driver-vehicle-service spring-boot:run

   .\mvnw.cmd -pl fare-payment-service spring-boot:run

   .\mvnw.cmd -pl ride-service spring-boot:run

---

## 8. Interactive Swagger UI Documentation URLs

Once started, access the live interactive OpenAPI documentation for each service in your browser:

- **Account Service**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **Driver & Vehicle Service**: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- **Ride Management Service**: [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)
- **Fare & Payment Service**: [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html)

---

## 9. Testing & Postman Instructions

### 9.1 Automated Unit Tests

Run unit test suites across all 4 services simultaneously from the repository root:

   .\mvnw.cmd clean test

*(Or test each service individually: `.\mvnw.cmd -pl account-service test`)*

### 9.2 Postman Test Collection Execution

1. Open **Postman**.
2. Click **Import** and select:
   - `postman/RideLink_API.postman_collection.json`
   - `postman/RideLink_Local.postman_environment.json`
3. Select the active environment: **RideLink Local Environment**.
4. Run requests in sequence across folders:
   - `01 Account Service`: Registers accounts and auto-stores JWT tokens.
   - `02 Driver & Vehicle Service`: Creates operational profile and sets availability to `AVAILABLE`.
   - `03 Fare Service`: Validates the calculation formula ($\text{Rs. } 150 + 5\text{ km} \times 100 = \text{Rs. } 650$).
   - `04 Ride Management Service`: Dispatches driver, executes full lifecycle transitions.
   - `05 Payment & Receipt Service`: Processes payment and retrieves itemized receipt.
   - `06 Negative Tests`: Executes the 4 required failure scenarios.

### 9.3 Negative Test Scenarios Verified

1. **No Available Driver**: Requests driver dispatch when no drivers are available (`404 NOT_FOUND`).
2. **Invalid Ride Status Transition**: Attempting to skip directly from `REQUESTED` to `COMPLETED` (`400 BAD_REQUEST`).
3. **Unauthorized Operation**: Passenger token attempting to alter administrative status (`403 FORBIDDEN`).
4. **Invalid Input Validation**: Submitting a negative distance to fare estimation (`400 BAD_REQUEST`).

---

## 10. Sample Test Credentials

| Account Type | Email | Password | Role | Account ID |
| :--- | :--- | :--- | :--- | :--- |
| **Passenger** | `kamal@example.com` | `Password123` | `PASSENGER` | `1` |
| **Driver** | `saman@example.com` | `Password123` | `DRIVER` | `2` |
| **Admin** | `admin@ridelink.com` | `Admin123!` | `ADMIN` | `3` |

---

## 11. Git Branching & Contribution Workflow

This project adheres to professional collaborative version control:

- `main`: Production release branch. Assessed version is tagged as `v1.0.0-release`.
- `develop`: Shared integration branch for validated feature pull requests.
- `feature/account-service`: Member 1 feature development.
- `feature/driver-service`: Member 2 feature development.
- `feature/ride-service`: Member 3 feature development.
- `feature/fare-payment-service`: Member 4 feature development.
#   R i d e L i n k  
 #   R i d e L i n k  
 