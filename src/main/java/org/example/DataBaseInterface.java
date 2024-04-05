package org.example;

import org.example.enums.WinCondition;

public interface DataBaseInterface {
    String getStats();

    String getUserStats(String name);

    void dataBaseUpdate(String name, int points, WinCondition winCondition);
}
