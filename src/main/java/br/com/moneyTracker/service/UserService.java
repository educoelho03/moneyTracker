package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.UserRequestDTO;
import br.com.moneyTracker.dto.response.UserResponseDTO;
import br.com.moneyTracker.exceptions.CpfAlreadyExistException;
import br.com.moneyTracker.exceptions.EmailAlreadyExistException;
import br.com.moneyTracker.exceptions.InvalidEmailException;
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

    public User createUser(UserRequestDTO request){
        if(userRepository.findByCpf(request.cpf()).isPresent()){
            throw new CpfAlreadyExistException("CPF já cadastrado.");
        }

        if(!isValidEmail(request.email())){
            throw new InvalidEmailException("E-mail invalido.");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistException("E-mail já cadastrado.");
        }

        User user = new User();
        user.setName(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); // criptografar senha
        user.setCpf(request.cpf());
        user.setSaldo(0);

        User savedUser = userRepository.save(user);
        return savedUser;
    }

}
