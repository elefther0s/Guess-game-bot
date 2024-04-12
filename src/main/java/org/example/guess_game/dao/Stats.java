package org.example.guess_game.dao;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class Stats {

    /**
     *     id SERIAL PRIMARY KEY,
     *     name VARCHAR(128) NOT NULL,
     *     points INT,
     *     games INT,
     *     wins INT,
     *     last_game TIMESTAMP,
     *     first_game TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     */
    private Long id;
    private String name;
    private Integer points;
    private Integer games;
    private Integer wins;
    private LocalDateTime lastGame;
    private LocalDateTime firstGame;

}
