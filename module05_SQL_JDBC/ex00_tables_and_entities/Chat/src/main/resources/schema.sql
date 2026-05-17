-- Clean slate per run
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS users_chatrooms CASCADE;
DROP TABLE IF EXISTS chatrooms CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE chatrooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE users_chatrooms (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    chatroom_id BIGINT NOT NULL REFERENCES chatrooms(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id,chatroom_id)
);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    room_id BIGINT NOT NULL REFERENCES chatrooms(id) ON DELETE CASCADE,
    test TEXT NOT NULL,
    message_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_messages_author ON messages(author_id);
CREATE INDEX idx_messages_room ON messages(room_id);
CREATE INDEX idx_chatrooms_owner ON chatrooms(owner_id);