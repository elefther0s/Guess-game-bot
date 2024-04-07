package org.example.guess_game.model;

public enum Command {
    START("/start"),
    DIFFICULTY("/difficulty"),
    HELP("/help"),
    STATS("/stats"),
    STOP("/stop");

    private final String title;

    Command(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
