package com.flashsale.orderservice.service;

import iuh.fit.cartpu.entity.CartItem;
import iuh.fit.cartpu.entity.UserCart;
import com.flashsale.orderservice.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);


    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final RestTemplate restTemplate;

    @Value("${services.inventory.url}")
    private String inventoryServiceUrl;

    @Value("${services.cart.url}")
    private String cartServiceUrl;

    private static final String CART_KEY_PREFIX = "cart:";
    private static final String ORDER_KEY_PREFIX = "order:";
    private static final String STOCK_KEY_PREFIX = "stock:";

    @Autowired
    public OrderService(RedisTemplate<String, Object> redisTemplate, 
                        StringRedisTemplate stringRedisTemplate,
                        RestTemplate restTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.restTemplate = restTemplate;
    }

    public Order checkout(String userId) {
        log.info("🚀 Starting checkout for userId: {}", userId);
        // 1. Lấy Cart từ Redis thông qua RedisTemplate (Key: cart:userId)
        UserCart cart = null;
        try {
            cart = (UserCart) redisTemplate.opsForValue().get(CART_KEY_PREFIX + userId);
        } catch (Exception e) {
            log.error("❌ Giỏ hàng không tương thích cho userId {}: {}", userId, e.getMessage());
            redisTemplate.delete(CART_KEY_PREFIX + userId);
            throw new RuntimeException("Dữ liệu giỏ hàng cũ không hợp lệ, vui lòng thêm lại sản phẩm vào giỏ hàng.");
        }
        
        if (cart == null) {
            throw new RuntimeException("Không tìm thấy giỏ hàng cho người dùng: " + userId);
        }

        List<CartItem> items = cart.getItems();
        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        double totalAmount = 0;
        for (CartItem item : items) {
            totalAmount += item.getSubtotal();
        }

        // 2. Giảm Stock trực tiếp trên Redis (SBA: Thao tác trên Hash "inventory")
        log.info("📉 Reducing stock for {} items in 'inventory' hash", items.size());
        for (CartItem item : items) {
            int currentStock = getSafeStock(item.getProductId());

            // Nếu không có trong inventory, thử lấy từ products hash (Lazy init)
            if (currentStock == 0) {
                Object rawProd = stringRedisTemplate.opsForHash().get("products", item.getProductId());
                if (rawProd != null) {
                    try {
                        String s = rawProd.toString().trim().replaceAll("[^0-9-]", "");
                        if (!s.isEmpty()) currentStock = Integer.parseInt(s);
                    } catch (Exception e) { log.error("Error lazy init: {}", e.getMessage()); }
                }
            }

            if (currentStock < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + item.getName() + " không đủ hàng (Hiện có: " + currentStock + ")");
            }

            // Ghi đè giá trị sạch (loại bỏ binary header cũ) rồi set giá trị mới
            // Dùng PUT thay vì HINCRBY để tránh lỗi "ERR hash value is not an integer"
            int newStock = currentStock - item.getQuantity();
            stringRedisTemplate.opsForHash().put("inventory", item.getProductId(), String.valueOf(newStock));
            log.info("✅ Decreased stock for {}. Remaining: {}", item.getProductId(), newStock);
        }

        // 3. Tạo Order và lưu vào Redis
        log.info("📦 Creating order for userId: {}, Amount: {}", userId, totalAmount);
        Order order = new Order(items, totalAmount, "COMPLETED");
        redisTemplate.opsForValue().set(ORDER_KEY_PREFIX + order.getOrderId(), order);

        // 4. Xóa Cart sau khi hoàn tất (Key: cart:userId)
        redisTemplate.delete(CART_KEY_PREFIX + userId);

        log.info("✅ Checkout successful for user: {}. OrderID: {}, Total: {}", userId, order.getOrderId(), totalAmount);
        return order;
    }

    private int getSafeStock(String productId) {
        try {
            // Dùng stringRedisTemplate (String serializer) nhất quán với lúc ghi
            Object val = stringRedisTemplate.opsForHash().get("inventory", productId);
            if (val != null) {
                String s = val.toString().trim().replaceAll("[^0-9-]", "");
                if (!s.isEmpty()) return Integer.parseInt(s);
            }
        } catch (Exception e) {
            log.error("getSafeStock failed for {}: {}", productId, e.getMessage());
        }
        return 0;
    }
}
