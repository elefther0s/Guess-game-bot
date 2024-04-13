package org.example;

import org.example.guess_game.dao.DataBaseService;
import org.example.guess_game.service.GuessGameService;
import org.example.guess_game.service.GuessGameServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class GuessGameServiceImplTest {
    private DataBaseService dataBaseService = mock(DataBaseService.class);
    private GuessGameService sut = new GuessGameServiceImpl(dataBaseService);

    @Test
    void onCommandStart_should_launchGame() {
        String[] tokens = {"/start"};
        String startMessage = "Игра запущена.\n" + "Загадано число от 1 до 100. Вы должны отгадать его за 7 попыток.";

        String result = sut.execute(tokens, null);

        assertEquals(startMessage, result);
    }

    @Test
    void onCommandStop_should_stopGame() {
        String[] startTokens = {"/start"};
        String[] stopTokens = {"/stop"};
        String stopMessage = "Игра остановлена.";

        sut.execute(startTokens, null);
        String result = sut.execute(stopTokens, null);

        assertEquals(stopMessage, result);
    }

    @Test
    void onCommandDifficultyEasy_should_difficultyEasy() {
        String[] tokens = {"/difficulty", "easy"};
        String difficultyInfo = "Установлена сложность easy.";

        String result = sut.execute(tokens, null);

        assertEquals(difficultyInfo, result);
    }

    @Test
    void onCommandDifficultyMedium_should_difficultyMedium() {
        String[] tokens = {"/difficulty", "medium"};
        String difficultyInfo = "Установлена сложность medium.";

        String result = sut.execute(tokens, null);

        assertEquals(difficultyInfo, result);
    }

    @Test
    void onCommandDifficultyHard_should_difficultyHard() {
        String[] tokens = {"/difficulty", "hard"};
        String difficultyInfo = "Установлена сложность hard.";

        String result = sut.execute(tokens, null);

        assertEquals(difficultyInfo, result);
    }

    @Test
    void onCommandHelp_should_help() {
        String[] tokens = {"/help"};
        String info = "Список комманд:\n/start - запустить игру,\n" +
                "/stop - остановить игру,\n/stats - показать статистику,\n" +
                "/difficulty <сложность> - установить выбранную сложность(easy, medium, hard).";

        String result = sut.execute(tokens, null);

        assertEquals(info, result);
    }
}