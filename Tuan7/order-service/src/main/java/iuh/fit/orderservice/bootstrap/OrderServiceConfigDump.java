package iuh.fit.orderservice.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceConfigDump implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceConfigDump.class);

    @Value("${clients.user-service.url}")
    private String userServiceUrl;

    @Value("${clients.food-service.url}")
    private String foodServiceUrl;

    @Value("${internal.service-token}")
    private String serviceToken;

    @Override
    public void run(String... args) {
        log.info("[order-service] clients.user-service.url={}", userServiceUrl);
        log.info("[order-service] clients.food-service.url={}", foodServiceUrl);
        log.info("[order-service] internal.service-token={}", serviceToken);
    }
}

