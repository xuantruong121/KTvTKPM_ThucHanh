package iuh.fit.orderservice.web;

import iuh.fit.orderservice.client.FoodClient;
import iuh.fit.orderservice.client.UserClient;
import iuh.fit.orderservice.domain.Order;
import iuh.fit.orderservice.domain.OrderItem;
import iuh.fit.orderservice.domain.OrderStatus;
import iuh.fit.orderservice.dto.OrderDtos;
import iuh.fit.orderservice.repo.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final FoodClient foodClient;

    @Value("${internal.service-token:service-token}")
    private String serviceToken;

    @PostMapping
    public OrderDtos.OrderResponse create(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            @Valid @RequestBody OrderDtos.CreateOrderRequest req
    ) {
        if (userIdHeader == null || authorization == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing auth");
        }

        UserClient.ValidateResponse validated = userClient.validate(authorization);
        Long userId = validated.id();
        if (!String.valueOf(userId).equals(userIdHeader)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User mismatch");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(Instant.now());

        BigDecimal total = BigDecimal.ZERO;
        for (OrderDtos.CreateOrderItem itemReq : req.items()) {
            FoodClient.FoodResponse food = foodClient.getFood(itemReq.foodId());
            BigDecimal line = food.price().multiply(BigDecimal.valueOf(itemReq.quantity()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setFoodId(food.id());
            item.setFoodName(food.name());
            item.setUnitPrice(food.price());
            item.setQuantity(itemReq.quantity());
            item.setLineTotal(line);

            order.getItems().add(item);
            total = total.add(line);
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @GetMapping
    public List<OrderDtos.OrderResponse> list(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestHeader(value = "X-User-Role", required = false) String role
    ) {
        if ("ADMIN".equals(role)) {
            return orderRepository.findAll().stream().map(this::toResponse).toList();
        }
        if (userIdHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing auth");
        }
        Long userId = Long.valueOf(userIdHeader);
        return orderRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}/status")
    public OrderDtos.OrderResponse updateStatus(
            @RequestHeader(value = "X-Service-Token", required = false) String token,
            @PathVariable Long id,
            @Valid @RequestBody OrderDtos.UpdateStatusRequest req
    ) {
        if (token == null || !token.equals(serviceToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        o.setStatus(req.status());
        Order saved = orderRepository.save(o);
        return toResponse(saved);
    }

    private OrderDtos.OrderResponse toResponse(Order o) {
        List<OrderDtos.OrderItemResponse> items = o.getItems().stream()
                .map(i -> new OrderDtos.OrderItemResponse(i.getFoodId(), i.getFoodName(), i.getUnitPrice(), i.getQuantity(), i.getLineTotal()))
                .toList();
        return new OrderDtos.OrderResponse(o.getId(), o.getUserId(), o.getStatus(), o.getTotalAmount(), o.getCreatedAt(), items);
    }
}

