CREATE TABLE IF NOT EXISTS Run (
    id INTEGER PRIMARY KEY CHECK (id > 0) NOT NULL, -- Ensures id is positive
    title VARCHAR(255) NOT NULL, -- Ensures title is not empty and not null
    started_on TIMESTAMP NOT NULL, -- LocalDateTime in Java maps to TIMESTAMP in SQL
    ended_on TIMESTAMP NOT NULL, -- LocalDateTime in Java maps to TIMESTAMP in SQL
    distance INTEGER CHECK (distance > 0) NOT NULL, -- Ensures distance is positive
    location VARCHAR(255) NOT NULL -- Assuming Location is a custom type that can be stored as a string
);
