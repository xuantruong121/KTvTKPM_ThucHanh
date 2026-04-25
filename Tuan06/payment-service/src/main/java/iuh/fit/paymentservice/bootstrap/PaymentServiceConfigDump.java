package iuh.fit.paymentservice.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceConfigDump implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceConfigDump.class);

    @Value("${clients.order-service.url}")
    private String orderServiceUrl;

    @Value("${internal.service-token}")
    private String serviceToken;

    @Override
    public void run(String... args) {
        log.info("[payment-service] clients.order-service.url={}", orderServiceUrl);
        log.info("[payment-service] internal.service-token={}", serviceToken);
    }
}

