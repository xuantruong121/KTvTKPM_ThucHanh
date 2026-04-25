# Hướng dẫn demo theo nhóm 5 người (Mini Food Ordering System)

## 0) Mục tiêu demo (bắt buộc)
Chạy và trình diễn end-to-end theo đúng kịch bản:
1. User đăng ký + login (JWT)
2. Xem danh sách món
3. Thêm vào giỏ → tạo order
4. Thanh toán (COD/Banking)
5. Nhận thông báo (log/notification)

## 1) Sơ đồ & port chuẩn
- `api-gateway`: `8080`
- `user-service`: `8081`
- `food-service`: `8082`
- `order-service`: `8083`
- `payment-service`: `8084`
- `frontend` (dev): `3000`

Quy tắc LAN: **tất cả service bind `0.0.0.0`** và **Frontend chỉ gọi Gateway**.

## 2) Phân vai & checklist trước demo
### IP mẫu (thay bằng IP thật của nhóm)
- **Máy Người 1 (Frontend)**: `FRONTEND_IP=192.168.1.50`
- **Máy Người 2 (User Service)**: `USER_IP=192.168.1.10`
- **Máy Người 3 (Food Service)**: `FOOD_IP=192.168.1.11`
- **Máy Người 4 (Order Service)**: `ORDER_IP=192.168.1.12`
- **Máy Người 5 (Payment + Notification + Gateway)**: `GATEWAY_IP=192.168.1.13`, `PAYMENT_IP=192.168.1.13`

### Biến môi trường bắt buộc (phải giống nhau giữa các máy liên quan)
- **JWT_SECRET**: Gateway và User Service phải dùng cùng một secret.
- **SERVICE_TOKEN**: Payment Service gọi Order Service cập nhật trạng thái phải khớp token.

> Gợi ý: xem file `lan.env.example` (chỉ là ví dụ), demo thật thì set đúng IP.

### “Chỗ cần sửa IP” để đổi mạng nhanh
- **Gateway machine (Người 5)**:
  - Mở file `env/gateway.env` (hoặc `lan.env.example`) và sửa các dòng có ký hiệu **⚠️**:
    - `FRONTEND_ORIGIN=http://<FRONTEND_IP>:3000`
    - `USER_SERVICE_URL=http://<USER_IP>:8081`
    - `FOOD_SERVICE_URL=http://<FOOD_IP>:8082`
    - `ORDER_SERVICE_URL=http://<ORDER_IP>:8083`
    - `PAYMENT_SERVICE_URL=http://<PAYMENT_IP>:8084`
- **Frontend machine (Người 1)**:
  - Mở `env/frontend.env` rồi copy sang `frontend/.env`, hoặc sửa trực tiếp `frontend/.env` (dòng **⚠️**):
    - `VITE_API_BASE_URL=http://<GATEWAY_IP>:8080`
- **Secrets**:
  - `JWT_SECRET` phải **giống nhau** giữa **User Service** và **API Gateway**
  - `SERVICE_TOKEN` phải **giống nhau** giữa **Payment Service** và **Order Service**

## 3) Hướng dẫn demo theo từng thành viên

## Người 1 — Frontend (ReactJS + Axios)
### Nhiệm vụ demo
- Mở UI để thao tác các bước: Login → Foods → Cart → Create Order → Pay → Orders

### Chuẩn bị cấu hình
Tại máy Frontend, trong `frontend/.env` đặt:

```env
VITE_API_BASE_URL=http://<GATEWAY_IP>:8080
```

### Chạy Frontend
```powershell
cd frontend
npm install
npm run dev -- --host 0.0.0.0 --port 3000
```

### Demo trên UI (đúng kịch bản)
1. **Login** bằng tài khoản seed:
   - `user / user123` (USER) hoặc `admin / admin123` (ADMIN)
2. Vào **Món ăn** → bấm **Thêm vào giỏ**
3. Vào **Giỏ hàng** → chỉnh quantity → bấm **Tạo đơn hàng**
4. Copy **Order ID** vừa tạo (hiện trong UI)
5. Vào **Thanh toán** → nhập Order ID → chọn `COD` hoặc `BANKING` → bấm **Thanh toán**
6. Vào **Đơn hàng** → thấy order chuyển `PAID`

### Điểm cần nói khi demo
- Frontend **chỉ gọi** `http://<GATEWAY_IP>:8080` (không gọi trực tiếp service IP:8081..8084).
- JWT được lưu localStorage và Axios tự gắn `Authorization: Bearer <token>`.

---

## Người 2 — User Service (Spring Boot)
### Nhiệm vụ demo
- Chứng minh API Register/Login phát JWT và có seed user/admin.

### Chạy service (máy USER_IP)
```powershell
cd user-service
$env:JWT_SECRET="dev-secret-change-me-dev-secret-change-me"
mvn spring-boot:run
```

### Demo nhanh bằng API (optional, để dự phòng)
Qua gateway:
- `POST http://<GATEWAY_IP>:8080/api/users/register`
- `POST http://<GATEWAY_IP>:8080/api/users/login` → trả `accessToken`

Body mẫu:
```json
{ "username": "u1", "password": "1234", "role": "USER" }
```

### Điểm cần nói khi demo
- `JWT_SECRET` phải trùng với Gateway để gateway verify token.
- Seed sẵn:
  - `admin/admin123` (ADMIN)
  - `user/user123` (USER)

---

## Người 3 — Food Service (Spring Boot)
### Nhiệm vụ demo
- Chứng minh có seed foods, GET list hoạt động, CRUD (ADMIN).

