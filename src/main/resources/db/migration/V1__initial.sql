CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email varchar NOT NULL UNIQUE,
    "password" varchar NOT NULL
);

CREATE TABLE passengers (
    user_id BIGSERIAL PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    first_name varchar NOT NULL,
    last_name varchar NOT NULL,
    data_birth DATE NOT NULL,
    gender varchar NOT NULL,
    photo varchar NOT NULL
);