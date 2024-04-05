package org.example;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String botToken;
        String url;
        String user;
        String password;
        try(InputStream stream = Files.newInputStream(Path.of("E:\\IDEA Projects\\guess-game-bot\\src\\main\\java\\org\\example\\source.txt"));
            Scanner scanner = new Scanner(stream)) {
            botToken = scanner.nextLine();
            url = scanner.nextLine();
            user = scanner.nextLine();
            password = scanner.nextLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DataBaseService dataBaseService = new DataBaseService(url, user, password);
        GuessGameBot guessGameBot = new GuessGameBot(botToken, dataBaseService);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(guessGameBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}