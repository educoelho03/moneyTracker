package br.com.moneyTracker.service.transaction;

import br.com.moneyTracker.domain.entities.Transactions;
import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.domain.enums.TRANSACTION_CATEGORY;
import br.com.moneyTracker.domain.enums.TRANSACTION_TYPE;
import br.com.moneyTracker.dto.request.TransactionRequestDTO;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import br.com.moneyTracker.exceptions.InvalidTokenException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
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
    String token;

    @BeforeEach
    void setup(){
        user = new User(1L, "eduardo", "teste@gmail.com", "teste", 10.0);
        transaction = new Transactions("Supermercado", 10.0, TRANSACTION_TYPE.DEPOSITO, TRANSACTION_CATEGORY.FOOD, user);
        user.setTransactions(new ArrayList<>(List.of(transaction)));
        token = "validToken";
    }

    @Test
    void addNewTransactionWithSuccess() {
        when(tokenService.validateToken(token)).thenReturn("teste@gmail.com"); // Simula a validação do token
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user)); // Simula a busca do usuário
        when(transactionRepository.save(transaction)).thenReturn(transaction); // Simula a criação da transação

        Transactions savedTransactions = transactionService.createNewTransaction(token, transaction);

        assertNotNull(savedTransactions);
        assertNotNull(token);
        assertEquals("Supermercado", savedTransactions.getName());
        assertEquals(10.0, savedTransactions.getAmount());
        assertEquals(TRANSACTION_TYPE.DEPOSITO, savedTransactions.getTransactionType());
        assertEquals(TRANSACTION_CATEGORY.FOOD, savedTransactions.getTransactionCategory());
        assertEquals(user, savedTransactions.getUser());
        assertEquals(20.0, user.getSaldo() + savedTransactions.getAmount());

        verify(tokenService, times(1)).validateToken(token); // Verifica se o token foi validado
        verify(userRepository, times(1)).findByEmail("teste@gmail.com"); // Verifica se o usuário foi buscado pelo email
        verify(transactionRepository, times(1)).save(transaction); // Verifica se a transação foi salva
        verify(userRepository, times(1)).save(user); // Verifica se o usuário foi salvo
    }

    @Test
    void callExceptionWhenUserEmailNotFound() {
        String nonExistentEmail = "notExist@gmail.com";
        when(tokenService.validateToken(token)).thenReturn(nonExistentEmail);
        final UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> {transactionService.createNewTransaction(token, transaction);});

        assertThat(userNotFoundException, notNullValue());
        assertThat(userNotFoundException.getMessage(), is("User with this email not found")); // verifica se a mensagem de erro é essa
        assertThat(userNotFoundException.getCause(), nullValue()); // verifica se a causa é dada por valor nule
    }

    @Test
    void listTransactionsWithSuccess() {
        // Cria a segunda transação e adiciona ao usuário
        Transactions secondTransaction = new Transactions("Gasolina", 100.0, TRANSACTION_TYPE.DESPESA, TRANSACTION_CATEGORY.TRANSPORT, user);
        user.getTransactions().add(secondTransaction); // Adiciona a segunda transação à lista

        // Mock do tokenService
        when(tokenService.validateToken(token)).thenReturn("teste@gmail.com");

        // Mock do userRepository
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user)); // Simula a busca do usuário

        List<TransactionResponseDTO> result = transactionService.listTransactionsByToken(token);

        assertNotNull(result); // Verifica se a lista não é nula
        assertEquals(2, result.size()); // Verifica o número de transações retornadas

        assertEquals("Supermercado", result.get(0).name());
        assertEquals(10.0, result.get(0).amount());
        assertEquals(TRANSACTION_TYPE.DEPOSITO, result.get(0).transactionType());
        assertEquals(TRANSACTION_CATEGORY.FOOD, result.get(0).transactionCategory());

        assertEquals("Gasolina", result.get(1).name());
        assertEquals(100.0, result.get(1).amount());
        assertEquals(TRANSACTION_TYPE.DESPESA, result.get(1).transactionType());
        assertEquals(TRANSACTION_CATEGORY.TRANSPORT, result.get(1).transactionCategory());

        verify(tokenService, times(1)).validateToken(token);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
}
