package br.com.moneyTracker.dto.response;

public record UserResponse(Long id, String username, String cpf) {

    public static UserResponse toDto(UserResponse userResponse) {
        return new UserResponse(userResponse.id(), userResponse.username(), userResponse.cpf());
    }
}
