package iuh.fit.orderservice.resilience;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@Order(1)
public class ResilienceTestService {

    // Đếm số lần gọi để mô phỏng lỗi / phục hồi
    private final AtomicInteger cbCallCount    = new AtomicInteger(0);
    private final AtomicInteger retryCallCount = new AtomicInteger(0);

    // -------------------------------------------------------
    // 1. CIRCUIT BREAKER – backendA
    //    Cứ 2 lần gọi đầu thì lỗi, lần 3 thành công
    // -------------------------------------------------------
    @CircuitBreaker(name = "backendA", fallbackMethod = "circuitBreakerFallback")
    public String callWithCircuitBreaker() {
        int count = cbCallCount.incrementAndGet();
        log.info("[CircuitBreaker] call #{}", count);
        // Giả lập: 70% lỗi để kích hoạt circuit breaker
        if (count % 3 != 0) {
            throw new RuntimeException("Simulated backend failure (call #" + count + ")");
        }
        return "CircuitBreaker OK – call #" + count;
    }

    public String circuitBreakerFallback(Throwable t) {
        log.warn("[CircuitBreaker] FALLBACK triggered: {}", t.getMessage());
        return "FALLBACK: Circuit is OPEN – " + t.getMessage();
    }

    // -------------------------------------------------------
    // 2. RETRY – backendA
    //    Lỗi 2 lần đầu, lần 3 thành công → thấy 3 log attempts
    // -------------------------------------------------------
    @Retry(name = "backendA", fallbackMethod = "retryFallback")
    public String callWithRetry() {
        int count = retryCallCount.incrementAndGet();
        log.info("[Retry] attempt #{}", count);
        if (count % 3 != 0) {
            throw new RuntimeException("Transient error on attempt #" + count);
        }
        return "Retry SUCCESS on attempt #" + count;
    }

    public String retryFallback(Throwable t) {
        log.warn("[Retry] FALLBACK after all attempts: {}", t.getMessage());
        return "FALLBACK: All retries exhausted – " + t.getMessage();
    }

    // -------------------------------------------------------
    // 3. RATE LIMITER – backendA  (10 req / 1s)
    //    Gọi > 10 lần trong 1 giây → bị từ chối
    // -------------------------------------------------------
    @RateLimiter(name = "backendA", fallbackMethod = "rateLimiterFallback")
    public String callWithRateLimiter() {
        log.info("[RateLimiter] request accepted");
        return "RateLimiter OK – request passed";
    }

    public String rateLimiterFallback(Throwable t) {
        log.warn("[RateLimiter] FALLBACK – rate limit exceeded: {}", t.getMessage());
        return "FALLBACK: Rate limit exceeded – try again later";
    }

    // -------------------------------------------------------
    // 4. BULKHEAD – backendA  (max 10 concurrent)
    //    Gọi đồng thời > 10 → bị từ chối
    // -------------------------------------------------------
    @Bulkhead(name = "backendA", fallbackMethod = "bulkheadFallback")
    public String callWithBulkhead() throws InterruptedException {
        log.info("[Bulkhead] executing request...");
        Thread.sleep(500); // giữ slot để test concurrent
        return "Bulkhead OK – request completed";
    }

    public String bulkheadFallback(Throwable t) {
        log.warn("[Bulkhead] FALLBACK – max concurrent calls reached: {}", t.getMessage());
        return "FALLBACK: Bulkhead full – " + t.getMessage();
    }

    // -------------------------------------------------------
    // 5. TIME LIMITER – backendA  (timeout = 2s)
    //    Nếu tác vụ chạy quá 2s → timeout
    // -------------------------------------------------------
    @TimeLimiter(name = "backendA", fallbackMethod = "timeLimiterFallback")
    public CompletableFuture<String> callWithTimeLimiter(boolean slowMode) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int delay = slowMode ? 5000 : 500;
                log.info("[TimeLimiter] sleeping {}ms...", delay);
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "TimeLimiter OK – completed in time";
        });
    }

    public CompletableFuture<String> timeLimiterFallback(Throwable t) {
        log.warn("[TimeLimiter] FALLBACK – timeout: {}", t.getMessage());
        return CompletableFuture.completedFuture("FALLBACK: Timeout – " + t.getMessage());
    }

    // -------------------------------------------------------
    // Reset counters (để test lại từ đầu)
    // -------------------------------------------------------
    public void resetCounters() {
        cbCallCount.set(0);
        retryCallCount.set(0);
        log.info("Counters reset to 0");
    }
}
