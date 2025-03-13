package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.exceptions.SamePasswordException;
import br.com.moneyTracker.exceptions.UserNotFoundException;
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

    public void updateUserPassword(String email, String newPassword) {
       User userRecovery = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user with this email :" + email + " not found"));

       if(passwordEncoder.matches(newPassword, userRecovery.getPassword())) {
           throw new SamePasswordException("Password must be different");
       }

       userRecovery.setPassword(passwordEncoder.encode(newPassword));
       userRepository.save(userRecovery);
    }
}
