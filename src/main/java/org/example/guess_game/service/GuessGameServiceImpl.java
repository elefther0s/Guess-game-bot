package org.example.guess_game.service;

import org.example.guess_game.GuessGameImpl;
import org.example.guess_game.dao.DataBaseService;
import org.example.guess_game.dao.Stats;
import org.example.guess_game.exception.InvalidDifficultyException;
import org.example.guess_game.model.Command;
import org.example.guess_game.model.Difficulty;
import org.example.guess_game.model.WinCondition;

import java.util.Arrays;
import java.util.List;
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
                game.setDifficulty(selectedDifficulty.orElseThrow(InvalidDifficultyException::new));
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
            output = topPlayersStatsToString(dataBaseService.getTopPlayersStats()) + userStatsToString(dataBaseService.getUserStats(username));
        } else if (game.isRunning()) {
            output = inGameUpdate(username, command);
        }
        return output;
    }

    private String userStatsToString(Stats userStats) {
        if (userStats == null) {
            return "Вы ещё не играли.";
        }
        StringBuilder message = new StringBuilder();
        String name = "@" + userStats.getName();
        int points = userStats.getPoints();
        int games = userStats.getGames();
        int wins = userStats.getWins();
        String firstGame = userStats.getFirstGame().toLocalDate().toString();
        String lastGame = userStats.getLastGame().toLocalDate().toString();

        message.append("Ваша статистика:\n").append(name).append(" Очков: ").append(points).append(" Игр: ").append(games).append(" Побед: ").append(wins).append("\n");
        message.append("Первая игра: ").append(firstGame).append(" Последняя игра: ").append(lastGame).append("\n");
        return message.toString();
    }

    private String topPlayersStatsToString(List<Stats> statsList) {
        if (statsList == null) {
            return "Статистика пуста.";
        }
        StringBuilder message = new StringBuilder();
        int i = 1;
        for (Stats stats: statsList) {
            String name = "@" + stats.getName();
            int points = stats.getPoints();
            int games = stats.getGames();
            int wins = stats.getWins();
            String firstGame = stats.getFirstGame().toLocalDate().toString();
            String lastGame = stats.getLastGame().toLocalDate().toString();

            message.append(i).append(". ").append(name).append(" Очков: ").append(points).append(" Игр: ").append(games).append(" Побед: ").append(wins).append("\n");
            message.append("Первая игра: ").append(firstGame).append(" Последняя игра: ").append(lastGame).append("\n");
            i++;
        }
        return message.toString();
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
