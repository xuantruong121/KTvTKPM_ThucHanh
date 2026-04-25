package iuh.fit.paymentservice.dto;

import iuh.fit.paymentservice.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class PaymentDtos {
    public record CreatePaymentRequest(
            @NotNull Long orderId,
            @NotNull PaymentMethod method
    ) {}

    public record PaymentResponse(
            Long id,
            Long orderId,
            Long userId,
            PaymentMethod method,
            Instant createdAt
    ) {}
}

