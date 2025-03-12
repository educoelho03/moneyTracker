package br.com.moneyTracker.controller;

import br.com.moneyTracker.dto.EmailDetails;
import br.com.moneyTracker.dto.request.EmailRequestDTO;
import br.com.moneyTracker.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
       try {
            String result = emailService.sendPasswordResetEmail(emailRequestDTO.email());
            return ResponseEntity.status(HttpStatus.OK).body(result);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");

       }

    }
}
