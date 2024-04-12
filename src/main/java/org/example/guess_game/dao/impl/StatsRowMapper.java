package org.example.guess_game.dao.impl;

import lombok.SneakyThrows;
import org.example.guess_game.dao.RowMapper;
import org.example.guess_game.dao.Stats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsRowMapper implements RowMapper<Stats> {

    @Override
    public Stats map(ResultSet resultSet) throws SQLException {
        return Stats.builder()
                .id(resultSet.getLong("id"))
                .wins(resultSet.getInt("wins"))
                .build();

    }
}
