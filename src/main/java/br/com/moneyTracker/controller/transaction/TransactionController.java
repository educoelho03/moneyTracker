package br.com.moneyTracker.controller.transaction;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.dto.request.TransactionRequestDTO;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import br.com.moneyTracker.infra.security.SecurityConfig;
import br.com.moneyTracker.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
@Tag(name = "Transações", description = "Controller responsável pelo gerenciamento de transações financeiras")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/add")
    @Operation(summary = "Criar nova transação",
            description = "Endpoint para registrar uma nova transação financeira")
    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da transação inválidos")
    @ApiResponse(responseCode = "401", description = "Não autorizado - token inválido")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<TransactionResponseDTO> createNewTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequestDTO request) {
        Transactions createdTransaction = transactionService.createNewTransaction(token, request.transaction());

        // Converte a transação criada para TransactionResponseDTO
        TransactionResponseDTO response = TransactionResponseDTO.fromEntity(createdTransaction);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping()
    @Operation(summary = "Listar todas as transações",
            description = "Endpoint para recuperar todas as transações do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autorizado - token inválido")
    @ApiResponse(responseCode = "404", description = "Nenhuma transação encontrada")
    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(200).body(transactionService.listTransactionsByToken(token));
    }
}
