package br.com.moneyTracker.repository;

import br.com.moneyTracker.domain.entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transactions, Integer> {
}
