package br.com.moneyTracker.controller.auth;

import br.com.moneyTracker.dto.request.AuthLoginRequestDTO;
import br.com.moneyTracker.dto.request.AuthRegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
import br.com.moneyTracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Autenticação", description = "Controller responsável por autenticar e registrar os usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    @Operation(summary = "Realiza o login do usuario", description = "Método para logar o usuario na pagina.")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou erro na requisição")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<DataResponseDTO> loginUser(@RequestBody AuthLoginRequestDTO body) {

        DataResponseDTO response = authService.loginUser(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário", description = "Método para cadastrar um novo usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário já existente")
    @ApiResponse(responseCode = "500", description = "Erro no servidor")
    public ResponseEntity<DataResponseDTO> registerUser(@RequestBody AuthRegisterRequestDTO body) {
        DataResponseDTO response = authService.registerUser(body);
        return ResponseEntity.ok(response);
    }
}
