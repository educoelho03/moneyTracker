package br.com.moneyTracker.dto.request;

public record ResetPasswordRequestDTO(String email, String newPassword, String confirmNewPassword) {
}
