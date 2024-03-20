package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class GuessGameBot extends TelegramLongPollingBot {
    private final GuessGame game = new GuessGame();

    public GuessGameBot(String botToken) {
        super(botToken);
    }

    @Override
    public String getBotUsername() {
        return  "GuessGamingBot";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        String input = update.getMessage().getText();
        String output = null;
        message.setChatId(update.getMessage().getChatId());
        if(input.equals("/start")){
            game.launchGame();
            output = game.getStartMessage();
        }
        else if(input.equals("/easy")) {
            output = game.setDifficulty(Difficulty.EASY);
        }
        else if(input.equals("/medium")) {
            output = game.setDifficulty(Difficulty.MEDIUM);
        }
        else if(input.equals("/hard")) {
            output = game.setDifficulty(Difficulty.HARD);
        }
        else if(game.isRunning) {
            try {
                int inputNumber = Integer.parseInt(update.getMessage().getText());
                output = game.getWinCondition(inputNumber);
            } catch (Exception e) {
                output = game.getWrongInputMessage();
            }
        }
        message.setText(output);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
