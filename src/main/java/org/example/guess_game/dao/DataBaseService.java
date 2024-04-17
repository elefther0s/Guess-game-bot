package org.example.guess_game.dao;

import org.example.guess_game.model.WinCondition;

import java.util.List;

public interface DataBaseService {
    List<Stats> getTopPlayersStats();


    Stats getUserStats(String name);

    void dataBaseUpdate(String name, int points, WinCondition winCondition);
}
