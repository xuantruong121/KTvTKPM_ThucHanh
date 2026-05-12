Thành viên nhóm:

1. Nguyễn Hoàng Sơn
2. Nguyễn Phú Triệu
3. Huỳnh Công Ý
4. Nguyễn Văn Khải Tiến
5. Nguyễn Đỗ Xuân Trường
# Travel Booking System - Orchestration-Driven SOA

He thong dat tour du lich theo kien truc Orchestration-Driven SOA.

## Nguyen tac kien truc

- Frontend chi goi Orchestrator Service.
- Cac service nghiep vu khong goi truc tiep lan nhau.
- Orchestrator la trung tam dieu phoi flow dat tour.
- Tat ca giao tiep backend la REST API.

## Thanh phan

| Thanh phan | Vai tro | LAN |
| --- | --- | --- |
| Frontend Mobile | Login, xem tour, dat tour | `192.168.137.210:3000` |
| Orchestrator Service | Dieu phoi dat tour | `192.168.1.10:8080` |
| User Service | Dang nhap, lay user | `192.168.1.11:8081` |
| Tour Service | Danh sach tour, chi tiet tour | `192.168.1.12:8082` |
| Booking + Payment Service | Tao booking, thanh toan random success/fail | `192.168.1.13:8083` |

## Cau truc thu muc

```text
Tuan9/
  frontend-mobile/
  orchestrator-service/
  user-service/
  tour-service/
  booking-service/
```

## Flow dat tour

1. Frontend goi `POST /book-tour` cua Orchestrator.
2. Orchestrator goi `GET /users/{id}` de validate user.
3. Orchestrator goi `GET /tours/{id}` de lay tour.
4. Orchestrator goi `POST /bookings` de tao booking.
5. Orchestrator goi `POST /payments` cung Booking + Payment Service de thanh toan.
6. Orchestrator tra ket qua ve Frontend.

## Chay tung service

Moi may copy dung thu muc cua minh, sau do:

```bash
npm install
npm run dev
```

Nguoi 5 chi can chay thu muc `booking-service`; service nay da gom ca Booking va Payment.

May chay `frontend-mobile` co IP `192.168.137.210` nen co the dung lenh sau de Expo co dinh dung card mang:

```bash
npm run start:lan-ip
```

Co the doi IP/port bang file `.env` hoac bien moi truong. Vi du tren may Orchestrator:

```bash
PORT=8080
HOST=0.0.0.0
USER_SERVICE_URL=http://192.168.1.11:8081
TOUR_SERVICE_URL=http://192.168.1.12:8082
BOOKING_SERVICE_URL=http://192.168.1.13:8083
BOOKING_PAYMENT_SERVICE_URL=http://192.168.1.13:8083
```

## Tai khoan mau

| Email | Password | User ID |
| --- | --- | --- |
| `an@example.com` | `123456` | `u1` |
| `binh@example.com` | `123456` | `u2` |

## API chinh

### Orchestrator

- `GET /health`
- `POST /login`
- `GET /tours`
- `GET /tours/:id`
- `POST /book-tour`

Body:

```json
{
  "userId": "u1",
  "tourId": "t1",
  "quantity": 2
}
```

### User Service

- `POST /login`
- `GET /users/:id`

### Tour Service

- `GET /tours`
- `GET /tours/:id`

### Booking + Payment Service

- `POST /bookings`
- `POST /payments`
