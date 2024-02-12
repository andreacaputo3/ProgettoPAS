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
import org.springframework.web.bind.annotation.*;

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
        try {
            List<UserDTO> usersToEducate = municipalOfficeService.identifyCitizensToAware();
            if (!usersToEducate.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("usersToAwareData", usersToEducate);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Nessun cittadino da sensibilizzare al momento.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore durante il recupero degli utenti da sensibilizzare: " + e.getMessage());
        }
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
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pagamento effettuato con successo");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/mark-user-as-awared/{id}")
    @PreAuthorize("hasRole('ADMIN_UFFICIO')")
    public ResponseEntity<?> markUserAsAwared(@PathVariable String id) {

        if (!userRepository.existsById(id)) {
            return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setAwared(true);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
