/*package it.unisalento.pas.smartcitywastemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendNotification(String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("destinatario@example.com"); // Indirizzo email del destinatario
        mailMessage.setSubject("Notifica cassonetto pieno");
        mailMessage.setText(message);

        try {
            emailSender.send(mailMessage);
            System.out.println("Email di notifica inviata con successo.");
        } catch (MailException e) {
            System.err.println("Errore durante l'invio dell'email di notifica: " + e.getMessage());
        }
    }
}
*/
