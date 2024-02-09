package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.Payment;
import it.unisalento.pas.smartcitywastemanagement.dto.PaymentDTO;
import it.unisalento.pas.smartcitywastemanagement.exceptions.UserNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.repositories.PaymentRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public void makePayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentDate(null);
        payment.setEmissionDate(new Date());
        payment.setPaid(false);
        payment.setUserId(paymentDTO.getUserId());

        paymentRepository.save(payment);
    }

    public void payPayment(String userId, String paymentId) {
        Payment payment = paymentRepository.findByIdAndUserId(paymentId, userId);
        if (payment == null) {
            throw new UserNotFoundException("Pagamento non trovato per l'utente specificato");
        }
        payment.setPaymentDate(new Date());
        payment.setPaid(true);
        paymentRepository.save(payment);
    }

    public List<Payment> getAllPaymentsByUserId(String userId) {
        return paymentRepository.findByUserId(userId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

}
