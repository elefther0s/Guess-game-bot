package org.example.guess_game.service;

import org.example.guess_game.GuessGameImpl;
import org.example.guess_game.dao.DataBaseService;
import org.example.guess_game.exception.InvalidDifficultyException;
import org.example.guess_game.model.Command;
import org.example.guess_game.model.Difficulty;
import org.example.guess_game.model.WinCondition;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

public class GuessGameServiceImpl implements GuessGameService {

    private static final Logger logger = Logger.getLogger(GuessGameServiceImpl.class.getName());

    private final GuessGameImpl game = new GuessGameImpl();

    private final DataBaseService dataBaseService;

    public GuessGameServiceImpl(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    @Override
    public String execute(String[] tokens, String username) {
        String output = "";
        String command = tokens[0];
        if (command.equals(Command.START.getTitle())) {
            game.launchGame();
            output = game.getStartMessage();
        } else if (command.equals(Command.DIFFICULTY.getTitle())) {
            try {
                if (tokens.length == 1) {
                    throw new InvalidDifficultyException();
                }
                String difficulty = tokens[1];
                Optional<Difficulty> selectedDifficulty = Arrays.stream(Difficulty.values())
                        .filter(dif -> dif.getTitle().equals(difficulty))
                        .findFirst();
                game.setDifficulty(selectedDifficulty.orElseThrow(() -> new InvalidDifficultyException()));
                if (game.isRunning()) {
                    game.stopGame();
                    output = game.getStopMessage() + '\n';
                }
                output = output + game.getDifficultyInfo();
            } catch (InvalidDifficultyException e) {
                logger.warning(e.getMessage() + " " + e.getClass());
                output = game.getInvalidDifficultyMessage();
            }
        } else if (command.equals(Command.HELP.getTitle())) {
            output = game.getHelp();
        } else if (command.equals(Command.STATS.getTitle())) {
            output = dataBaseService.getStats() + dataBaseService.getUserStats(username);
        } else if (game.isRunning()) {
            output = inGameUpdate(username, command);
        }
        return output;
    }

    private String inGameUpdate(String username, String command) {
        String output = null;
        if (command.equals(Command.STOP.getTitle())) {
            game.stopGame();
            output = game.getStopMessage();
        } else {
            try {
                int inputNumber = Integer.parseInt(command);
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
                dataBaseService.dataBaseUpdate(username, game.getDifficulty().getPoints(), winCondition);
            } catch (IllegalArgumentException e) {
                logger.warning(e.getMessage() + " " + e.getClass());
                output = game.getInvalidInputMessage();
            }
        }
        return output;
    }
}
