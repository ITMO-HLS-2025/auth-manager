CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(32) NOT NULL,
    theatre_id BIGINT
);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO app_user (username, password_hash, role, theatre_id)
VALUES ('admin', crypt('admin123', gen_salt('bf')), 'ADMIN', NULL);
