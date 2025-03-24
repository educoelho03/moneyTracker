package br.com.moneyTracker.controller.user;

import br.com.moneyTracker.dto.request.ResetPasswordRequestDTO;
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

    @GetMapping
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("sucesso!");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> updatePassword(@RequestBody ResetPasswordRequestDTO passwordRequestDTO){
        try {
            if(!passwordRequestDTO.newPassword().equals(passwordRequestDTO.confirmNewPassword())){
                return ResponseEntity.badRequest().body("A nova senha e a confirmação não coincidem.");
            }
            userService.updateUserPassword(passwordRequestDTO.email(), passwordRequestDTO.newPassword());
            return ResponseEntity.ok("Senha atualizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
