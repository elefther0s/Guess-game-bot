package org.example;

import org.example.guess_game.dao.DataBaseService;
import org.example.guess_game.dao.impl.DataBaseServiceImpl;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataBaseServiceTest {

    private Connection connection = mock(Connection.class);
    private DataBaseService sut = new DataBaseServiceImpl(connection);

    @Test
    void dataBaseUpdate() {
    }

    @Test
    void getStats() {
    }

    @Test
    void given_getUserStats_when_userNotFound_then_returnCorrectMessage() throws SQLException {

        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        String result = sut.getUserStats("username");

        assertEquals("Вы еще не играли.", result);
    }
}