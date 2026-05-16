# Order Service – Resilience4j Demo

## Thành viên nhóm
| # | Họ và tên |
|---|-----------|
| 1 | Nguyễn Hoàng Sơn |
| 2 | Nguyễn Phú Triệu |
| 3 | Huỳnh Công Ý |
| 4 | Nguyễn Văn Khải Tiến |
| 5 | Nguyễn Đỗ Xuân Trường |

---

## Tổng quan

Spring Boot microservice minh họa các pattern **Resilience4j**:

| Pattern | Instance | Mô tả |
|---|---|---|
| Circuit Breaker | `backendA`, `backendB` | Ngắt mạch khi tỉ lệ lỗi vượt ngưỡng |
| Retry | `backendA`, `backendB` | Tự động thử lại khi gặp lỗi tạm thời |
| Rate Limiter | `backendA`, `backendB` | Giới hạn số request trong một khoảng thời gian |
| Bulkhead | `backendA`, `backendB` | Giới hạn số request đồng thời |
| Time Limiter | `backendA`, `backendB` | Timeout nếu xử lý quá lâu |

---

## Yêu cầu

- Java 17+
- Maven (hoặc dùng `mvnw.cmd` đi kèm)

---

## Cách chạy ứng dụng

Mở terminal tại thư mục gốc của project và chạy:

```cmd
.\mvnw.cmd spring-boot:run
```

Ứng dụng khởi động thành công khi log hiện:

```
Started OrderServiceApplication in X.XXX seconds
```

> **URL:** `http://localhost:8083`

---

## Cách test thủ công (CMD)

> Mở **cmd.exe thứ 2** trong khi terminal thứ 1 đang chạy app để quan sát log.

---

### Bước 1 – Kiểm tra app đang chạy

```cmd
curl http://localhost:8083/actuator/health
```

Kết quả mong đợi: `{"status":"UP",...}`

---

### Bước 2 – Reset bộ đếm (trước mỗi lần test)

```cmd
curl -X POST http://localhost:8083/resilience/reset
```

---

### Test 1 – Retry

**Cấu hình:** `maxAttempts=3`, `waitDuration=1s`, exponential backoff x2

```cmd
curl -X POST http://localhost:8083/resilience/reset
curl http://localhost:8083/resilience/retry
```

**Quan sát log app:**
```
[Retry] attempt #1   → lỗi, đợi 1s
[Retry] attempt #2   → lỗi, đợi 2s (backoff x2)
[Retry] attempt #3   → thành công!
```

**Response trả về:** `Retry SUCCESS on attempt #3`

> Nếu gọi thêm lần nữa: attempt #4 (lỗi), #5 (lỗi), #6 (thành công)

---

### Test 2 – Rate Limiter

**Cấu hình:** `limitForPeriod=10`, `limitRefreshPeriod=1s`

```cmd
for /l %i in (1,1,15) do curl http://localhost:8083/resilience/rate-limiter
```

**Kết quả mong đợi:**
- Request 1–10: `RateLimiter OK – request passed`
- Request 11–15: `FALLBACK: Rate limit exceeded – try again later`

> Đợi 1 giây rồi gọi lại → sẽ OK trở lại (window mới)

---

### Test 3 – Circuit Breaker

**Cấu hình:** `slidingWindowSize=100`, lỗi 2/3 lần → tích lũy failure

```cmd
curl -X POST http://localhost:8083/resilience/reset
for /l %i in (1,1,9) do curl http://localhost:8083/resilience/circuit-breaker
```

**Kết quả mong đợi:**
- Lần 1: `FALLBACK: Circuit is OPEN – Simulated backend failure (call #1)`
- Lần 2: `FALLBACK: Circuit is OPEN – Simulated backend failure (call #2)`
- Lần 3: `CircuitBreaker OK – call #3`
- Lần 4, 5: FALLBACK | Lần 6: OK | ...

> Khi Circuit Breaker chuyển sang `OPEN` (sau đủ failure), **tất cả** đều FALLBACK

Kiểm tra trạng thái Circuit Breaker:
```cmd
curl http://localhost:8083/actuator/health
```

---

### Test 4 – Time Limiter

**Cấu hình:** `timeoutDuration=2s`

```cmd
:: Fast (0.5s < 2s) → thành công
curl "http://localhost:8083/resilience/time-limiter?slow=false"

:: Slow (5s > 2s) → timeout → FALLBACK
curl "http://localhost:8083/resilience/time-limiter?slow=true"
```

**Kết quả mong đợi:**
- `slow=false` → `TimeLimiter OK – completed in time`
- `slow=true`  → `FALLBACK: Timeout – ...`

---

### Test 5 – Bulkhead (Concurrent)

**Cấu hình:** `maxConcurrentCalls=10` (mỗi request giữ slot 500ms)

Mở **nhiều cửa sổ CMD** và chạy đồng thời lệnh sau ở mỗi cửa sổ (>10 cửa sổ):

```cmd
curl http://localhost:8083/resilience/bulkhead
```

- 10 request đầu vào được: `Bulkhead OK – request completed`
- Request thứ 11 trở đi: `FALLBACK: Bulkhead full – ...`

---

## Actuator Endpoints

| URL | Mô tả |
|---|---|
| `/actuator/health` | Tổng trạng thái + trạng thái CB |
| `/actuator/circuitbreakers` | Chi tiết tất cả Circuit Breaker |
| `/actuator/retries` | Thống kê Retry |
| `/actuator/ratelimiters` | Thống kê Rate Limiter |
| `/actuator/bulkheads` | Thống kê Bulkhead |

```cmd
curl http://localhost:8083/actuator/health
curl http://localhost:8083/actuator/circuitbreakers
curl http://localhost:8083/actuator/ratelimiters
```

---

## Cấu trúc thư mục quan trọng

```
src/main/java/.../resilience/
├── ResilienceTestController.java   # Các endpoint /resilience/*
├── ResilienceTestService.java      # Logic demo từng pattern
└── BusinessException.java          # Exception bị bỏ qua khi Retry

src/main/resources/
└── application.yml                 # Toàn bộ cấu hình Resilience4j
```