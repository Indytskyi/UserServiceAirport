CREATE TABLE user (
    id BIGSERIAL PRIMARY KEY,
    email varchar NOT NULL UNIQUE,
    password varchar NOT NULL
);

CREATE TABLE passenger (
    name varchar NOT NULL,
    surname varchar NOT NULL,
    data_birth DATE NOT NULL,
    gender varchar NOT NULL,
    photo varchar
);