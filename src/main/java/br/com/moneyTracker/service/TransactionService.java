package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import br.com.moneyTracker.exceptions.InvalidTokenException;
import br.com.moneyTracker.exceptions.SaldoInsuficienteException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.infra.security.TokenService;
import br.com.moneyTracker.repository.TransactionRepository;
import br.com.moneyTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TokenService tokenService;
    private final UserService userService;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository , TransactionRepository transactionRepository, TokenService tokenService, UserService userService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    public List<TransactionResponseDTO> listTransactionsByToken(String token) { // TODO: DUVIDA AQUI, É UMA BOA PRATICA FAZER A CONVERSAO DE ENTITY PARA RESPONSE DENTRO DA SERVICE?
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Invalid token");
        }

        String userEmail = tokenService.validateToken(token);

        if (userEmail == null || userEmail.isEmpty()) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        User user = userService.findUserByEmail(userEmail);

        return user.getTransactions().stream()
                .map(transactions -> new TransactionResponseDTO(
                        transactions.getName(),
                        transactions.getAmount(),
                        transactions.getTransactionType(),
                        transactions.getTransactionCategory(),
                        transactions.getDate()
                )).collect(Collectors.toList());
    }

    public Transactions createNewTransaction(String token, Transactions transaction) {
        String userEmail = tokenService.validateToken(token); // TODO: REVER ESSE PONTO
        User user = userService.findUserByEmail(userEmail);

        // Associa a transação ao usuário
        transaction.setUser(user);

        calculateAmount(user, transaction);
        Transactions savedTransaction = transactionRepository.save(transaction);
        userRepository.save(user);

        return savedTransaction;
    }

    public void calculateAmount(User user, Transactions transaction) {
        if (transaction.getTransactionType() == TRANSACTION_TYPE.DESPESA) {
            subtractToBalance(user, transaction.getAmount());
        } else if (transaction.getTransactionType() == TRANSACTION_TYPE.DEPOSITO) {
            addToBalance(user, transaction.getAmount());
        }
    }

    private void addToBalance(User user, double amount){
        double newBalance = user.getSaldo() + amount;
        user.setSaldo(newBalance);
    }

    private void subtractToBalance(User user, double amount){
        double newBalance = user.getSaldo() - amount;
        if(newBalance < 0){
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transação.");
        }
        user.setSaldo(newBalance);
    }
}
