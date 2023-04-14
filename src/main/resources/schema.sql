DROP TABLE IF EXISTS users, films, mpa, genres, film_genres, film_likes, friends_user CASCADE;



CREATE TABLE IF NOT EXISTS mpa
(
 mpa_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 mpa_name VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
 film_id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 name         VARCHAR(60)  NOT NULL,
 description  VARCHAR(200) NOT NULL,
 release_date DATE         NOT NULL,
 duration     INTEGER      NOT NULL,
 mpa_id       INTEGER REFERENCES mpa (mpa_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
 user_id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 email    VARCHAR(60) NOT NULL,
 login    VARCHAR(60) NOT NULL,
 name     VARCHAR(60),
 birthday DATE        NOT NULL
);

CREATE TABLE IF NOT EXISTS genres
(
 genre_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 genre_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres
(
 genre_id INTEGER REFERENCES genres (genre_id) ON DELETE CASCADE,
 film_id  INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
 PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_likes
(
 film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
 user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friends_user
(
 user_id   INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
 friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
 status BOOLEAN NOT NULL
);