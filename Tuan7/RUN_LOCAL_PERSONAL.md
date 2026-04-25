## Chạy demo cá nhân (1 máy) — IP hiện tại: `192.168.1.58`

### Nguyên tắc
- Mỗi module Spring Boot **tự đọc `.env`** trong thư mục của nó (nhờ `spring-dotenv`).
- Khi test trên 1 máy, các URL trong `.env` đã trỏ về `http://192.168.1.58:<port>`.
- Mỗi service chạy **một terminal riêng**.

### 1) Chạy backend theo thứ tự khuyến nghị
Mở 5 terminal và chạy lần lượt:

**User Service**
```powershell
cd d:\Study\KTvTKPM\Buoi5\user-service
mvn spring-boot:run
```

**Food Service**
```powershell
cd d:\Study\KTvTKPM\Buoi5\food-service
mvn spring-boot:run
```

**Order Service**
```powershell
cd d:\Study\KTvTKPM\Buoi5\order-service
mvn spring-boot:run
```

**Payment Service**
```powershell
cd d:\Study\KTvTKPM\Buoi5\payment-service
mvn spring-boot:run
```

**API Gateway**
```powershell
cd d:\Study\KTvTKPM\Buoi5\api-gateway
mvn spring-boot:run
```

Kiểm tra gateway:
- `GET http://192.168.1.58:8080/actuator/health` → `{"status":"UP"}`

### 2) Chạy Frontend
```powershell
cd d:\Study\KTvTKPM\Buoi5\frontend
npm install
npm run dev -- --host 0.0.0.0 --port 3000
```

Mở trình duyệt:
- `http://192.168.1.58:3000`

### 3) Demo kịch bản bắt buộc
1. Login: `user/user123` hoặc `admin/admin123`
2. Foods → Add to cart
3. Cart → Create order (lấy Order ID)
4. Pay → nhập Order ID → COD/BANKING
5. Xem log ở Payment Service: `"User <id> đã đặt đơn #<orderId> thành công"`

### 4) Khi đổi mạng (IP thay đổi)
Sửa các file `.env` sau:
- `api-gateway/.env`: sửa `FRONTEND_ORIGIN` + các `*_SERVICE_URL`
- `order-service/.env`: sửa `USER_SERVICE_URL`, `FOOD_SERVICE_URL`
- `payment-service/.env`: sửa `ORDER_SERVICE_URL`
- `frontend/.env`: sửa `VITE_API_BASE_URL`

`JWT_SECRET` (Gateway + User) và `SERVICE_TOKEN` (Order + Payment) phải giữ **giống nhau**.

