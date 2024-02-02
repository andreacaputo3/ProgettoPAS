package it.unisalento.pas.smartcitywastemanagement.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.domain.Payment;
import it.unisalento.pas.smartcitywastemanagement.dto.PaymentDTO;
import it.unisalento.pas.smartcitywastemanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentRestController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/make-payment")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> makePayment(@RequestBody PaymentDTO paymentDTO) {

        paymentDTO.setPaymentDate(new Date());

        paymentService.makePayment(paymentDTO);

        return new ResponseEntity<>("Payment made successfully", HttpStatus.OK);
    }

}
