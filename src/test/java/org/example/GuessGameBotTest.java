package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class GuessGameBotTest {

    private String token = "dummyToken";
    private GuessGame guessGame = mock(GuessGame.class);
    private DataBaseService dataBaseService = mock(DataBaseService.class);


    // system under test
    private GuessGameBot sut = new GuessGameBot(token, guessGame, dataBaseService);

    // todo сложно написать тест из-за сильной связанности логики игры и отправки ответа через бота
    // в идеал вынести логику игры в отдельный класс, тогда можно было бы протестировать изолировано
    // сейчас вызывается метод execute и выбрасывается исключение
    @Test
    void onUpdateReceived_CommandStart_should_launchGame() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        var command = "/start";
        var startMessage = "Игра запущена. Загадано число от 1 до 100. Вы должны отгадать его за 10 попыток.";

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn(command);
        when(message.getChatId()).thenReturn(123L);
        when(guessGame.getStartMessage()).thenReturn(startMessage);

        // test

        sut.onUpdateReceived(update);

        verify(guessGame, times(1)).launchGame();
        verify(message, times(2)).setText(argumentCaptor.capture());
        assertEquals(startMessage, argumentCaptor.getValue());

    }
}