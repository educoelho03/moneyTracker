package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.AuthLoginRequestDTO;
import br.com.moneyTracker.dto.request.AuthRegisterRequestDTO;
import br.com.moneyTracker.dto.response.DataResponseDTO;
import br.com.moneyTracker.exceptions.InvalidCredentialsException;
import br.com.moneyTracker.exceptions.UserAlreadyExistsException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.infra.security.TokenService;
import br.com.moneyTracker.interfaces.AuthServiceInterface;
import br.com.moneyTracker.interfaces.EmailInterface;
import br.com.moneyTracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceInterface {

    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;

    public AuthService(TokenService tokenService, PasswordEncoder passwordEncoder, UserService userService, EmailService emailService) {
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public DataResponseDTO loginUser(AuthLoginRequestDTO authLoginRequestDTO) { // todo: DUVIDA AQUI, NA SERVICE QUANDO USAR RESPONSE E QUANDO USAR ENTIDADE NO TIPO DE RETORNO DO METODO
        User userRecovery = userService.findUserByEmail(authLoginRequestDTO.email());

        if (!passwordEncoder.matches(authLoginRequestDTO.password(), userRecovery.getPassword())) {
            throw new InvalidCredentialsException("Wrong Password.");
        }

        String token = tokenService.generateToken(userRecovery);
        return new DataResponseDTO(userRecovery.getName(), token);
    }

    @Override
    public DataResponseDTO registerUser(AuthRegisterRequestDTO authRegisterRequestDTO) {
        try {
            User newUser = userService.registerUser(authRegisterRequestDTO);
            String token = tokenService.generateToken(newUser);

            var message = EmailInterface.Message.builder()
                    .to(newUser.getEmail())
                    .subject("🎉 You’re in! Welcome to MoneyTracker.")
                    .body("Hi " + newUser.getName() + ",\n\n" +
                            "Welcome to MoneyTracker – we’re glad to have you on board!\n\n" +
                            "Your account is ready, and you can now start exploring all the features we’ve prepared for you.\n\n" +
                            "Need help? We’re here anytime.\n\n" +
                            "Let’s get started! 🚀\n" +
                            "— The MoneyTracker Team"
                    ).build();

            emailService.sendMail(message);
            return new DataResponseDTO(newUser.getName(), token);
        } catch (Exception e) {
            throw new UserNotFoundException("Error to register user.", e);
        }
    }


}
