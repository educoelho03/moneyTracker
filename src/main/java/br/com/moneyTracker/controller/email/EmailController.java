package br.com.moneyTracker.controller.email;

import br.com.moneyTracker.dto.request.EmailRequestDTO;
import br.com.moneyTracker.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@Tag(name = "Email", description = "Controller responsável pelo envio de e-mails do sistema")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    @Operation(summary = "Envia e-mail de redefinição de senha",
            description = "Envia um e-mail com link para redefinição de senha para o endereço fornecido")
    @ApiResponse(responseCode = "200", description = "E-mail enviado com sucesso")
    @ApiResponse(responseCode = "400", description = "Endereço de e-mail inválido ou não encontrado")
    @ApiResponse(responseCode = "500", description = "Falha ao enviar o e-mail")
    public ResponseEntity<String> sendEmailPasswordReset(@RequestBody EmailRequestDTO emailRequestDTO) {
       try {
            String result = emailService.sendPasswordResetEmail(emailRequestDTO.email());
            return ResponseEntity.status(HttpStatus.OK).body(result);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");

       }

    }
}
