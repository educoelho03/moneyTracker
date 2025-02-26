package br.com.moneyTracker.controller;

import br.com.moneyTracker.dto.response.TransactionResponse;
import br.com.moneyTracker.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(Long userId) {
        return ResponseEntity.status(200).body(transactionService.listTransactionsByUserId(userId));
    }
}
