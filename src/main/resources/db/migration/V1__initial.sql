CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email varchar NOT NULL UNIQUE,
    "password" varchar NOT NULL
);

CREATE TABLE passengers (
    id BIGSERIAL PRIMARY KEY REFERENCES users(id),
    "name" varchar NOT NULL,
    surname varchar NOT NULL,
    data_birth DATE NOT NULL,
    gender varchar NOT NULL,
    photo varchar NOT NULL
);