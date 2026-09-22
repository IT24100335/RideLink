# RideLink Microservices Architecture Documentation

## 1. System Overview
RideLink is a backend microservices solution engineered for a high-concurrency ride-sharing platform. The architecture separates core business functions into four cohesive, loosely coupled microservices, each strictly owning its own persistence boundary on Microsoft SQL Server.

## 2. High-Level Architecture Diagram

```
                                  ┌────────────────────────┐
                                  │ Postman / Swagger UI   │
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
```

## 3. Database Isolation & Persistence Boundaries
Under the database-per-service pattern, direct cross-service queries and joins are prohibited.
- `account_db`: Owns user credentials, profiles, roles, BCrypt passwords, and account status.
- `driver_db`: Owns driver operational profiles, vehicle specifications, availability states, and simulated GPS coordinates.
- `ride_db`: Owns ride bookings, lifecycle statuses, customer pickup/destination details, and interservice state transitions.
- `fare_db`: Owns calculation pricing formulas, payment transaction records, and formal itemized receipts.

## 4. Interservice Communication Justification
The platform employs **Synchronous REST** communication for:
1. **Ride Service → Fare Service (`POST /api/fares/estimate`)**: When a passenger creates a ride, immediate pricing feedback is mandatory. Synchronous REST provides instantaneous confirmation with sub-100ms response times.
2. **Ride Service → Driver Service (`GET /api/drivers/available`)**: When dispatching a driver, the dispatcher requires the immediate real-time availability of active drivers.
3. **Ride Service → Fare Service (`POST /api/fares/final`)**: Final ride completion triggers instant ledger settlement and receipt readiness.

### Comparison with Asynchronous Messaging (e.g. RabbitMQ / Kafka)
- **Asynchronous Messaging**: Excellent for decoupled event notifications (e.g. sending SMS notifications or analytics logging) where immediate response is not required.
- **Synchronous REST**: Chosen here because ride request creation and driver matching require real-time validation and immediate state confirmation to the calling client.

## 5. Microservices vs. Monolithic Architecture
| Characteristic | Microservices (RideLink) | Monolithic Architecture |
| :--- | :--- | :--- |
| **Data Boundary** | Strict schema isolation per business service | Shared monolithic database with tight table coupling |
| **Fault Isolation** | High: A failure in fare calculation does not bring down account authentication | Low: A single memory leak or crash impacts the entire platform |
| **Independent Deployability**| Each service is updated, tested, and scaled independently | Requires complete re-testing and redeployment of the whole system |
| **Team Autonomy** | Distinct service ownership across team members | Frequent merge conflicts and shared code interference |
