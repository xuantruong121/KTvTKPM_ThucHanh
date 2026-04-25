package iuh.fit.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "foodClient", url = "${clients.food-service.url}")
public interface FoodClient {

    record FoodResponse(Long id, String name, BigDecimal price) {}

    @GetMapping("/foods/{id}")
    FoodResponse getFood(@PathVariable("id") Long id);
}

