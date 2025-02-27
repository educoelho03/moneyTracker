package br.com.moneyTracker.dto.request;

import br.com.moneyTracker.domain.entities.User;

public record UserRequestDTO(Long user_id, String username, String email, String password, String cpf) {

    public static User toUser(UserRequestDTO userRequestDTO) {
        return new User(null, userRequestDTO.username, userRequestDTO.email , userRequestDTO.password, userRequestDTO.cpf);
    }
}
