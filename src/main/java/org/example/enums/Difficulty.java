package org.example.enums;

public enum Difficulty {
    EASY(4, 10, 1, "easy"),
    MEDIUM(7, 100, 5, "medium"),
    HARD(10, 1000, 25, "hard");

    private final int attempts, range , points;
    private final String title;

    Difficulty(int attempts, int range, int points, String title) {
        this.attempts = attempts;
        this.range = range;
        this.points = points;
        this.title = title;
    }
    public int getAttempts() {
        return attempts;
    }

    public int getRange() {
        return range;
    }

    public int getPoints() {
        return points;
    }

    public String getTitle() {
        return title;
    }
}
