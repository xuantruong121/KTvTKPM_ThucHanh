package iuh.fit.orderservice.dto;

import iuh.fit.orderservice.domain.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderDtos {

    public record CreateOrderItem(
            @NotNull Long foodId,
            @NotNull @Min(1) Integer quantity
    ) {}

    public record CreateOrderRequest(
            @NotNull @Valid List<CreateOrderItem> items
    ) {}

    public record OrderItemResponse(
            Long foodId,
            String foodName,
            BigDecimal unitPrice,
            Integer quantity,
            BigDecimal lineTotal
    ) {}

    public record OrderResponse(
            Long id,
            Long userId,
            OrderStatus status,
            BigDecimal totalAmount,
            Instant createdAt,
            List<OrderItemResponse> items
    ) {}

    public record UpdateStatusRequest(
            @NotNull OrderStatus status
    ) {}
}

