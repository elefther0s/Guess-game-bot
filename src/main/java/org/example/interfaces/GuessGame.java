package org.example.interfaces;

import org.example.enums.Difficulty;
import org.example.enums.WinCondition;

public interface GuessGame {
    void launchGame();
    void stopGame();
    void setDifficulty(Difficulty difficulty);
    Difficulty getDifficulty();
    WinCondition getWinCondition(int inputNumber);
}
