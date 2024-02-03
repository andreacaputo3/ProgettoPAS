package it.unisalento.pas.smartcitywastemanagement.controllers;

import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.dto.UserDTO;
import it.unisalento.pas.smartcitywastemanagement.security.JwtUtilities;
import it.unisalento.pas.smartcitywastemanagement.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private JwtUtilities jwtUtilities;

    @PostMapping("/register-company-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerAziendaAdmin(@RequestBody UserDTO adminDTO) {
        try {
            adminDTO.setRole("ADMIN_AZIENDA");
            User registeredAdmin = userService.registerNewAdmin(adminDTO);
            var jwtToken = jwtUtilities.generateToken(registeredAdmin.getUsername());
            return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la registrazione dell'amministratore aziendale: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register-office-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerUfficioAdmin(@RequestBody UserDTO adminDTO) {
        try {
            adminDTO.setRole("ADMIN_UFFICIO");
            User registeredAdmin = userService.registerNewAdmin(adminDTO);
            var jwtToken = jwtUtilities.generateToken(registeredAdmin.getUsername());
            return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la registrazione dell'amministratore d'ufficio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
