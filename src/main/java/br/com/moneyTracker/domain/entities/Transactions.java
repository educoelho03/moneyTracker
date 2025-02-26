package br.com.moneyTracker.domain.entities;

import br.com.moneyTracker.domain.enums.TRANSACTION_CATEGORY;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trasaction_id;

    private String name;
    private double value;
    private TRANSACTION_TYPE transactionType;
    private TRANSACTION_CATEGORY transactionCategory;
    private LocalDate date;

    public Transactions(String name, double value, TRANSACTION_TYPE transactionType, TRANSACTION_CATEGORY transactionCategory) {
        this.name = name;
        this.value = value;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.date = LocalDate.now();
    }

    public Transactions() {

    }
}
