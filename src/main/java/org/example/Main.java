package org.example;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    private final static String botToken = "";
    public static void main(String[] args) {
        GuessGameBot guessGameBot = new GuessGameBot(botToken);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(guessGameBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}