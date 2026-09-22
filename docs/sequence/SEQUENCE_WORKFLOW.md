# RideLink Sequence Workflows & Failure Handling

## 1. End-to-End Successful Ride Lifecycle Sequence

```mermaid
sequenceDiagram
    autonumber
    actor Passenger
    actor Driver
    participant AccountSvc as Account Service (:8081)
    participant DriverSvc as Driver Service (:8082)
    participant RideSvc as Ride Service (:8083)
    participant FareSvc as Fare Service (:8084)

    Note over Passenger,AccountSvc: 1. Registration & Authentication
    Passenger->>AccountSvc: POST /api/accounts/register (role=PASSENGER)
    AccountSvc-->>Passenger: 201 Created (ID=1)
    Driver->>AccountSvc: POST /api/accounts/register (role=DRIVER)
    AccountSvc-->>Driver: 201 Created (ID=2)
    Passenger->>AccountSvc: POST /api/accounts/login
    AccountSvc-->>Passenger: 200 OK (JWT Token)

    Note over Driver,DriverSvc: 2. Driver Onboarding & Availability
    Driver->>DriverSvc: POST /api/drivers (accountId=2, license="B1234567")
    Driver->>DriverSvc: POST /api/vehicles (driverId=1, vehicleNumber="CAB-1234")
    Driver->>DriverSvc: PATCH /api/drivers/1/availability (AVAILABLE)

    Note over Passenger,FareSvc: 3. Booking & Interservice Interaction
    Passenger->>RideSvc: POST /api/rides (pickup, destination, distanceKm=5)
    RideSvc->>FareSvc: [Interservice] POST /api/fares/estimate
    FareSvc-->>RideSvc: Estimated Fare = Rs. 650.00
    RideSvc-->>Passenger: 201 Created (Ride ID=1, Status: REQUESTED)

    Passenger->>RideSvc: POST /api/rides/1/assign
    RideSvc->>DriverSvc: [Interservice] GET /api/drivers/available
    DriverSvc-->>RideSvc: Returns [Driver ID=1, Vehicle="CAB-1234"]
    RideSvc-->>Passenger: 200 OK (Driver Assigned, Status: ASSIGNED)

    Note over Driver,RideSvc: 4. Ride Execution
    Driver->>RideSvc: POST /api/rides/1/accept
    RideSvc-->>Driver: 200 OK (Status: ACCEPTED)
    Driver->>RideSvc: POST /api/rides/1/start
    RideSvc-->>Driver: 200 OK (Status: IN_PROGRESS)
    Driver->>RideSvc: POST /api/rides/1/complete
    RideSvc->>FareSvc: [Interservice] POST /api/fares/final
    FareSvc-->>RideSvc: Final Fare = Rs. 650.00
    RideSvc-->>Driver: 200 OK (Status: COMPLETED)

    Note over Passenger,FareSvc: 5. Payment & Itemized Receipt
    Passenger->>FareSvc: POST /api/payments (rideId=1, amount=650, method=CARD)
    FareSvc-->>Passenger: 201 Created (Status: SUCCESS, Ref: TXN-XXXX)
    Passenger->>FareSvc: GET /api/payments/1/receipt
    FareSvc-->>Passenger: 200 OK (Receipt: RCP-20260922-0001)
```

---

## 2. Negative Scenario Sequences

### Negative Scenario 1: No Available Driver
When a passenger requests driver assignment, but all registered drivers are either `BUSY` or `UNAVAILABLE`:
```mermaid
sequenceDiagram
    autonumber
    actor Passenger
    participant RideSvc as Ride Service (:8083)
    participant DriverSvc as Driver Service (:8082)

    Passenger->>RideSvc: POST /api/rides/1/assign
    RideSvc->>DriverSvc: GET /api/drivers/available
    DriverSvc-->>RideSvc: [] (Empty List)
    RideSvc-->>Passenger: 404 NOT FOUND (NO_AVAILABLE_DRIVER)
    Note over Passenger,RideSvc: Error: "No active and available drivers found in the selected service area."
```

### Negative Scenario 2: Invalid Ride State Transition
When a client attempts an illegal state transition (e.g. attempting to jump from `REQUESTED` directly to `COMPLETED` without driver acceptance or trip progress):
```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant RideSvc as Ride Service (:8083)

    Note over Client,RideSvc: Ride is currently in REQUESTED state
    Client->>RideSvc: POST /api/rides/1/complete
    RideSvc-->>Client: 400 BAD REQUEST (INVALID_RIDE_STATUS)
    Note over Client,RideSvc: Error: "Ride must be IN_PROGRESS before it can be marked as COMPLETED."
```
