-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE apod (date DATE PRIMARY KEY, explanation TEXT, hdurl TEXT, title TEXT NOT NULL, url TEXT NOT NULL);

-- changeset liquibase:2
DROP TABLE apod;
CREATE TABLE apod (
                      id BIGSERIAL PRIMARY KEY,
                      picture_date DATE,
                      explanation VARCHAR(500),
                      hdurl VARCHAR(500),
                      title VARCHAR(500) NOT NULL,
                      url VARCHAR(500) NOT NULL);

-- changeset liquibase:3
ALTER TABLE apod ALTER COLUMN explanation TYPE VARCHAR(255);
ALTER TABLE apod ALTER COLUMN hdurl TYPE VARCHAR(255);
ALTER TABLE apod ALTER COLUMN title TYPE VARCHAR(255);
ALTER TABLE apod ALTER COLUMN url TYPE VARCHAR(255);

-- changeset liquibase:4
ALTER TABLE apod ALTER COLUMN explanation TYPE VARCHAR(2048);