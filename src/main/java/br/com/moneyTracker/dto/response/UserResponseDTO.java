package br.com.moneyTracker.dto.response;

import br.com.moneyTracker.domain.entities.User;

public record UserResponseDTO(Long user_id, String name, String email, String cpf) {

    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getUser_id(),
                user.getName(),
                user.getEmail(),
                user.getCpf()
        );
    }
}
