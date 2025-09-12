CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_send BOOLEAN NOT NULL DEFAULT FALSE
    );

CREATE TABLE user_profile
(
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    address VARCHAR(255),
    favorite_pet VARCHAR(100)

    CONSTRAINT fk_user_profile_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,

    CONSTRAINT unique_user_id UNIQUE (user_id)
);

CREATE TABLE kafka_message
(
    id      SERIAL PRIMARY KEY,
    message VARCHAR(255)
);