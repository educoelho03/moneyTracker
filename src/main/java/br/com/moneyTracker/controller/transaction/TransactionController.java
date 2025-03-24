package br.com.moneyTracker.controller.transaction;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.dto.request.TransactionRequestDTO;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import br.com.moneyTracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/add")
    public ResponseEntity<TransactionResponseDTO> createNewTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequestDTO request) {
        Transactions createdTransaction = transactionService.createNewTransaction(token, request.transaction());

        // Converte a transação criada para TransactionResponseDTO
        TransactionResponseDTO response = TransactionResponseDTO.fromEntity(createdTransaction);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(200).body(transactionService.listTransactionsByToken(token));
    }
}
