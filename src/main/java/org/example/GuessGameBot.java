package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Scanner;

public class GuessGameBot extends TelegramLongPollingBot {

    private final GuessGame game = new GuessGame();

    private final DataBaseService dataBaseService;

    public GuessGameBot(String botToken, DataBaseService dataBaseService) {
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
        try(Scanner scanner = new Scanner(update.getMessage().getText()))
        {
            String input = scanner.next();
            String output = null;
            message.setChatId(update.getMessage().getChatId());
            if (input.equals("/start")) {
                game.launchGame();
                output = game.getStartMessage();
            }
            else if (input.equals("/difficulty")) {
                input = scanner.next();
                output = "";
                if(game.isRunning()) {
                    game.stopGame();
                    output = game.getStopMessage();
                }
                if(input.equals("easy")) {
                    game.setDifficulty(Difficulty.EASY);
                    output = output + '\n' + game.getDifficultyInfo();
                }
                else if(input.equals("medium")) {
                    game.setDifficulty(Difficulty.MEDIUM);
                    output = output + '\n' + game.getDifficultyInfo();
                }
                else if(input.equals("hard")) {
                    game.setDifficulty(Difficulty.HARD);
                    output = output + '\n' + game.getDifficultyInfo();
                }
                else {
                    output = output + '\n' + game.getWrongDifficultyMessage();
                }
            }
            else if (input.equals("/help")) {
                output = game.getHelp();
            }
            else if (input.equals("/stats")) {
                output = dataBaseService.getStats() + dataBaseService.getUserStats(update.getMessage().getChat().getUserName());
            }
            else if (game.isRunning()) {
                if (input.equals("/stop")) {
                    game.stopGame();
                    output = game.getStopMessage();
                }
                else {
                    try {
                        int inputNumber = Integer.parseInt(update.getMessage().getText());
                        WinCondition winCondition = game.getWinCondition(inputNumber);
                        if(winCondition.equals(WinCondition.IS_WINNING)) {
                            output = game.getWinMessage();
                            dataBaseService.dataBaseWinUpdate(update.getMessage().getChat().getUserName(), game.getDifficulty().getPoints());
                        }
                        else if(winCondition.equals(WinCondition.IS_LOSING)) {
                            output = game.getLoseMessage();
                            dataBaseService.dataBaseLoseUpdate(update.getMessage().getChat().getUserName());
                        }
                        else if(winCondition.equals(WinCondition.INPUT_NUMBER_IS_GREATER)) {
                            output = game.getGreaterNumberMessage();
                        }
                        else if(winCondition.equals(WinCondition.INPUT_NUMBER_IS_LESS)) {
                            output = game.getLessNumberMessage();
                        }
                        output += game.getAttemptsInfo();
                    } catch (Exception e) {
                        output = game.getWrongInputMessage();
                    }
                }
            }
            message.setText(output);
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
