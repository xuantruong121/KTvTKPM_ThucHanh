package iuh.fit.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "userClient", url = "${clients.user-service.url}")
public interface UserClient {

    record ValidateResponse(Long id, String username, String role) {}

    @GetMapping("/validate")
    ValidateResponse validate(@RequestHeader("Authorization") String authorization);
}

