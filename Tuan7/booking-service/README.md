# Booking Service

The core business logic service for seat reservations. It uses an asynchronous flow to coordinate with payment.

## Features
- **Booking Flow**: Creates a "PENDING" booking and notifies the Payment service via Kafka.
- **Kafka Listener**: Waits for `PAYMENT_COMPLETED` or `BOOKING_FAILED` to update the finalized status.
- **Status Types**: `PENDING`, `CONFIRMED`, `FAILED`.

## Connectivity
- **Port**: 8083
- **Kafka Topic**: `BOOKING_CREATED` (Publisher), `PAYMENT_COMPLETED` (Subscriber).

## Key Endpoints
- `POST /api/bookings`: Start a new booking.
- `GET /api/bookings`: Fetch user booking history.

## Running Locally
```bash
./mvnw spring-boot:run -pl booking-service
```
