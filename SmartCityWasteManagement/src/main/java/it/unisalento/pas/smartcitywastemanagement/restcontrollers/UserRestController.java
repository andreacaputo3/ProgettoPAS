package it.unisalento.pas.smartcitywastemanagement.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.dto.LoginDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.PaymentDTO;
import it.unisalento.pas.smartcitywastemanagement.dto.UserDTO;
import it.unisalento.pas.smartcitywastemanagement.exceptions.UserNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import it.unisalento.pas.smartcitywastemanagement.security.JwtUtilities;
import it.unisalento.pas.smartcitywastemanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilities jwtUtilities;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return new ResponseEntity<>("Username già in uso", HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setNome(userDTO.getNome());
        newUser.setCognome(userDTO.getCognome());
        newUser.setEmail(userDTO.getEmail());
        newUser.setEta(userDTO.getEta());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRuolo(userDTO.getRuolo());

        userRepository.save(newUser);

        var jwtToken = jwtUtilities.generateToken(newUser.getUsername());

        return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
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
                return new ResponseEntity<>(jwtToken, HttpStatus.OK);
            } else {
                throw new UserNotFoundException(loginDTO.getUsername());
            }
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Login non riuscito: credenziali non valide", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{userId}/payments")
    public ResponseEntity<List<PaymentDTO>> getUserPayments(@PathVariable String userId) {
        List<PaymentDTO> paymentDTOs = paymentService.getPaymentsForUser(userId).stream()
                .map(payment -> new PaymentDTO(payment.getId(), payment.getAmount(), payment.getPaymentDate(), payment.isPaid()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(paymentDTOs, HttpStatus.OK);
    }

    @PostMapping("/payments/{paymentId}/pay")
    public ResponseEntity<String> payPayment(@PathVariable String paymentId) {
        boolean paymentStatus = paymentService.payPayment(paymentId);
        if (paymentStatus) {
            return new ResponseEntity<>("Pagamento effettuato con successo", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Pagamento non riuscito", HttpStatus.BAD_REQUEST);
        }
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNome(user.getNome());
        userDTO.setCognome(user.getCognome());
        userDTO.setEmail(user.getEmail());
        userDTO.setEta(user.getEta());
        userDTO.setUsername(user.getUsername());
        userDTO.setRuolo(user.getRuolo());
        return userDTO;
    }
}
