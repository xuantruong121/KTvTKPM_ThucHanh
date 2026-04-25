package iuh.fit.paymentservice.web;

import iuh.fit.paymentservice.client.OrderClient;
import iuh.fit.paymentservice.domain.Payment;
import iuh.fit.paymentservice.dto.PaymentDtos;
import iuh.fit.paymentservice.repo.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    @Value("${internal.service-token:service-token}")
    private String serviceToken;

    @PostMapping
    public PaymentDtos.PaymentResponse pay(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody PaymentDtos.CreatePaymentRequest req
    ) {
        log.info("==================== [PAYMENT-SERVICE] RECEIVING NEW PAYMENT REQUEST ====================");
        if (userIdHeader == null) {
            log.error("[AUTH] Missing X-User-Id header. Access Denied.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing auth");
        }
        Long userId = Long.valueOf(userIdHeader);
        log.info("[REQUEST] User ID: {}, Order ID: {}, Method: {}", userId, req.orderId(), req.method());

        // Update order status via Order Service
        log.info("[FEIGN] Calling Order-Service to update status for Order #{} to 'PAID'...", req.orderId());
        OrderClient.OrderResponse updated = orderClient.updateStatus(
                serviceToken,
                req.orderId(),
                new OrderClient.UpdateStatusRequest("PAID")
        );
        log.info("[FEIGN] Order-Service responded successfully for Order #{}. Current userId in order: {}", req.orderId(), updated.userId());

        // Simple safeguard: user can only pay their own order unless ADMIN
        if (!"ADMIN".equals(role) && updated.userId() != null && !updated.userId().equals(userId)) {
            log.warn("[SECURITY] User {} attempted to pay for Order #{} belonging to User {}", userId, req.orderId(), updated.userId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot pay for another user");
        }

        Payment p = new Payment();
        p.setOrderId(req.orderId());
        p.setUserId(userId);
        p.setMethod(req.method());
        p.setCreatedAt(Instant.now());
        
        log.info("[DB] Saving payment record for Order #{}...", req.orderId());
        Payment saved = paymentRepository.save(p);
        log.info("[DB] Payment saved success. PaymentID: {}", saved.getId());

        // Notification (console log)
        log.info(">>> SUCCESS: User {} completed payment for Order #{} successfully.", userId, req.orderId());
        log.info("==================== [PAYMENT-SERVICE] REQUEST PROCESSED ====================");

        return new PaymentDtos.PaymentResponse(saved.getId(), saved.getOrderId(), saved.getUserId(), saved.getMethod(), saved.getCreatedAt());
    }
}

