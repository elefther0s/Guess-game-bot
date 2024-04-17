package org.example;

import org.example.guess_game.dao.DataBaseService;
import org.example.guess_game.dao.Stats;
import org.example.guess_game.dao.impl.DataBaseServiceImpl;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataBaseServiceTest {

    private Connection connection = mock(Connection.class);
    private DataBaseService sut = new DataBaseServiceImpl(connection);

    @Test
    void getTopStats_when_stats_isEmpty() throws SQLException {
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        List<Stats> result = sut.getTopPlayersStats();

        assertNull(result);
    }

    @Test
    void given_getUserStats_when_userNotFound() throws SQLException {

        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        Stats result = sut.getUserStats("username");

        assertNull(result);
    }
}