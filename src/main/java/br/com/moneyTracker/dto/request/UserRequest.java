package br.com.moneyTracker.dto.request;

import br.com.moneyTracker.domain.entities.User;

public record UserRequest(Long user_id, String username, String email, String password, String cpf) {

    public static User toUser(UserRequest userRequest) {
        return new User(null, userRequest.username, userRequest.email ,userRequest.password, userRequest.cpf);
    }
}
