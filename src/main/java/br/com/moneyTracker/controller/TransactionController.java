package br.com.moneyTracker.controller;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.dto.request.TransactionRequestDTO;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import br.com.moneyTracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/add")
    public ResponseEntity<TransactionResponseDTO> createNewTransaction(@RequestBody TransactionRequestDTO request) {
        Transactions createdTransaction = transactionService.createNewTransaction(request.token(), request.transaction());

        // Converte a transação criada para TransactionResponseDTO
        TransactionResponseDTO response = TransactionResponseDTO.fromEntity(createdTransaction);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping() // TODO: CORRIGIR ERRO 403 NO METODO
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@RequestHeader("Authorization") String token) {
        return ResponseEntity.status(200).body(transactionService.listTransactionsByToken(token));
    }
}
