package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.AuthLoginRequestDTO;
import br.com.moneyTracker.dto.request.AuthRegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
import br.com.moneyTracker.exceptions.InvalidCredentialsException;
import br.com.moneyTracker.exceptions.UserAlreadyExistsException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.infra.security.TokenService;
import br.com.moneyTracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public DataResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO) { // todo: DUVIDA AQUI, NA SERVICE QUANDO USAR RESPONSE E QUANDO USAR ENTIDADE NO TIPO DE RETORNO DO METODO
        User user = userRepository.findByEmail(authLoginRequestDTO.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(authLoginRequestDTO.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Wrong Password.");
        }

        String token = tokenService.generateToken(user);
        return new DataResponseDTO(user.getName(), token);
    }

    public DataResponseDTO registerUser(AuthRegisterRequestDTO authRegisterRequestDTO) {
        if (userRepository.findByEmail(authRegisterRequestDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(authRegisterRequestDTO.password()));
        newUser.setEmail(authRegisterRequestDTO.email());
        newUser.setName(authRegisterRequestDTO.name());

        String token = tokenService.generateToken(newUser);
        // newUser.setToken(token); // Define o token no usuário

        userRepository.save(newUser); // Salva o usuário no banco
        return new DataResponseDTO(newUser.getName(), token);
    }
}
