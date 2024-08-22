-- liquibase formatted sql

-- changeset PC:1724341144043-1
CREATE TABLE if not exists app_users (id BIGINT AUTO_INCREMENT NOT NULL, username VARCHAR(255) NULL, email VARCHAR(255) NULL, password VARCHAR(255) NULL, app_user_role VARCHAR(255) NULL, CONSTRAINT pk_app_users PRIMARY KEY (id));

-- changeset PC:1724341144043-2
CREATE TABLE if not exists game_ratings (id BIGINT AUTO_INCREMENT NOT NULL, rate DOUBLE NOT NULL, user_id BIGINT NULL, game_id BIGINT NULL, CONSTRAINT pk_game_ratings PRIMARY KEY (id));

-- changeset PC:1724341144043-3
CREATE TABLE if not exists games (id BIGINT AUTO_INCREMENT NOT NULL, title VARCHAR(255) NULL, producer VARCHAR(255) NULL, game_category VARBINARY(255) NULL, image_url VARCHAR(255) NULL, is_accepted BIT(1) NOT NULL, CONSTRAINT pk_games PRIMARY KEY (id));

-- changeset PC:1724341144043-4
CREATE TABLE if not exists games_ratings (game_id BIGINT NOT NULL, ratings_id BIGINT NOT NULL);

-- changeset PC:1724341144043-5
ALTER TABLE games_ratings ADD CONSTRAINT uc_games_ratings_ratings UNIQUE (ratings_id);

-- changeset PC:1724341144043-6
ALTER TABLE game_ratings ADD CONSTRAINT FK_GAME_RATINGS_ON_GAME FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE;

-- changeset PC:1724341144043-7
ALTER TABLE game_ratings ADD CONSTRAINT FK_GAME_RATINGS_ON_USER FOREIGN KEY (user_id) REFERENCES app_users (id);

-- changeset PC:1724341144043-8
ALTER TABLE games_ratings ADD CONSTRAINT fk_gamrat_on_game FOREIGN KEY (game_id) REFERENCES games (id);

-- changeset PC:1724341144043-9
ALTER TABLE games_ratings ADD CONSTRAINT fk_gamrat_on_game_rating FOREIGN KEY (ratings_id) REFERENCES game_ratings (id);

