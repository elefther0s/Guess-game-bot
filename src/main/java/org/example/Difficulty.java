package org.example;

public enum Difficulty {
    EASY(5, 10, "easy"),
    MEDIUM(7, 100, "medium"),
    HARD(11, 1000, "hard");

    private final int attempts, range;
    private final String title;

    Difficulty(int attempts, int range, String title) {
        this.attempts = attempts;
        this.range = range;
        this.title = title;
    }
    public int getAttempts() {
        return attempts;
    }

    public int getRange() {
        return range;
    }

    public String getTitle() {
        return title;
    }
}
