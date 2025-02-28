package br.com.moneyTracker.controller;

import br.com.moneyTracker.dto.EmailDetails;
import br.com.moneyTracker.dto.request.LoginRequestDTO;
import br.com.moneyTracker.dto.request.RegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
import br.com.moneyTracker.service.EmailService;
import br.com.moneyTracker.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    private final EmailService emailService;

    public LoginController(LoginService loginService, EmailService emailService) {
        this.loginService = loginService;
        this.emailService = emailService;
    }


    @PostMapping("/login")
    public ResponseEntity<DataResponseDTO> loginUser(@RequestBody LoginRequestDTO body) {
        DataResponseDTO response = loginService.loginUser(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponseDTO> registerUser(@RequestBody RegisterRequestDTO body) {
        DataResponseDTO response = loginService.registerUser(body);
        emailService.sendMail(new EmailDetails(body.email(), "User Register", "Usuario cadastrado com sucesso!"));
        return ResponseEntity.ok(response);
    }
}
