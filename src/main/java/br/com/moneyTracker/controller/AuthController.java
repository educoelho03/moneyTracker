package br.com.moneyTracker.controller;

import br.com.moneyTracker.dto.request.AuthLoginRequestDTO;
import br.com.moneyTracker.dto.request.AuthRegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
import br.com.moneyTracker.service.EmailService;
import br.com.moneyTracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }


    @PostMapping("/login")
    public ResponseEntity<DataResponseDTO> loginUser(@RequestBody AuthLoginRequestDTO body) {
        DataResponseDTO response = authService.loginUser(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponseDTO> registerUser(@RequestBody AuthRegisterRequestDTO body) {
        DataResponseDTO response = authService.registerUser(body);
        //emailService.sendMail(new EmailDetails(body.email(), "Money Tracker - Conta criada", "Usuario cadastrado com sucesso!"));
        return ResponseEntity.ok(response);
    }
}
