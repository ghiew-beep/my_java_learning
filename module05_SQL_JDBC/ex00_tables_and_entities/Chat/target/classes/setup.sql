-- setup.sql - Run with: sudo -u postgres psql -f setup.sql

-- 1. Create database
CREATE DATABASE chat;

-- 2. Create user
CREATE USER chat_app WITH PASSWORD 'chat123';

-- 3. Grant database connection
GRANT CONNECT ON DATABASE chat TO chat_app;

-- 4. Connect to chat database
\c chat

-- 5. Grant schema usage AND creation privileges
GRANT USAGE, CREATE ON SCHEMA public TO chat_app;

-- 6. Set as default for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO chat_app;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT USAGE ON SEQUENCES TO chat_app;

-- 7. Verify (optional)
\du chat_app
\dp