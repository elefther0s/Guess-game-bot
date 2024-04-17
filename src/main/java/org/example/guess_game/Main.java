package org.example.guess_game;


import org.example.guess_game.dao.impl.DataBaseServiceImpl;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        String botToken;
        String url;
        String user;
        String password;
        try (InputStream stream = Main.class.getClassLoader().getResourceAsStream("properties.txt");
            Scanner scanner = new Scanner(stream)) {
            botToken = scanner.nextLine();
            url = scanner.nextLine();
            user = scanner.nextLine();
            password = scanner.nextLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DataBaseServiceImpl dataBaseService = new DataBaseServiceImpl(url, user, password);

        GuessGameBot guessGameBot = new GuessGameBot(botToken, dataBaseService);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(guessGameBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}