package br.com.moneyTracker.dto.response;

public record UserResponseDTO(Long id, String name, String email, String cpf) {

    public static UserResponseDTO toDto(UserResponseDTO userResponseDTO) {
        return new UserResponseDTO(userResponseDTO.id(), userResponseDTO.name(), userResponseDTO.email(), userResponseDTO.cpf());
    }
}
