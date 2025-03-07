package br.com.moneyTracker.controller;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.UserRequestDTO;
import br.com.moneyTracker.dto.response.UserResponseDTO;
import br.com.moneyTracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    @GetMapping
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("sucesso!");
    }
}
