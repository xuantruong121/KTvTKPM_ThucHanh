package com.flashsale.orderservice.controller;

import com.flashsale.orderservice.model.Order;
import com.flashsale.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final RestTemplate restTemplate;

    private final String AUTH_SERVICE_URL = "http://192.168.137.180:8080/api/auth/me";

    @Autowired
    public OrderController(OrderService orderService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
    }

    private String getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(AUTH_SERVICE_URL, HttpMethod.GET, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("username");
            }
        } catch (Exception e) {
            log.error("❌ Auth check failed: {}", e.getMessage());
        }
        return null;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestHeader(value = "Authorization", required = false) String token) {
        String userId = getUserIdFromToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Yêu cầu đăng nhập"));
        }
        log.info("📥 API POST /order/checkout: userId={}", userId);
        try {
            Order order = orderService.checkout(userId);
            return ResponseEntity.ok(Map.of(
                    "message", "Order placed successfully",
                    "userId", userId,
                    "order", order));
        } catch (Exception e) {
            log.error("❌ Checkout failed for userId {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "order-service",
                "port", 8083,
                "architecture", "Space-Based Architecture"));
    }
}
