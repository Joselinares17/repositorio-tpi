package org.lumeninvestiga.backend.repositorio.tpi.exceptions;

public class InvalidRegisterException extends RuntimeException{
    public InvalidRegisterException() {
    }

    public InvalidRegisterException(String message) {
        super(message);
    }
}
