package br.com.moneyTracker.service.auth;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.AuthLoginRequestDTO;
import br.com.moneyTracker.dto.request.AuthRegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
import br.com.moneyTracker.exceptions.InvalidCredentialsException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.infra.security.TokenService;
import br.com.moneyTracker.repository.UserRepository;
import br.com.moneyTracker.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;


    @Mock
    private PasswordEncoder passwordEncoder;

    User user;
    AuthLoginRequestDTO authLoginRequestDTO;
    AuthRegisterRequestDTO authRegisterRequestDTO;
    DataResponseDTO response;

    @BeforeEach
    void setUp() {
        authLoginRequestDTO = new AuthLoginRequestDTO("teste@gmail.com", "password");
        user = new User();
        user.setUser_id(1L);
        user.setName("Eduardo");
        user.setPassword("password");
        user.setEmail("teste@gmail.com");
        user.setSaldo(10.0);
    }

    @Test
    void loginUserWithSuccess(){
        when(userRepository.findByEmail(authLoginRequestDTO.email())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(authLoginRequestDTO.password(), user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("generatedToken");

        response = authService.loginUser(authLoginRequestDTO);

        assertNotNull(response);
        assertNotNull(response.token());
        assertEquals("Eduardo", user.getName());
        assertEquals("generatedToken", response.token());

        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void callExceptionPasswordsBeDifferent(){
        when(userRepository.findByEmail(authLoginRequestDTO.email())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(authLoginRequestDTO.password(), user.getPassword())).thenReturn(false);

        final InvalidCredentialsException invalidCredentialsException = assertThrows(InvalidCredentialsException.class, () -> authService.loginUser(authLoginRequestDTO));
        assertThat(invalidCredentialsException, notNullValue());
        assertThat(invalidCredentialsException.getMessage(), is("Wrong Password."));
        assertThat(invalidCredentialsException.getCause(), nullValue());

        verify(userRepository, times(1)).findByEmail(authLoginRequestDTO.email());
    }


    @Test
    void registerUserWithSuccess() {
        // Arrange
        authRegisterRequestDTO = new AuthRegisterRequestDTO("Eduardo", "teste@gmail.com", "password");

        // Configura os mocks
        when(userRepository.findByEmail(authRegisterRequestDTO.email())).thenReturn(Optional.empty()); // Usuário não existe
        when(passwordEncoder.encode(authRegisterRequestDTO.password())).thenReturn("encodedPassword"); // Simula a codificação da senha
        when(tokenService.generateToken(ArgumentMatchers.any(User.class))).thenReturn("generatedToken"); // Aceita qualquer User
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Retorna o User salvo

        // Act
        response = authService.registerUser(authRegisterRequestDTO);

        // Assert
        assertNotNull(response);
        assertNotNull(response.token());
        assertEquals("Eduardo", response.name());
        assertEquals("generatedToken", response.token());

        // Verifica as interações com os mocks
        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class)); // Verifica se o User foi salvo
        verify(passwordEncoder, times(1)).encode(authRegisterRequestDTO.password()); // Verifica se a senha foi codificada
        verify(tokenService, times(1)).generateToken(ArgumentMatchers.any(User.class)); // Verifica se o token foi gerado
    }

    @Test
    void callExceptionWhenRepositoryFailed() {
        // Arrange
        authRegisterRequestDTO = new AuthRegisterRequestDTO("Eduardo", "teste@gmail.com", "password");
        when(userRepository.findByEmail(authRegisterRequestDTO.email())).thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> {
            authService.registerUser(authRegisterRequestDTO);
        });

        // Verifica a exceção
        assertThat(userNotFoundException, notNullValue()); // Verifica se a exceção foi lançada
        assertThat(userNotFoundException.getMessage(), is("Error to register user.")); // Verifica a mensagem da exceção
        assertThat(userNotFoundException.getCause(), notNullValue()); // Verifica se a causa não é nula
        assertThat(userNotFoundException.getCause().getClass(), is(RuntimeException.class)); // Verifica o tipo da causa
        assertThat(userNotFoundException.getCause().getMessage(), is("User not found")); // Verifica a mensagem da causa

        // Verifica as interações com os mocks
        verify(userRepository, times(1)).findByEmail(authRegisterRequestDTO.email());
        verifyNoMoreInteractions(userRepository);
    }

}
