package br.com.moneyTracker.dto.response;

import br.com.moneyTracker.domain.enums.TRANSACTION_CATEGORY;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;

import java.time.LocalDate;

public record TransactionResponse(
        String name,
        double value,
        TRANSACTION_TYPE transactionType,
        TRANSACTION_CATEGORY transactionCategory,
        LocalDate date
) {
}
