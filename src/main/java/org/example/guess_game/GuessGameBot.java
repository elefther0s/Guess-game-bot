package org.example.guess_game;
import org.example.guess_game.dao.DataBaseService;
import org.example.guess_game.service.GuessGameService;
import org.example.guess_game.service.GuessGameServiceImpl;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Logger;

public class GuessGameBot extends TelegramLongPollingBot {

    private static final Logger logger = Logger.getLogger(GuessGameBot.class.getName());
    private final DataBaseService dataBaseService;
    private final GuessGameService guessGameService;

    public GuessGameBot(String botToken, DataBaseService dataBaseService) {
        super(botToken);
        this.dataBaseService = dataBaseService;
        this.guessGameService = new GuessGameServiceImpl(dataBaseService);
    }

    @Override
    public String getBotUsername() {
        return "GuessGamingBot";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        String[] tokens = update.getMessage().getText().split(" ");
        String username = update.getMessage().getChat().getUserName();
        message.setChatId(update.getMessage().getChatId());
        String output = guessGameService.execute(tokens, username);
        message.setText(output);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
        }
    }
}
