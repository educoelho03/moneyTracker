package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import br.com.moneyTracker.exceptions.SaldoInsuficienteException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.infra.security.TokenService;
import br.com.moneyTracker.repository.TransactionRepository;
import br.com.moneyTracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TokenService tokenService;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository , TransactionRepository transactionRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.tokenService = tokenService;
    }

    public Transactions createNewTransaction(String token, Transactions transaction) {
        String userEmail = tokenService.validateToken(token);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("UserEmail not found."));

        // Associa a transação ao usuário
        transaction.setUser(user);

        if (transaction.getTransactionType() == TRANSACTION_TYPE.DESPESA) {
            user.setSaldo(user.getSaldo() - transaction.getAmount());
        } else if (transaction.getTransactionType() == TRANSACTION_TYPE.DEPOSITO) {
            user.setSaldo(user.getSaldo() + transaction.getAmount());
        }

        Transactions savedTransaction = transactionRepository.save(transaction);
        userRepository.save(user);

        // Retorna a transação criada
        return savedTransaction;
    }

    public List<TransactionResponseDTO> listTransactionsByToken(String token) { // TODO: DUVIDA AQUI, É UMA BOA PRATICA FAZER A CONVERSAO DE ENTITY PARA RESPONSE DENTRO DA SERVICE?
        User user = userRepository.findByToken(token).orElseThrow(() -> new UserNotFoundException("User not service found."));

        return user.getTransactions().stream()
                .map(transactions -> new TransactionResponseDTO(
                        transactions.getName(),
                        transactions.getAmount(),
                        transactions.getTransactionType(),
                        transactions.getTransactionCategory(),
                        transactions.getDate()
                )).collect(Collectors.toList());
    }
}
