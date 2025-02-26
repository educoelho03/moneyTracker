package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;
import br.com.moneyTracker.dto.response.TransactionResponse;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private UserRepository userRepository;

    public TransactionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createNewTransaction(Long userId, Transactions transact) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Transactions transactions = new Transactions(transact.getName(), transact.getValue(), transact.getTransactionType(), transact.getTransactionCategory());
        
        if(transact.getTransactionType() == TRANSACTION_TYPE.DESPESA){
            user.setSaldo(user.getSaldo() - transact.getValue());
        } else if (transact.getTransactionType() == TRANSACTION_TYPE.DEPOSITO){
            user.setSaldo(user.getSaldo() + transact.getValue());
        }

        if (transact.getTransactionType() == TRANSACTION_TYPE.DESPESA && user.getSaldo() - transact.getValue() < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar a transação");
        }

        user.getTransactions().add(transactions);
        userRepository.save(user);

    }

    public List<TransactionResponse> listTransactionsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        return user.getTransactions().stream()
                .map(transactions -> new TransactionResponse(
                        transactions.getName(),
                        transactions.getValue(),
                        transactions.getTransactionType(),
                        transactions.getTransactionCategory(),
                        transactions.getDate()
                )).collect(Collectors.toList());
    }
}
