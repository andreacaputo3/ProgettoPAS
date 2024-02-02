package it.unisalento.pas.smartcitywastemanagement.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/api/payments")
public class PaymentRestController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/make-payment")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> makePayment(@RequestParam BigDecimal amount, @RequestParam String userId) {
        Date paymentDate = new Date();  // Data corrente
        paymentService.makePayment(amount, paymentDate, userId);
        return new ResponseEntity<>("Payment made successfully", HttpStatus.OK);
    }
}
