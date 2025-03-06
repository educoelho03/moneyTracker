package br.com.moneyTracker.dto.request;

import br.com.moneyTracker.domain.entities.User;

public record UserRequestDTO(Long user_id, String name, String email, String password) {

    public static User toUser(UserRequestDTO userRequestDTO) {
        return new User(null, userRequestDTO.name, userRequestDTO.email , userRequestDTO.password);
    }
}
