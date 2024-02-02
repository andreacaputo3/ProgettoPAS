package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.Payment;
import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.dto.PaymentDTO;
import it.unisalento.pas.smartcitywastemanagement.exceptions.UserNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.repositories.PaymentRepository;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Payment> getPaymentsForUser(String userId) {
        return paymentRepository.findByUser_Id(userId);
    }

    public boolean payPayment(String paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            if (!payment.isPaid()) {
                // Simulazione del pagamento
                payment.setPaid(true);
                paymentRepository.save(payment);
                return true; // Pagamento effettuato con successo
            } else {
                return false; // Pagamento già pagato
            }
        } else {
            return false; // Pagamento non trovato
        }
    }

    public void makePayment(PaymentDTO paymentDTO) {


    }


    public void makePayment(BigDecimal amount, Date paymentDate, String userId) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentDate(paymentDate);
        payment.setPaid(false);

        // Trova l'utente dal repository utilizzando l'ID fornito
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Aggiunge il nuovo pagamento all'utente
        user.getPayments().add(payment);

        // Salva il pagamento e l'utente aggiornato nei rispettivi repository
        paymentRepository.save(payment);
        //userRepository.save(user);
    }
}
