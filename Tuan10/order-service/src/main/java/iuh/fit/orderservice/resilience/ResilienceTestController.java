package iuh.fit.orderservice.resilience;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * Controller chỉ dùng để DEMO / TEST các Resilience4j patterns.
 * Truy cập tại: http://localhost:8083/resilience/...
 */
@RestController
@RequestMapping("/resilience")
@RequiredArgsConstructor
@Slf4j
public class ResilienceTestController {

    private final ResilienceTestService service;

    // ------------------------------------------------------------------
    // Circuit Breaker
    // Gọi liên tục → 2/3 lần lỗi → CB chuyển sang OPEN → fallback
    // GET http://localhost:8083/resilience/circuit-breaker
    // ------------------------------------------------------------------
    @GetMapping("/circuit-breaker")
    public String testCircuitBreaker() {
        return service.callWithCircuitBreaker();
    }

    // ------------------------------------------------------------------
    // Retry
    // Mỗi lần gọi: thử 3 lần (log sẽ hiện 3 attempts), lần 3 thành công
    // GET http://localhost:8083/resilience/retry
    // ------------------------------------------------------------------
    @GetMapping("/retry")
    public String testRetry() {
        return service.callWithRetry();
    }

    // ------------------------------------------------------------------
    // Rate Limiter
    // Cho phép 10 req / 1 giây. Gọi nhanh > 10 lần → fallback
    // GET http://localhost:8083/resilience/rate-limiter
    // (Dùng Apache Bench hoặc lặp trong Postman để test burst)
    // ------------------------------------------------------------------
    @GetMapping("/rate-limiter")
    public String testRateLimiter() {
        return service.callWithRateLimiter();
    }

    // ------------------------------------------------------------------
    // Bulkhead
    // Gọi đồng thời > 10 requests → các request thừa nhận fallback
    // GET http://localhost:8083/resilience/bulkhead
    // ------------------------------------------------------------------
    @GetMapping("/bulkhead")
    public String testBulkhead() throws InterruptedException {
        return service.callWithBulkhead();
    }

    // ------------------------------------------------------------------
    // Time Limiter
    // ?slow=true  → sleep 5s → vượt timeout 2s → fallback
    // ?slow=false → sleep 0.5s → OK
    // GET http://localhost:8083/resilience/time-limiter?slow=true
    // ------------------------------------------------------------------
    @GetMapping("/time-limiter")
    public CompletableFuture<String> testTimeLimiter(
            @RequestParam(defaultValue = "false") boolean slow) {
        return service.callWithTimeLimiter(slow);
    }

    // ------------------------------------------------------------------
    // Reset bộ đếm để test lại từ đầu
    // POST http://localhost:8083/resilience/reset
    // ------------------------------------------------------------------
    @PostMapping("/reset")
    public String reset() {
        service.resetCounters();
        return "Counters reset – bạn có thể test lại từ đầu";
    }
}
