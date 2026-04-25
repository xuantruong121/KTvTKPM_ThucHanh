package iuh.fit.foodservice.bootstrap;

import iuh.fit.foodservice.domain.Food;
import iuh.fit.foodservice.repo.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedFoods implements CommandLineRunner {

    private final FoodRepository foodRepository;

    @Override
    public void run(String... args) {
        if (foodRepository.count() > 0) return;

        Food f1 = new Food();
        f1.setName("Cơm gà xối mỡ");
        f1.setPrice(new BigDecimal("35000.00"));

        Food f2 = new Food();
        f2.setName("Bún bò Huế");
        f2.setPrice(new BigDecimal("40000.00"));

        Food f3 = new Food();
        f3.setName("Trà sữa trân châu");
        f3.setPrice(new BigDecimal("25000.00"));

        foodRepository.saveAll(List.of(f1, f2, f3));
    }
}

