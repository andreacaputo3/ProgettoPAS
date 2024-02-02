package it.unisalento.pas.smartcitywastemanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import it.unisalento.pas.smartcitywastemanagement.domain.Payment;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    List<Payment> findByUser_Id(String userId);
}
