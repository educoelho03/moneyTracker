package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.UserRequestDTO;
import br.com.moneyTracker.dto.response.UserResponseDTO;
import br.com.moneyTracker.exceptions.*;
import br.com.moneyTracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    public void updateUserPassword(String email, String newPassword) {
       User userRecovery = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user with this email :" + email + " not found"));

       if(passwordEncoder.matches(newPassword, userRecovery.getPassword())) {
           throw new SamePasswordException("Password must be different");
       }

       userRecovery.setPassword(passwordEncoder.encode(newPassword));
       userRepository.save(userRecovery);
    }


    public User createUser(UserRequestDTO request){
        if(!isValidEmail(request.email())){
            throw new InvalidEmailException("E-mail invalido.");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistException("E-mail já cadastrado.");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); // criptografar senha
        user.setSaldo(0);

        User savedUser = userRepository.save(user);
        return savedUser;
    }

}
