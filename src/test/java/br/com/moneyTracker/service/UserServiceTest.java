package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService; // injeto a classe service mockada para usar nos testes

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository; // injeto a unica classe que usamos na nossa service

    User user;
    @BeforeEach // usar quando eu tenho dados que podem ser reaproveitados em todos os testes e devem rodar antes dos testes começarem
    public void setuo(){
        user = new User(1L, "eduardo", "teste@gmail.com", "teste", 10.0);
    }

    @Test
    void changePasswordByUserEmailWithSuccess(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(anyString(), eq(user.getPassword()))).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("SenhaCodificada");

        assertDoesNotThrow(() -> userService.updateUserPassword(user.getEmail(), "newPassword"));
        assertEquals("SenhaCodificada", user.getPassword()); // senhas sejam iguais

        verify(userRepository, times(1)).findByEmail(user.getEmail()); // garante que o metodo seja chamado apenas 1 vez

    }


}
