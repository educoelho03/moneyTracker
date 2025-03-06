package br.com.moneyTracker.dto.request;

import br.com.moneyTracker.domain.entities.Transactions;

public record TransactionRequestDTO(String token, Transactions transaction) {
}
