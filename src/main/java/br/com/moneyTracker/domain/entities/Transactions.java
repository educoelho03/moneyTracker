package br.com.moneyTracker.domain.entities;

import br.com.moneyTracker.domain.enums.TRANSACTION_CATEGORY;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tb_transactions")
@Getter
@Setter
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_id;

    private String name;
    private double amount;
    private TRANSACTION_TYPE transactionType;
    private TRANSACTION_CATEGORY transactionCategory;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; 

    public Transactions(String name, double amount, TRANSACTION_TYPE transactionType, TRANSACTION_CATEGORY transactionCategory, User user) {
        this.name = name;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.date = LocalDate.now();
        this.user = user;
    }

    public Transactions() {
        this.date = LocalDate.now(); // quando o spring deserializa o json ele usa o construtor padrao
    }

}
