package iuh.fit.foodservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class FoodDtos {
    public record FoodRequest(
            @NotBlank String name,
            @NotNull BigDecimal price
    ) {}

    public record FoodResponse(
            Long id,
            String name,
            BigDecimal price
    ) {}
}

