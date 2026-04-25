package iuh.fit.paymentservice.repo;

import iuh.fit.paymentservice.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

