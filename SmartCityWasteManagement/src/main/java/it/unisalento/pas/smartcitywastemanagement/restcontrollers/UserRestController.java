package it.unisalento.pas.smartcitywastemanagement.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.domain.Payment;
import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.dto.LoginDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.UserDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.UserWasteSeparationPerformanceDTO;
import it.unisalento.pas.smartcitywastemanagement.exceptions.UserNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import it.unisalento.pas.smartcitywastemanagement.security.JwtUtilities;
import it.unisalento.pas.smartcitywastemanagement.service.CustomUserDetailsService;
import it.unisalento.pas.smartcitywastemanagement.service.PaymentService;
import it.unisalento.pas.smartcitywastemanagement.service.WasteSeparationPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WasteSeparationPerformanceService wasteSeparationPerformanceService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilities jwtUtilities;


    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "PROVA");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN_UFFICIO')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAllByRole("USER"); // Filtra gli utenti per ruolo "USER"
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return new ResponseEntity<>(convertToDTO(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ADMIN_UFFICIO')")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
        }

        // Elimino utente dal repository
        userRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Utente eliminato con successo");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // Verifica se l'username è già in uso
            if (userRepository.existsByUsername(userDTO.getUsername())) {
                return new ResponseEntity<>("Registrazione fallita: Username già in uso", HttpStatus.BAD_REQUEST);
            }

            User registeredUser = customUserDetailsService.registerNewUser(userDTO);
            // Verifica se l'utente è stato registrato correttamente
            if (registeredUser == null) {
                return new ResponseEntity<>("Registrazione fallita: impossibile ottenere l'utente registrato", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Genera il token JWT per l'utente registrato
            var jwtToken = jwtUtilities.generateToken(registeredUser.getUsername());
            // Costruisci la mappa per includere il token JWT nella risposta
            HashMap<String, String> map = new HashMap<>();
            map.put("token", jwtToken);
            map.put("id", registeredUser.getId());
            map.put("username", registeredUser.getUsername());

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Gestisci eventuali eccezioni durante la registrazione
            return new ResponseEntity<>("Registrazione fallita: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
            User authenticatedUser = userRepository.findByUsername(authentication.getName());

            if (authenticatedUser != null) {
                var jwtToken = jwtUtilities.generateToken(authenticatedUser.getUsername());
                HashMap<String, String> map = new HashMap<>();
                map.put("token", jwtToken);
                map.put("id", authenticatedUser.getId());
                map.put("username", authenticatedUser.getUsername());
                map.put("role", authenticatedUser.getRole());
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                throw new UserNotFoundException(loginDTO.getUsername());
            }
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Login non riuscito: credenziali non valide", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}/payments")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getAllPaymentsByUserId(@PathVariable String id) {
        List<Payment> payments = paymentService.getAllPaymentsByUserId(id);
        if (payments.isEmpty()) {
            return new ResponseEntity<>("Nessun pagamento disponibile", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @PostMapping("/{id}/payments/{paymentId}/pay")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> payPayment(@PathVariable String id, @PathVariable String paymentId) {
        paymentService.payPayment(id, paymentId);

        // Incapsula il messaggio di conferma in un oggetto JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pagamento effettuato con successo");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/waste-separation-performance")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> getUserWasteSeparationPerformance(@PathVariable String id) {
        // Verifica se l'utente esiste nel sistema
        if (!userRepository.existsById(id)) {
            return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
        }

        // Calcola le prestazioni di separazione dei rifiuti per l'utente specificato
        UserWasteSeparationPerformanceDTO performanceDTO = wasteSeparationPerformanceService.calculateUserWasteSeparationPerformance(id);

        // Verifica se le prestazioni sono disponibili
        if (performanceDTO == null) {
            return new ResponseEntity<>("Prestazioni non disponibili per l'utente", HttpStatus.NOT_FOUND);
        }

        // Restituisci le prestazioni di separazione dei rifiuti
        return new ResponseEntity<>(performanceDTO, HttpStatus.OK);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setMail(user.getMail());
        userDTO.setAge(user.getAge());
        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        userDTO.setIncorrectDisposalCount(user.getIncorrectDisposalCount());
        userDTO.setAwared(user.getAwared());
        return userDTO;
    }
}
