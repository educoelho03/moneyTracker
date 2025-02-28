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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO request){
        User createdUser = userService.createUser(request);
        UserResponseDTO userResponse = UserResponseDTO.fromEntity(createdUser);
        return ResponseEntity.status(201).body(userResponse);
    }

    @GetMapping
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("sucesso!");
    }
}
