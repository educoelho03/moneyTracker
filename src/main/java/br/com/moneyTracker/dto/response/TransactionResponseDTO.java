package br.com.moneyTracker.dto.response;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.domain.enums.TRANSACTION_CATEGORY;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;

import java.time.LocalDate;

public record TransactionResponseDTO(
        String name,
        double amount,
        TRANSACTION_TYPE transactionType,
        TRANSACTION_CATEGORY transactionCategory,
        LocalDate date) {

    public static TransactionResponseDTO fromEntity(Transactions transaction) { // converte para DTO
        return new TransactionResponseDTO(
                transaction.getName(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTransactionCategory(),
                transaction.getDate()
        );
    }

}
