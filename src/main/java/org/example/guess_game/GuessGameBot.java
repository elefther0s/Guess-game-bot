package org.example.guess_game;
import org.example.guess_game.dao.impl.DataBaseServiceImpl;
import org.example.guess_game.model.Command;
import org.example.guess_game.model.Difficulty;
import org.example.guess_game.model.WinCondition;
import org.example.guess_game.exception.InvalidDifficultyException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

public class GuessGameBot extends TelegramLongPollingBot {

    private static final Logger logger = Logger.getLogger(GuessGameBot.class.getName());

    private final GuessGameImpl game = new GuessGameImpl();

    private final DataBaseServiceImpl dataBaseService;

    public GuessGameBot(String botToken, DataBaseServiceImpl dataBaseService) {
        super(botToken);
        this.dataBaseService = dataBaseService;
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
        String command = tokens[0];
        String output = null;
        message.setChatId(update.getMessage().getChatId());
        if (command.equals(Command.START.getTitle())) {
            game.launchGame();
            output = game.getStartMessage();
        } else if (command.equals(Command.DIFFICULTY.getTitle())) {
            String difficult = tokens[1];
            output = "";
            if (game.isRunning()) {
                game.stopGame();
                output = game.getStopMessage();
            }
            Optional<Difficulty> selectedDifficulty = Arrays.stream(Difficulty.values())
                    .filter(dif -> dif.getTitle().equals(difficult))
                    .findFirst();
            game.setDifficulty(selectedDifficulty.orElseThrow(() -> new InvalidDifficultyException()));
            output = game.getDifficultyInfo();
        } else if (command.equals(Command.HELP.getTitle())) {
            output = game.getHelp();
        } else if (command.equals(Command.STATS.getTitle())) {
            output = dataBaseService.getStats() + dataBaseService.getUserStats(update.getMessage().getChat().getUserName());
        } else if (game.isRunning()) {
            // TODO большой блок, для читаемости и уменьшения вложенности лучше вынести в отдельным приватный метод
            if (command.equals(Command.STOP.getTitle())) {
                game.stopGame();
                output = game.getStopMessage();
            } else {
                try {
                    int inputNumber = Integer.parseInt(update.getMessage().getText());
                    WinCondition winCondition = game.getWinCondition(inputNumber);
                    if (winCondition.equals(WinCondition.IS_WINNING)) {
                        output = game.getWinMessage();
                    } else if (winCondition.equals(WinCondition.IS_LOSING)) {
                        output = game.getLoseMessage();
                    } else if (winCondition.equals(WinCondition.INPUT_NUMBER_IS_GREATER)) {
                        output = game.getGreaterNumberMessage();
                    } else if (winCondition.equals(WinCondition.INPUT_NUMBER_IS_LESS)) {
                        output = game.getLessNumberMessage();
                    }
                    output += game.getAttemptsInfo();
                    dataBaseService.dataBaseUpdate(update.getMessage().getChat().getUserName(), game.getDifficulty().getPoints(), winCondition);
                } catch (Exception e) {
                    logger.warning(e.getMessage());
                    output = game.getInvalidInputMessage();
                }
            }
        }
        message.setText(output);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
        }
    }
}
