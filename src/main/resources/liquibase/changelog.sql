-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE apod (date DATE PRIMARY KEY, explanation TEXT, hdurl TEXT, title TEXT NOT NULL, url TEXT NOT NULL);
