CREATE SEQUENCE IF NOT EXISTS password_reset_token_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS refresh_token_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE exercise
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_by      BIGINT,
    last_updated_by BIGINT,
    created_on      TIMESTAMP WITHOUT TIME ZONE,
    last_updated_on TIMESTAMP WITHOUT TIME ZONE,
    is_active       BOOLEAN                                 NOT NULL,
    name            VARCHAR(150)                            NOT NULL,
    CONSTRAINT pk_exercise PRIMARY KEY (id)
);

CREATE TABLE exercise_muscle
(
    exercise_id BIGINT NOT NULL,
    muscle_name VARCHAR(255)
);

CREATE TABLE password_reset_token
(
    id          BIGINT                      NOT NULL,
    user_id     BIGINT,
    token       VARCHAR(255)                NOT NULL,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    attempts    INTEGER,
    CONSTRAINT pk_password_reset_token PRIMARY KEY (id)
);

CREATE TABLE profile_picture
(
    user_id   BIGINT NOT NULL,
    name      VARCHAR(255),
    type      VARCHAR(255),
    file_data OID,
    CONSTRAINT pk_profile_picture PRIMARY KEY (user_id)
);

CREATE TABLE refresh_token
(
    id          BIGINT                      NOT NULL,
    user_id     BIGINT,
    token       VARCHAR(255)                NOT NULL,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_refresh_token PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(20),
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE series
(
    id                    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_by            BIGINT,
    last_updated_by       BIGINT,
    created_on            TIMESTAMP WITHOUT TIME ZONE,
    last_updated_on       TIMESTAMP WITHOUT TIME ZONE,
    is_active             BOOLEAN                                 NOT NULL,
    position_index        INTEGER                                 NOT NULL,
    parent_id             BIGINT,
    weight                INTEGER                                 NOT NULL,
    rest_time             VARCHAR(255),
    notes                 OID,
    reps_count            INTEGER                                 NOT NULL,
    difficulty            SMALLINT,
    training_exercises_id BIGINT,
    CONSTRAINT pk_series PRIMARY KEY (id)
);

CREATE TABLE training
(
    id                     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_by             BIGINT,
    last_updated_by        BIGINT,
    created_on             TIMESTAMP WITHOUT TIME ZONE,
    last_updated_on        TIMESTAMP WITHOUT TIME ZONE,
    is_active              BOOLEAN                                 NOT NULL,
    name                   VARCHAR(255)                            NOT NULL,
    sport_preset           VARCHAR(255),
    training_days          SMALLINT[],
    training_status        SMALLINT,
    start_date             date,
    end_date               date,
    performed_date         date,
    description            OID,
    has_warm_up            BOOLEAN                                 NOT NULL,
    has_stretching         BOOLEAN                                 NOT NULL,
    icon_name              VARCHAR(255)                            NOT NULL,
    icon_hexadecimal_color VARCHAR(255),
    parent_id              BIGINT,
    CONSTRAINT pk_training PRIMARY KEY (id)
);

CREATE TABLE training_exercises
(
    id                       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    position_index           INTEGER                                 NOT NULL,
    parent_id                BIGINT,
    training_id              BIGINT,
    exercise_id              BIGINT,
    notes                    VARCHAR(255),
    number_of_warm_up_series INTEGER,
    training_day             date,
    CONSTRAINT pk_training_exercises PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    role_id INTEGER NOT NULL,
    user_id BIGINT  NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE users
(
    id                      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_by              BIGINT,
    last_updated_by         BIGINT,
    created_on              TIMESTAMP WITHOUT TIME ZONE,
    last_updated_on         TIMESTAMP WITHOUT TIME ZONE,
    is_active               BOOLEAN                                 NOT NULL,
    username                VARCHAR(20)                             NOT NULL,
    gender                  SMALLINT,
    email                   VARCHAR(255)                            NOT NULL,
    password                VARCHAR(255)                            NOT NULL,
    profile_picture_user_id BIGINT,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_77584fbe74cc86922be2a3560 UNIQUE (username);

ALTER TABLE password_reset_token
    ADD CONSTRAINT uc_password_reset_token_user UNIQUE (user_id);

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refresh_token_token UNIQUE (token);

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refresh_token_user UNIQUE (user_id);

ALTER TABLE password_reset_token
    ADD CONSTRAINT FK_PASSWORD_RESET_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE profile_picture
    ADD CONSTRAINT FK_PROFILE_PICTURE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESH_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE series
    ADD CONSTRAINT FK_SERIES_ON_PARENT FOREIGN KEY (parent_id) REFERENCES series (id);

ALTER TABLE series
    ADD CONSTRAINT FK_SERIES_ON_TRAINING_EXERCISES FOREIGN KEY (training_exercises_id) REFERENCES training_exercises (id);

ALTER TABLE training_exercises
    ADD CONSTRAINT FK_TRAINING_EXERCISES_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercise (id);

ALTER TABLE training_exercises
    ADD CONSTRAINT FK_TRAINING_EXERCISES_ON_PARENT FOREIGN KEY (parent_id) REFERENCES training_exercises (id);

ALTER TABLE training_exercises
    ADD CONSTRAINT FK_TRAINING_EXERCISES_ON_TRAININGS FOREIGN KEY (training_id) REFERENCES training (id);

ALTER TABLE training
    ADD CONSTRAINT FK_TRAINING_ON_PARENT FOREIGN KEY (parent_id) REFERENCES training (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_PROFILE_PICTURE_USER FOREIGN KEY (profile_picture_user_id) REFERENCES profile_picture (user_id);

ALTER TABLE exercise_muscle
    ADD CONSTRAINT fk_exercise_muscle_on_exercise FOREIGN KEY (exercise_id) REFERENCES exercise (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);