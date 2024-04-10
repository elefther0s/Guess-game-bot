package org.example.guess_game.exception;

public class InvalidDifficultyException extends RuntimeException {
    public InvalidDifficultyException () {
        super();
    }

    public InvalidDifficultyException(String message) {
        super(message);
    }
}
