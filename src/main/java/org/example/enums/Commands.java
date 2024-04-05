package org.example.enums;

public enum Commands {
    START("/start"),
    DIFFICULTY("/difficulty"),
    HELP("/help"),
    STATS("/stats"),
    STOP("/stop");

    private final String title;

    Commands(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
