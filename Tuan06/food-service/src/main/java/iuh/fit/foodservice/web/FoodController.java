package iuh.fit.foodservice.web;

import iuh.fit.foodservice.domain.Food;
import iuh.fit.foodservice.dto.FoodDtos;
import iuh.fit.foodservice.repo.FoodRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodRepository foodRepository;

    @GetMapping
    public List<FoodDtos.FoodResponse> list() {
        return foodRepository.findAll()
                .stream()
                .map(f -> new FoodDtos.FoodResponse(f.getId(), f.getName(), f.getPrice()))
                .toList();
    }

    @GetMapping("/{id}")
    public FoodDtos.FoodResponse get(@PathVariable Long id) {
        Food f = foodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
        return new FoodDtos.FoodResponse(f.getId(), f.getName(), f.getPrice());
    }

    @PostMapping
    public FoodDtos.FoodResponse create(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody FoodDtos.FoodRequest req
    ) {
        requireAdmin(role);
        Food f = new Food();
        f.setName(req.name());
        f.setPrice(req.price());
        Food saved = foodRepository.save(f);
        return new FoodDtos.FoodResponse(saved.getId(), saved.getName(), saved.getPrice());
    }

    @PutMapping("/{id}")
    public FoodDtos.FoodResponse update(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id,
            @Valid @RequestBody FoodDtos.FoodRequest req
    ) {
        requireAdmin(role);
        Food f = foodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
        f.setName(req.name());
        f.setPrice(req.price());
        Food saved = foodRepository.save(f);
        return new FoodDtos.FoodResponse(saved.getId(), saved.getName(), saved.getPrice());
    }

    @DeleteMapping("/{id}")
    public void delete(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id
    ) {
        requireAdmin(role);
        if (!foodRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found");
        }
        foodRepository.deleteById(id);
    }

    private void requireAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ADMIN only");
        }
    }
}

