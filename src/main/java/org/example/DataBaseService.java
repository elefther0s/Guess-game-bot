package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataBaseService implements DataBaseInterface {
    private final Connection connection;
    public DataBaseService(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("SQL");
        }
    }

    public void dataBaseWinUpdate(String name, int points) {
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM stats WHERE name = ?")) {
            selectStatement.setString(1, name);

            try (ResultSet result = selectStatement.executeQuery()) {
                if (result.next()) {
                    int count = result.getInt(1);
                    if (count == 0) {
                        try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO stats (name, points, games, wins, last_game) VALUES (?, ?, 1, 1, CURRENT_TIMESTAMP)")) {
                            insertStatement.setString(1, name);
                            insertStatement.setInt(2, points);
                            insertStatement.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE stats SET games = games + 1, wins = wins + 1, last_game = CURRENT_TIMESTAMP, points = points + ? WHERE name = ?")) {
                            updateStatement.setInt(1, points);
                            updateStatement.setString(2, name);
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dataBaseLoseUpdate(String name) {
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM stats WHERE name = ?")) {
            selectStatement.setString(1, name);

            try (ResultSet result = selectStatement.executeQuery()) {
                if (result.next()) {
                    int count = result.getInt(1);
                    if (count == 0) {
                        try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO stats (name, points, games, wins, last_game) VALUES (?, 0, 1, 0, CURRENT_TIMESTAMP)")) {
                            insertStatement.setString(1, name);
                            insertStatement.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE stats SET games = games + 1, last_game = CURRENT_TIMESTAMP WHERE name = ?")) {
                            updateStatement.setString(1, name);
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStats() {
        StringBuilder message = new StringBuilder();
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM stats");
            ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                int count = result.getInt(1);
                if (count == 0) {
                    message.append("Статистика пуста.\n");
                    return message.toString();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stats ORDER BY points DESC LIMIT 5");
             ResultSet resultSet = statement.executeQuery()) {
            int i = 1;
            while (resultSet.next()) {
                String name = "@" + resultSet.getString("name");
                int points = resultSet.getInt("points");
                int games = resultSet.getInt("games");
                int wins = resultSet.getInt("wins");
                String lastGame = resultSet.getTimestamp("last_game").toLocalDateTime().toLocalDate().toString();
                String firstGame = resultSet.getTimestamp("first_game").toLocalDateTime().toLocalDate().toString();

                message.append(i).append(". ").append(name).append(" Очков: ").append(points).append(" Игр: ").append(games).append(" Побед: ").append(wins).append("\n");
                message.append("Первая игра: ").append(firstGame).append(" Последняя игра: ").append(lastGame).append("\n");
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return message.toString();
    }

    public String getUserStats(String userName) {
        StringBuilder message = new StringBuilder();
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM stats WHERE name = ?")) {
            statement.setString(1, userName);
            try(ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int count = result.getInt(1);
                    if (count == 0) {
                        message.append("Вы ещё не играли.");
                        return message.toString();
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
                    String name = "@" + resultSet.getString("name");
                    int points = resultSet.getInt("points");
                    int games = resultSet.getInt("games");
                    int wins = resultSet.getInt("wins");
                    String firstGame = resultSet.getTimestamp("first_game").toLocalDateTime().toLocalDate().toString();
                    String lastGame = resultSet.getTimestamp("last_game").toLocalDateTime().toLocalDate().toString();

                    message.append("Ваша статистика:\n").append(name).append(" Очков: ").append(points).append(" Игр: ").append(games).append(" Побед: ").append(wins).append("\n");
                    message.append("Первая игра: ").append(firstGame).append(" Последняя игра: ").append(lastGame).append("\n");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return message.toString();
    }
}
