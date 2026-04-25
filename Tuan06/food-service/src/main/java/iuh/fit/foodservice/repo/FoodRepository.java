package iuh.fit.foodservice.repo;

import iuh.fit.foodservice.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}

