package br.com.moneyTracker.controller;

import br.com.moneyTracker.dto.request.UserRequestDTO;
import br.com.moneyTracker.dto.response.UserResponseDTO;
import br.com.moneyTracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request){
        UserResponseDTO response = userService.createUser(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("sucesso!");
    }
}
