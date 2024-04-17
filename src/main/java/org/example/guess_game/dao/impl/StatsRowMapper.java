package org.example.guess_game.dao.impl;

import org.example.guess_game.dao.RowMapper;
import org.example.guess_game.dao.Stats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsRowMapper implements RowMapper<Stats> {

    @Override
    public Stats map(ResultSet resultSet) throws SQLException {
        return Stats.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .points(resultSet.getInt("points"))
                .games(resultSet.getInt("games"))
                .wins(resultSet.getInt("wins"))
                .firstGame(resultSet.getTimestamp("first_game").toLocalDateTime())
                .lastGame(resultSet.getTimestamp("last_game").toLocalDateTime())
                .build();
    }
}