### Chạy service (máy FOOD_IP)
```powershell
cd food-service
mvn spring-boot:run
```

### Demo qua gateway
- Public list:
  - `GET http://<GATEWAY_IP>:8080/api/foods`

CRUD (ADMIN, cần login lấy token admin):
- `POST/PUT/DELETE http://<GATEWAY_IP>:8080/api/foods...` với header:
  - `Authorization: Bearer <adminToken>`

### Điểm cần nói khi demo
- Seed sẵn 3 món để demo không phụ thuộc nhập tay.

---

## Người 4 — Order Service (Spring Boot)
### Nhiệm vụ demo
- Chứng minh khi tạo order có gọi liên-service:
  - validate user (User Service)
  - lấy thông tin món (Food Service)

### Chạy service (máy ORDER_IP)
```powershell
cd order-service
$env:USER_SERVICE_URL="http://<USER_IP>:8081"
$env:FOOD_SERVICE_URL="http://<FOOD_IP>:8082"
$env:SERVICE_TOKEN="service-token"
mvn spring-boot:run
```

### Demo qua gateway
- `POST http://<GATEWAY_IP>:8080/api/orders` (cần Bearer token)

Body mẫu:
```json
{
  "items": [
    { "foodId": 1, "quantity": 1 }
  ]
}
```

- `GET http://<GATEWAY_IP>:8080/api/orders`:
  - USER → chỉ thấy đơn của mình
  - ADMIN → thấy tất cả

### Điểm cần nói khi demo
- Order Service nhận `Authorization` (Bearer) và gọi `/validate` ở User Service để chắc user hợp lệ.
- Order Service gọi Food Service `GET /foods/{id}` để lấy `name/price` trước khi tính tổng.

---

## Người 5 — Payment + Notification + API Gateway
### Nhiệm vụ demo
- Chứng minh Payment gọi Order để update status và in notification log.
- Chứng minh Gateway route + CORS + verify JWT.

### Chạy API Gateway (máy GATEWAY_IP)
```powershell
cd api-gateway
$env:FRONTEND_ORIGIN="http://<FRONTEND_IP>:3000"
$env:USER_SERVICE_URL="http://<USER_IP>:8081"
$env:FOOD_SERVICE_URL="http://<FOOD_IP>:8082"
$env:ORDER_SERVICE_URL="http://<ORDER_IP>:8083"
$env:PAYMENT_SERVICE_URL="http://<PAYMENT_IP>:8084"
$env:JWT_SECRET="dev-secret-change-me-dev-secret-change-me"
mvn spring-boot:run
```

### Chạy Payment Service (máy PAYMENT_IP)
```powershell
cd payment-service
$env:ORDER_SERVICE_URL="http://<ORDER_IP>:8083"
$env:SERVICE_TOKEN="service-token"
mvn spring-boot:run
```

### Demo qua gateway
- `POST http://<GATEWAY_IP>:8080/api/payments` (cần Bearer token)

Body mẫu:
```json
{ "orderId": 1, "method": "COD" }
```

### Điểm cần show khi demo
- Log Payment Service (notification):
  - `User <id> đã đặt đơn #<orderId> thành công`
- Giải thích Payment gọi nội bộ:
  - `PUT /orders/{id}/status` kèm `X-Service-Token`

---

## 4) Checklist demo tổng hợp (MC dẫn/đứng trình bày)
1. Người 5 xác nhận Gateway `UP`:
   - `GET http://<GATEWAY_IP>:8080/actuator/health`
2. Người 1 mở UI: `http://<FRONTEND_IP>:3000`
3. Người 1 login bằng `user/user123`
4. Người 1 mở Foods và thêm vào cart
5. Người 1 tạo order và đọc to Order ID
6. Người 1 thanh toán COD/BANKING
7. Người 5 show log notification ở Payment Service
8. Người 1 vào Orders để thấy status `PAID`

## 5) Chế độ Docker (giai đoạn 2) — demo nhanh trên 1 máy
Nếu demo 1 máy:
```powershell
docker compose build
docker compose up -d
```

Gateway: `http://localhost:8080` \nFrontend: `http://localhost:3000`

Khi demo 5 máy (mỗi người chạy 1 service/module), chỉ cần đổi IP ở các chỗ sau

Người 1 (Frontend): sửa frontend/.env
VITE_API_BASE_URL=http://<GATEWAY_IP>:8080

Người 2 (API Gateway): sửa api-gateway/.env
FRONTEND_ORIGIN=http://<FRONTEND_IP>:3000
USER_SERVICE_URL=http://<USER_IP>:8081
FOOD_SERVICE_URL=http://<FOOD_IP>:8082
ORDER_SERVICE_URL=http://<ORDER_IP>:8083
PAYMENT_SERVICE_URL=http://<PAYMENT_IP>:8084

Người 3 (Order Service): sửa order-service/.env
USER_SERVICE_URL=http://<USER_IP>:8081
FOOD_SERVICE_URL=http://<FOOD_IP>:8082

Người 4 (Payment Service): sửa payment-service/.env
ORDER_SERVICE_URL=http://<ORDER_IP>:8083

Và nhớ 2 giá trị phải giống nhau giữa các máy
JWT_SECRET: phải giống giữa user-service/.env và api-gateway/.env
SERVICE_TOKEN: phải giống giữa order-service/.env và payment-service/.env
Sau đó mỗi người chỉ cần chạy mvn spring-boot:run (hoặc frontend npm run dev) là demo được.