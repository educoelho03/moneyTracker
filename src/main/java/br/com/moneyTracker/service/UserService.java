package br.com.moneyTracker.service;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.request.UserRequest;
import br.com.moneyTracker.dto.response.UserResponse;
import br.com.moneyTracker.exceptions.CpfAlreadyExistException;
import br.com.moneyTracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request){
        if(userRepository.findByCpf(request.cpf()).isPresent()){
            throw new CpfAlreadyExistException("CPF já cadastrado.");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password()); // criptografar senha
        user.setCpf(request.cpf());
        user.setSaldo(0);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getuser_id(),
                savedUser.getUsername(),
                savedUser.getCpf()
        );
    }

}
