DROP DATABASE IF EXISTS MyMDB_MOVIES;
CREATE DATABASE MyMDB_MOVIES;
USE MyMDB_MOVIES;

CREATE TABLE gender (
	gender_id INTEGER NOT NULL,
    gender_name VARCHAR(16) NOT NULL,
    PRIMARY KEY (gender_id)
);

CREATE TABLE person (
	person_id INTEGER NOT NULL,
    name VARCHAR(128) NOT NULL,
    gender_id INTEGER,
    birthday DATE,
    deathday DATE,
    biography VARCHAR(8192),
    birthplace VARCHAR(128),
    popularity FLOAT,
    profile_path VARCHAR(32),
    PRIMARY KEY (person_id),
    FOREIGN KEY (gender_id) REFERENCES gender (gender_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE movie (
	movie_id VARCHAR(16) NOT NULL,
    title VARCHAR(128) NOT NULL,
    year INTEGER NOT NULL,
    director_id INTEGER NOT NULL,
    rating FLOAT DEFAULT 0.0 NOT NULL,
    num_votes INTEGER DEFAULT 0 NOT NULL,
    budget INTEGER DEFAULT 0,
    revenue BIGINT DEFAULT 0,
    overview VARCHAR(8192),
    backdrop_path VARCHAR(32),
    poster_path VARCHAR(32),
    hidden BOOLEAN DEFAULT 0,
    PRIMARY KEY (movie_id),
    FOREIGN KEY (director_id) REFERENCES person(person_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE genre (
	genre_id INTEGER NOT NULL,
    name VARCHAR(32) NOT NULL,
    PRIMARY KEY (genre_id)
);

CREATE TABLE keyword (
	keyword_id INTEGER NOT NULL,
    name VARCHAR(128) NOT NULL,
    PRIMARY KEY (keyword_id)
);

CREATE TABLE genre_in_movie (
	genre_id INTEGER NOT NULL,
    movie_id VARCHAR(16) NOT NULL,
    PRIMARY KEY (genre_id, movie_id),
    FOREIGN KEY (genre_id) REFERENCES genre(genre_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE person_in_movie (
	person_id INTEGER NOT NULL,
    movie_id VARCHAR(16) NOT NULL,
    PRIMARY KEY (person_id, movie_id),
    FOREIGN KEY (person_id) REFERENCES person(person_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE keyword_in_movie (
	keyword_id INTEGER NOT NULL,
    movie_id VARCHAR(16) NOT NULL,
    PRIMARY KEY (keyword_id, movie_id),
    FOREIGN KEY (keyword_id) REFERENCES keyword(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON UPDATE CASCADE ON DELETE CASCADE
);