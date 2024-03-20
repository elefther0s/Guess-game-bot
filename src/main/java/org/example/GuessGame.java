package org.example;

import java.util.Random;

public class GuessGame {
    private final String START_MESSAGE = "Игра запущена.";
    private final String GAME_START_INFO = "Загадано число от 1 до %d. Вы должны отгадать его за %d попыток.";
    private final String WRONG_INPUT_MESSAGE = "Введите целое число от 1 до %d.";
    private final String WIN_MESSAGE = "Вы выиграли. Загаданное число было %d. ";
    private final String LOSS_MESSAGE = "Вы проиграли. Загаднное число было %d. ";
    private final String LESS_NUMBER_MESSAGE = "Ваше число меньше загаданного. ";
    private final String BIGGER_NUMBER_MESSAGE = "Ваше число больше загаданного. ";
    private final String ATTEMPTS_INFO = "Осталось %d попыток.";
    private final String DIFFICULTY_CHANGE_MESSAGE = "Сложность изменена на %s.";
    private int targetNumber, attempts;
    private Difficulty difficulty = Difficulty.MEDIUM;
    public boolean isRunning = false;

    public void launchGame(){
        Random random = new Random();
        targetNumber = random.nextInt(difficulty.getRange()) + 1;
        isRunning = true;
        attempts = difficulty.getAttempts();
    }

    public String setDifficulty(Difficulty difficulty){
        isRunning = false;
        this.difficulty = difficulty;
        return String.format(DIFFICULTY_CHANGE_MESSAGE, difficulty.getTitle());
    }

    public String getWinCondition(int inputNumber){
        if(inputNumber < 1 || inputNumber > difficulty.getRange()) {
            throw new RuntimeException();
        }
        attempts--;

        String output = null;
        if(inputNumber == targetNumber) {
            isRunning = false;
            output = String.format(WIN_MESSAGE, targetNumber);
        }
        else if(attempts == 0) {
            isRunning = false;
            output = String.format(LOSS_MESSAGE, targetNumber);
        }
        else if(inputNumber < targetNumber)
            output = LESS_NUMBER_MESSAGE;
        else if(inputNumber > targetNumber)
            output = BIGGER_NUMBER_MESSAGE;
        output = output + String.format(ATTEMPTS_INFO, attempts);
        return output;
    }

    public String getWrongInputMessage() {
        String output = String.format(WRONG_INPUT_MESSAGE, difficulty.getRange());
        return output;
    }

    public String getStartMessage() {
        String output = START_MESSAGE + '\n' + String.format(GAME_START_INFO, difficulty.getRange(), difficulty.getAttempts());
        return output;
    }
}
