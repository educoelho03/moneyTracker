package br.com.moneyTracker.service.auth;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.AuthLoginRequestDTO;
import br.com.moneyTracker.dto.request.AuthRegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
import br.com.moneyTracker.exceptions.InvalidCredentialsException;
import br.com.moneyTracker.infra.security.TokenService;
import br.com.moneyTracker.repository.UserRepository;
import br.com.moneyTracker.service.AuthService;
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
        assertThat(invalidCredentialsException.getCause(), is(new InvalidCredentialsException("Wrong Password")));

        verify(userRepository, times(1)).findByEmail(authLoginRequestDTO.email());
    }



    @Test
    void registerUserWithSuccess() {
        authRegisterRequestDTO = new AuthRegisterRequestDTO("Eduardo", "teste@gmail.com", "password");

        User newUser = new User();
        newUser.setName(authRegisterRequestDTO.name());
        newUser.setEmail(authRegisterRequestDTO.email());
        newUser.setPassword("encodedPassword");

        when(userRepository.findByEmail(authRegisterRequestDTO.email())).thenReturn(Optional.empty()); // retornar vazio = usuario nao existe(pode ser registrado)
        when(passwordEncoder.encode(authRegisterRequestDTO.password())).thenReturn("encodedPassword");
        when(tokenService.generateToken(user)).thenReturn("generatedToken");
        when(userRepository.save(user)).thenReturn(newUser);

        response = authService.registerUser(authRegisterRequestDTO);

        assertNotNull(response);
        assertNotNull(response.token());
        assertEquals("Eduardo", response.name());
        assertEquals("generatedToken", response.token());

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode(authRegisterRequestDTO.password());
        verify(tokenService, times(1)).generateToken(user);
    }



}
