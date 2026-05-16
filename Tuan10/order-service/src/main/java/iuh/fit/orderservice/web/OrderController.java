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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("Received request to create order. Header X-User-Id: {}", userIdHeader);
        if (userIdHeader == null || authorization == null) {
            log.warn("Missing X-User-Id or Authorization header");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing auth");
        }

        log.info("Calling User Service to validate token...");
        UserClient.ValidateResponse validated = userClient.validate(authorization);
        log.info("User validated successfully. User ID: {}, Role: {}", validated.id(), validated.role());
        
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
            log.info("Calling Food Service to get details for foodId: {}", itemReq.foodId());
            FoodClient.FoodResponse food = foodClient.getFood(itemReq.foodId());
            log.info("Food details retrieved: {}", food);
            
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
        log.info("Order created successfully with ID: {}, Total Amount: {}", saved.getId(), saved.getTotalAmount());
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
        log.info("Received request to update order {} status to {}", id, req.status());
        if (token == null || !token.equals(serviceToken)) {
            log.warn("Invalid or missing X-Service-Token");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
        Order o = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order {} not found", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
                });
        o.setStatus(req.status());
        Order saved = orderRepository.save(o);
        log.info("Order {} status updated successfully to {}", id, saved.getStatus());
        return toResponse(saved);
    }

    private OrderDtos.OrderResponse toResponse(Order o) {
        List<OrderDtos.OrderItemResponse> items = o.getItems().stream()
                .map(i -> new OrderDtos.OrderItemResponse(i.getFoodId(), i.getFoodName(), i.getUnitPrice(), i.getQuantity(), i.getLineTotal()))
                .toList();
        return new OrderDtos.OrderResponse(o.getId(), o.getUserId(), o.getStatus(), o.getTotalAmount(), o.getCreatedAt(), items);
    }
}

