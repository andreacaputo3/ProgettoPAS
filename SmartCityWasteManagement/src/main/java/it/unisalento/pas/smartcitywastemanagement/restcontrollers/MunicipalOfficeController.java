package it.unisalento.pas.smartcitywastemanagement.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.domain.Payment;
import it.unisalento.pas.smartcitywastemanagement.dto.UserDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.UserWasteSeparationPerformanceDTO;
import it.unisalento.pas.smartcitywastemanagement.service.MunicipalOfficeService;
import it.unisalento.pas.smartcitywastemanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/municipal-office")
public class MunicipalOfficeController {

    @Autowired
    private MunicipalOfficeService municipalOfficeService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/users-to-aware")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<List<UserDTO>> getUsersToEducate() {
        List<UserDTO> usersToEducate = municipalOfficeService.identifyCitizensToAware();
        return new ResponseEntity<>(usersToEducate, HttpStatus.OK);
    }

    @GetMapping("/payments-state")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/waste-separation-performance")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<UserWasteSeparationPerformanceDTO> analyzeWasteSeparationPerformance() {
        UserWasteSeparationPerformanceDTO municipalPerformance = municipalOfficeService.analyzeWasteSeparationPerformance();
        return new ResponseEntity<>(municipalPerformance, HttpStatus.OK);
    }

    @GetMapping("/yearly-payment-amounts")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<Map<String, Double>> calculateYearlyPaymentAmounts() {
        Map<String, Double> yearlyPaymentAmounts = municipalOfficeService.calculateYearlyPaymentAmounts();
        return new ResponseEntity<>(yearlyPaymentAmounts, HttpStatus.OK);
    }
}
