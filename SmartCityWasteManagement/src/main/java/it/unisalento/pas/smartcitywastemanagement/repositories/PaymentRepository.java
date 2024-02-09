package it.unisalento.pas.smartcitywastemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import it.unisalento.pas.smartcitywastemanagement.domain.Payment;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByUserId(String userId);

    Payment findByIdAndUserId(String paymentId, String userId);

    boolean existsByUserIdAndPaid(String userId, boolean b);
}
