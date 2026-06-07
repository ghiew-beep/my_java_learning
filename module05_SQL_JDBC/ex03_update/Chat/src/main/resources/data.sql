INSERT INTO users (login, password) VALUES
('alice', 'pass123'),
('bob', 'secure456'),
('charlie', 'charlie789'),
('diana', 'diana321'),
('eve', 'eve654');

INSERT INTO chatrooms (name, owner_id) VALUES
('General Chat', 1),      -- owned by alice
('Gaming Lounge', 2),     -- owned by bob
('Tech Talk', 3),         -- owned by charlie
('Music Fans', 4),        -- owned by diana
('Movie Buffs', 5);       -- owned by eve

INSERT INTO users_chatrooms (user_id, chatroom_id) VALUES
(1, 1),  -- alice in General Chat
(1, 2),  -- alice in Gaming Lounge
(1, 3);  -- alice in Tech Talk

INSERT INTO users_chatrooms (user_id, chatroom_id) VALUES
(2, 1),  -- bob in General Chat
(2, 3),  -- bob in Tech Talk
(2, 4);  -- bob in Music Fans

INSERT INTO users_chatrooms (user_id, chatroom_id) VALUES
(3, 1),  -- charlie in General Chat
(3, 2),  -- charlie in Gaming Lounge
(3, 4),  -- charlie in Music Fans
(3, 5);  -- charlie in Movie Buffs

INSERT INTO users_chatrooms (user_id, chatroom_id) VALUES
(4, 1),  -- diana in General Chat
(4, 5);  -- diana in Movie Buffs

INSERT INTO users_chatrooms (user_id, chatroom_id) VALUES
(5, 2),  -- eve in Gaming Lounge
(5, 3);  -- eve in Tech Talk

INSERT INTO messages (author_id, room_id, text, message_date) VALUES
(1, 1, 'Hello everyone! Welcome to General Chat!', '2024-01-15 10:00:00'),
(2, 1, 'Hey alice, thanks for setting this up!', '2024-01-15 10:05:00'),
(3, 2, 'Anyone up for some gaming tonight?', '2024-01-15 14:30:00'),
(1, 2, 'I am! What game are we playing?', '2024-01-15 14:32:00'),
(5, 3, 'Just deployed my first Spring Boot app!', '2024-01-16 09:15:00'),
(3, 3, 'Congrats eve! Java is awesome!', '2024-01-16 09:20:00'),
(4, 5, 'Has anyone watched Dune 2?', '2024-01-16 20:00:00'),
(2, 4, 'Check out this new jazz album!', '2024-01-17 12:00:00');