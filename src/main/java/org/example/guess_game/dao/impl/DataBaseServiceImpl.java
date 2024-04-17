package org.example.guess_game.dao.impl;

import org.example.guess_game.dao.DataBaseService;
import org.example.guess_game.dao.Stats;
import org.example.guess_game.model.WinCondition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseServiceImpl implements DataBaseService {
    private final Connection connection;

    private final StatsRowMapper mapper = new StatsRowMapper();

    public DataBaseServiceImpl(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public DataBaseServiceImpl(Connection connection) {
        this.connection = connection;
    }

    private boolean isUserInDatabase(String name) throws SQLException {
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM stats WHERE name = ?")) {
            selectStatement.setString(1, name);

            try (ResultSet result = selectStatement.executeQuery()) {
                if (result.next()) {
                    int count = result.getInt(1);
                    return count != 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return false;
    }

    private void addUserInDatabase(String name, int points, boolean isWinning) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO stats (name, points, games, wins, last_game) VALUES (?, ?, 1, ?, CURRENT_TIMESTAMP)")) {
            statement.setString(1, name);
            statement.setInt(2, isWinning ? points : 0);
            statement.setInt(3, isWinning ? 1 : 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    private void updateUserInDatabase(String name, int points, boolean isWinning) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE stats SET games = games + 1, wins = wins + ?, last_game = CURRENT_TIMESTAMP, points = points + ? WHERE name = ?")) {
            statement.setInt(1, isWinning ? 1 : 0);
            statement.setInt(2, isWinning ? points : 0);
            statement.setString(3, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void dataBaseUpdate(String name, int points, WinCondition winCondition) {
        if (winCondition.equals(WinCondition.IS_WINNING) || winCondition.equals(WinCondition.IS_LOSING)) {
            try {
                if (isUserInDatabase(name))
                    updateUserInDatabase(name, points, winCondition == WinCondition.IS_WINNING);
                else
                    addUserInDatabase(name, points, winCondition == WinCondition.IS_WINNING);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    public List<Stats> getTopPlayersStats() {
        List<Stats> topPlayersStats = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM stats");
            ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                int count = result.getInt(1);
                if (count == 0) {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stats ORDER BY points DESC LIMIT 5");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                topPlayersStats.add(mapper.map(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return topPlayersStats;
    }

    public Stats getUserStats(String userName) {
        Stats userStats = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM stats WHERE name = ?")) {
            statement.setString(1, userName);
            try(ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int count = result.getInt(1);
                    if (count == 0) {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stats WHERE name = ?")) {
            statement.setString(1, userName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userStats = mapper.map(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return userStats;
    }
}
