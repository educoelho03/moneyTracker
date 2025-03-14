package br.com.moneyTracker.service.user;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.exceptions.PasswordNullException;
import br.com.moneyTracker.exceptions.SamePasswordException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.repository.UserRepository;
import br.com.moneyTracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void notCallRepositoryIfEmailIsNull(){
        final UserNotFoundException emailException = assertThrows(UserNotFoundException.class, () -> userService.updateUserPassword(null, "newPassword"));

        assertThat(emailException, notNullValue()); // Verifica se a exceção não é nula
        assertThat(emailException.getMessage(), is("user email cannot be null")); // verifica se a mensagem de erro é essa
        assertThat(emailException.getCause(), nullValue()); // verifica se a causa é dada por valor nule

        // Verifica se o repositório não foi chamado
        verifyNoInteractions(userRepository);
    }

    @Test
    void notCallRepositoryIfNewPasswordIsNullOrEmpty(){
        final PasswordNullException passwordException = assertThrows(PasswordNullException.class, () -> userService.updateUserPassword("teste@gmail.com", null));

        assertThat(passwordException, notNullValue()); // verifica se a exceção nao é nula
        assertThat(passwordException.getMessage(), is("password cannot be null"));
        assertThat(passwordException.getCause(), nullValue());

        verifyNoInteractions(userRepository);
    }

    @Test
    void notCallRepositoryIfNewPasswordIsEqualTheOldPassword(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq(user.getPassword()))).thenReturn(true);
        final SamePasswordException samePasswordException = assertThrows(SamePasswordException.class, () -> userService.updateUserPassword(user.getEmail(), "teste"));

        assertThat(samePasswordException, notNullValue()); // verifica se a execução é nula
        assertThat(samePasswordException.getMessage(), is("Password must be different"));
        assertThat(samePasswordException.getCause(), nullValue());

        verify(userRepository, times(1)).findByEmail(user.getEmail());

    }

    //@Test
    //void callExceptionWhenRepositoryFailed() {
    //    when(userRepository.findByEmail(user.getEmail()))
    //            .thenThrow(new RuntimeException("Erro to search user by this email"));

    //    final UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userService.updateUserPassword(user.getEmail(), "newPassword"));

    //    // Verifica a mensagem correta da exceção principal
    //    assertThat(userNotFoundException.getMessage(), is("Error to search Email = " + user.getEmail()));

    //    // Verifica que a causa da exceção é RuntimeException
    //    assertThat(userNotFoundException.getCause(), instanceOf(RuntimeException.class));

    //    // Verifica a mensagem da exceção original (causa)
    //    assertThat(userNotFoundException.getCause().getMessage(), is("Erro to search user by this email"));

    //    verify(userRepository, times(1)).findByEmail(user.getEmail());
    //    verifyNoMoreInteractions(userRepository);
    //}

}
