package org.example.exceptions;

public class InvalidDifficultyException extends RuntimeException {
    public InvalidDifficultyException () {
        super();
    }

    public InvalidDifficultyException(String message) {
        super(message);
    }
}
