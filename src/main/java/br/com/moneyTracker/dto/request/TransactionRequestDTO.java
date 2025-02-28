package br.com.moneyTracker.dto.request;

import br.com.moneyTracker.domain.entities.Transactions;

public record TransactionRequestDTO(Long userId, Transactions transaction) {
}
