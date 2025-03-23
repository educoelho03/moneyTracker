package br.com.moneyTracker.repository;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {
    List<TransactionResponseDTO> getAllByUser(User user);
}
