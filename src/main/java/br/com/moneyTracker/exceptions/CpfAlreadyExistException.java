package br.com.moneyTracker.exceptions;

public class CpfAlreadyExistException extends RuntimeException {
    public CpfAlreadyExistException(String message) {
        super(message);
    }
}
