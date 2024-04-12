CREATE TABLE stats(
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    points INT,
    games INT,
    wins INT,
    last_game TIMESTAMP,
    first_game TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);