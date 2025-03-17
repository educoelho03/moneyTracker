package br.com.moneyTracker.service.transaction;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.domain.enums.TRANSACTION_CATEGORY;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;
import br.com.moneyTracker.dto.request.TransactionRequestDTO;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import br.com.moneyTracker.infra.security.TokenService;
import br.com.moneyTracker.repository.TransactionRepository;
import br.com.moneyTracker.repository.UserRepository;
import br.com.moneyTracker.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    Transactions transaction;
    User user;
    TransactionRequestDTO request;
    String token;

    @BeforeEach
    void setup(){
        user = new User(1L, "eduardo", "teste@gmail.com", "teste", 10.0);
        transaction = new Transactions("Supermercado", 10.0, TRANSACTION_TYPE.DEPOSITO, TRANSACTION_CATEGORY.FOOD, user);
        request = new TransactionRequestDTO(token, transaction);

    }

    @Test // TODO: CORRIGIR ERRO DE NULL NO TOKEN
    void transactionWithSuccess(){
        when(tokenService.generateToken(user)).thenReturn("generatedToken");
        when(userRepository.findByEmail(token)).thenReturn(Optional.of(user));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transactions savedTransaction = transactionService.createNewTransaction("generatedToken", transaction);

        assertNotNull(savedTransaction);
        assertEquals("Supermercado", savedTransaction.getName());
        assertEquals(true, savedTransaction.getTransactionType() == TRANSACTION_TYPE.DEPOSITO);
        assertEquals("generatedToken", token);

        verify(userRepository, times(1)).findByEmail(user.getEmail()); // garante que o metodo seja chamado apenas 1 vez
        verify(transactionRepository, times(1)).save(transaction);
        verify(userRepository, times(1)).save(user);

    }
}
