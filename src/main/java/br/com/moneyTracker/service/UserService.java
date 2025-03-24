package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.AuthRegisterRequestDTO;
import br.com.moneyTracker.exceptions.PasswordNullException;
import br.com.moneyTracker.exceptions.SamePasswordException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
import br.com.moneyTracker.interfaces.AuthServiceInterface;
import br.com.moneyTracker.interfaces.UserServiceInterface;
import br.com.moneyTracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void updateUserPassword(String email, String newPassword) {
        if (email == null) {
            throw new UserNotFoundException("user email cannot be null");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new PasswordNullException("password cannot be null");
        }

        User userRecovery = findUserByEmail(email);
        if(passwordEncoder.matches(newPassword, userRecovery.getPassword())) {
            throw new SamePasswordException("Password must be different");
        }
        userRecovery.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userRecovery);
    }

    @Override
    public User registerUser(AuthRegisterRequestDTO authRegisterRequestDTO){
        findUserByEmail(authRegisterRequestDTO.email());
        User newUser = createNewUser(authRegisterRequestDTO);
        return userRepository.save(newUser);
    }

    public User findUserByEmail(String email) { // TODO: DUVIDA SOBRE COMO APLICAR O SOLID PARA REPOSITORIOS
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with this email : " + email + " not found"));
    }

    private User createNewUser(AuthRegisterRequestDTO authRegisterRequestDTO) {
        return new User(passwordEncoder.encode(authRegisterRequestDTO.password()), authRegisterRequestDTO.email(), authRegisterRequestDTO.name());
    }
}
