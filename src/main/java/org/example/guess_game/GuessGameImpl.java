package org.example.guess_game;

import org.example.guess_game.model.Difficulty;
import org.example.guess_game.model.WinCondition;

import java.util.Random;

public class GuessGameImpl implements GuessGame {
    private final static String START_MESSAGE = "Игра запущена.";
    private final static String STOP_MESSAGE = "Игра остановлена.";
    private final static String GAME_START_INFO = "Загадано число от 1 до %d. Вы должны отгадать его за %d %s.";
    private final static String INVALID_INPUT_MESSAGE = "Введите целое число от 1 до %d.";
    private final static String WIN_MESSAGE = "Вы выиграли. Было загадано число %d. ";
    private final static String LOSS_MESSAGE = "Вы проиграли. Было загадано число %d. ";
    private final static String LESS_NUMBER_MESSAGE = "Ваше число меньше загаданного. ";
    private final static String GREATER_NUMBER_MESSAGE = "Ваше число больше загаданного. ";
    private final static String ATTEMPTS_INFO = "%s %d %s.";
    private final static String DIFFICULTY_CHANGE_MESSAGE = "Установлена сложность %s.";
    private final static String INVALID_DIFFICULTY_MESSAGE = "Неправильно указан уровень сложности.";
    private final static String HELP_MESSAGE = "Список комманд:\n/start - запустить игру,\n/stop - остановить игру,\n/stats - показать статистику,\n/difficulty <сложность> - установить выбранную сложность(easy, medium, hard).";
    private int targetNumber, attempts;
    private Difficulty difficulty = Difficulty.MEDIUM;
    private boolean isRunning = false;

    public void launchGame() {
        Random random = new Random();
        targetNumber = random.nextInt(difficulty.getRange()) + 1;
        isRunning = true;
        attempts = difficulty.getAttempts();
    }

    public String getStartMessage() {
        String word = "попыток";
        if(attempts > 1 && attempts < 5)
            word = "попытки";
        else if(attempts == 1)
            word = "попытка";
        String output = START_MESSAGE + '\n' + String.format(GAME_START_INFO, difficulty.getRange(), difficulty.getAttempts(), word);
        return output;
    }

    public void stopGame(){
        isRunning = false;
    }

    public String getStopMessage() {
        return STOP_MESSAGE;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getDifficultyInfo(){
        return String.format(DIFFICULTY_CHANGE_MESSAGE, difficulty.getTitle());
    }

    public String getInvalidDifficultyMessage() {
        return INVALID_DIFFICULTY_MESSAGE;
    }

    public WinCondition getWinCondition(int inputNumber) {
        if(inputNumber < 1 || inputNumber > difficulty.getRange()) {
            throw new IllegalArgumentException();
        }
        attempts--;
        if(inputNumber == targetNumber) {
            isRunning = false;
            return WinCondition.IS_WINNING;
        }
        if(attempts == 0) {
            isRunning = false;
            return WinCondition.IS_LOSING;
        }
        if(inputNumber > targetNumber) {
            return WinCondition.INPUT_NUMBER_IS_GREATER;
        }
        if(inputNumber < targetNumber) {
            return WinCondition.INPUT_NUMBER_IS_LESS;
        }
        return null;
    }

    public String getAttemptsInfo() {
        String word = "попыток";
        String verb = "Осталось";
        if(attempts > 1 && attempts < 5)
            word = "попытки";
        else if(attempts == 1) {
            word = "попытка";
            verb = "Осталась";
        }
        return String.format(ATTEMPTS_INFO, verb, attempts, word);
    }

    public String getWinMessage() {
        return String.format(WIN_MESSAGE, targetNumber);
    }

    public String getLoseMessage() {
        return String.format(LOSS_MESSAGE, targetNumber);
    }

    public String getGreaterNumberMessage() {
        return GREATER_NUMBER_MESSAGE;
    }

    public String getLessNumberMessage() {
        return LESS_NUMBER_MESSAGE;
    }

    public String getInvalidInputMessage() {
        return String.format(INVALID_INPUT_MESSAGE, difficulty.getRange());
    }

    public String getHelp() {
        return HELP_MESSAGE;
    }
}
