package it.unisalento.pas.smartcitywastemanagement.controllers;

import it.unisalento.pas.smartcitywastemanagement.domain.Payment;
import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.dto.UserDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.UserWasteSeparationPerformanceDTO;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import it.unisalento.pas.smartcitywastemanagement.service.MunicipalOfficeService;
import it.unisalento.pas.smartcitywastemanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/municipal-office")
public class MunicipalOfficeController {

    @Autowired
    private MunicipalOfficeService municipalOfficeService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users-to-aware")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<?> getUsersToEducate() {
        List<UserDTO> usersToEducate = municipalOfficeService.identifyCitizensToAware();
        return new ResponseEntity<>(usersToEducate, HttpStatus.OK);
    }

    @GetMapping("/payments-state")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<?> getAllPaymentsWithUserInfo() {
        List<Payment> payments = paymentService.getAllPayments();
        List<Map<String, Object>> paymentsWithUserInfo = new ArrayList<>();

        for (Payment payment : payments) {
            Optional<User> user = userRepository.findById(payment.getUserId());
            Map<String, Object> paymentInfo = new HashMap<>();
            paymentInfo.put("payment", payment);
            paymentInfo.put("user", user.orElse(null)); // Null se l'utente non è trovato
            paymentsWithUserInfo.add(paymentInfo);
        }

        return new ResponseEntity<>(paymentsWithUserInfo, HttpStatus.OK);
    }

    @GetMapping("/waste-separation-performance")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<?> analyzeWasteSeparationPerformance() {
        UserWasteSeparationPerformanceDTO municipalPerformance = municipalOfficeService.analyzeWasteSeparationPerformance();
        return new ResponseEntity<>(municipalPerformance, HttpStatus.OK);
    }

    @GetMapping("/yearly-payment-amounts")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<List<Map<String, Object>>> calculateYearlyPaymentAmountsWithUserInfo() {
        Map<String, Double> yearlyPaymentAmounts = municipalOfficeService.calculateYearlyPaymentAmounts();

        List<Map<String, Object>> yearlyPaymentAmountsWithUserInfo = new ArrayList<>();

        for (Map.Entry<String, Double> entry : yearlyPaymentAmounts.entrySet()) {
            String userId = entry.getKey();
            Double yearlyPaymentAmount = entry.getValue();
            Optional<User> user = userRepository.findById(userId);

            Map<String, Object> paymentInfo = new HashMap<>();
            paymentInfo.put("userId", userId);
            paymentInfo.put("yearlyPaymentAmount", yearlyPaymentAmount);
            paymentInfo.put("user", user.orElse(null));

            yearlyPaymentAmountsWithUserInfo.add(paymentInfo);
        }

        return new ResponseEntity<>(yearlyPaymentAmountsWithUserInfo, HttpStatus.OK);
    }

    @PostMapping("/erogate-payments")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<?> erogatePayments() {
        municipalOfficeService.erogatePayments();
        // Incapsula il messaggio di conferma in un oggetto JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pagamento effettuato con successo");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
