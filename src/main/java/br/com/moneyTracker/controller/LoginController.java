package br.com.moneyTracker.controller;

import br.com.moneyTracker.dto.request.LoginRequestDTO;
import br.com.moneyTracker.dto.request.RegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
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

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/login")
    public ResponseEntity<DataResponseDTO> loginUser(@RequestBody LoginRequestDTO body) {
        DataResponseDTO response = loginService.loginUser(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<DataResponseDTO> registerUser(@RequestBody RegisterRequestDTO body) {
        DataResponseDTO response = loginService.registerUser(body);
        return ResponseEntity.ok(response);
    }
}
