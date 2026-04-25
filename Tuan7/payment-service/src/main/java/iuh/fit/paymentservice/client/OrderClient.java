package iuh.fit.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "orderClient", url = "${clients.order-service.url}")
public interface OrderClient {

    record UpdateStatusRequest(String status) {}
    record OrderResponse(Long id, Long userId, String status) {}

    @PutMapping("/orders/{id}/status")
    OrderResponse updateStatus(
            @RequestHeader("X-Service-Token") String serviceToken,
            @PathVariable("id") Long id,
            @RequestBody UpdateStatusRequest req
    );
}

