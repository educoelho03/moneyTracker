package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.LoginRequestDTO;
import br.com.moneyTracker.dto.request.RegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
import br.com.moneyTracker.exceptions.InvalidCredentialsException;
import br.com.moneyTracker.exceptions.UserAlreadyExistsException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.infra.security.TokenService;
import br.com.moneyTracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public DataResponseDTO loginUser(LoginRequestDTO loginRequestDTO) { // todo: DUVIDA AQUI, NA SERVICE QUANDO USAR RESPONSE E QUANDO USAR ENTIDADE NO TIPO DE RETORNO DO METODO
        User user = userRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = tokenService.generateToken(user);
        return new DataResponseDTO(user.getName(), token);
    }

    public DataResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByEmail(registerRequestDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        newUser.setEmail(registerRequestDTO.email());
        newUser.setName(registerRequestDTO.name());

        String token = tokenService.generateToken(newUser);
        newUser.setToken(token); // Define o token no usuário

        userRepository.save(newUser); // Salva o usuário no banco
        return new DataResponseDTO(newUser.getName(), token);
    }
}
