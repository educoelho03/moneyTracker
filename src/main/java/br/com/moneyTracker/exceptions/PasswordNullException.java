package br.com.moneyTracker.exceptions;

public class PasswordNullException extends RuntimeException {
    public PasswordNullException(String message) {
        super(message);
    }
}
