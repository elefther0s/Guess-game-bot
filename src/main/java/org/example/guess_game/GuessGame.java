package org.example.guess_game;

import org.example.guess_game.model.Difficulty;
import org.example.guess_game.model.WinCondition;

public interface GuessGame {
    void launchGame();
    void stopGame();
    void setDifficulty(Difficulty difficulty);
    Difficulty getDifficulty();
    WinCondition getWinCondition(int inputNumber);

    Object getStartMessage();
}
