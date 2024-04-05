package org.example;

public interface DataBaseInterface {
    String getStats();

    String getUserStats(String name);

    void dataBaseWinUpdate(String name, int points);

    void dataBaseLoseUpdate(String name);
}
