# Hướng Dẫn Triển Khai Trên Nhiều Máy (LAN)

Tài liệu này hướng dẫn cách triển khai hệ thống microservices CinemaMagic trên 5 máy tính khác nhau trong cùng một mạng nội bộ (LAN).

## Điều Kiện Tiên Quyết
- Tất cả các máy phải trong cùng một mạng (có thể `ping` thông nhau).
- Đã cài đặt Java 17+ trên tất cả các máy.
- Đã cài đặt Docker trên máy "Máy Chủ Core" (dành cho Kafka).

## Bước 1: Thiết Lập Hạ Tầng (Máy số 1 - Chạy Kafka)
Chọn máy có cấu hình mạnh nhất để làm "Máy Chủ". 

1. Chuột phải vào file `start-kafka.ps1` và chọn **Run with PowerShell**.
2. Script sẽ tự động tìm IP máy của bạn (ví dụ: `192.168.1.10`) và khởi động Docker Kafka.
3. Ghi lại địa chỉ IP mà script hiển thị.

## Bước 2: Triển Khai Các Microservices (Máy số 2, 3, 4)
Khi chạy mỗi service trên một máy khác nhau, hãy dùng tham số `-D` để trỏ về máy chạy Kafka.

**Ví dụ cho Booking Service (Máy số 2):**
```bash
./mvnw spring-boot:run -pl booking-service -Dspring.kafka.bootstrap-servers=192.168.1.10:9092
```

## Bước 3: Triển Khai API Gateway (Máy số 5)
Gateway cần biết địa chỉ IP của từng microservice để điều hướng (routing).

1. Chạy Gateway với các URI được ghi đè:
```bash
./mvnw spring-boot:run -pl api-gateway \
  -Dspring.cloud.gateway.routes[0].uri=http://<IP_USER_SERVICE>:8081 \
  -Dspring.cloud.gateway.routes[1].uri=http://<IP_MOVIE_SERVICE>:8082 \
  -Dspring.cloud.gateway.routes[2].uri=http://<IP_BOOKING_SERVICE>:8083
```

## Bước 4: Kết Nối Frontend (Máy tính cá nhân của bạn)
Cập nhật file `src/api.js` trong thư mục `cinema-frontend`:
```javascript
const api = axios.create({
  baseURL: 'http://<IP_MAY_GATEWAY>:8000/api'
});
```

## Lưu Ý Quan Trọng (Troubleshooting)
- **Firewall (Tường lửa)**: Đảm bảo các cổng `8000`, `9092`, `8081-8084` đã được mở trong Windows Firewall của mỗi máy.
- **H2 DB**: Vì H2 dùng bộ nhớ tạm (in-memory), dữ liệu của mỗi service sẽ nằm độc lập trên máy đó. Khi tắt service, dữ liệu sẽ biến mất.
- **Thứ tự chạy**: Nên chạy Kafka trước, sau đó đến các microservices, cuối cùng là Gateway và Frontend.
