# First revoke privileges
sudo -u postgres psql -c "REASSIGN OWNED BY chat_app TO postgres;"
sudo -u postgres psql -c "DROP OWNED BY chat_app;"

# Then drop user
sudo -u postgres psql -c "DROP USER chat_app;"

# Drop the database
sudo -u postgres psql -c "DROP DATABASE chat;"

# Now drop the user
sudo -u postgres psql -c "DROP USER chat_app;"


sudo -u postgres psql -l


sudo -u postgres psql <<EOF
CREATE USER chat_app WITH PASSWORD 'chat123';
CREATE DATABASE chat OWNER chat_app;
\c chat
ALTER SCHEMA public OWNER TO chat_app;
GRANT ALL ON SCHEMA public TO chat_app;
EOF

# Create table under DB 'chat'
psql -U chat_app -h localhost -d chat -c "CREATE TABLE test (id INT);"

# Display tables under DB 'chat'
psql -U chat_app -h localhost -d chat -c "\dt"

# Drop said table under a DB
psql -U chat_app -h localhost -d chat -c "DROP TABLE test;"


# to execute sql script
psql -U user_name -h localhost -d chat -f src/main/resources/schema.sql
psql -U user_name -h localhost -d chat -f src/main/resources/data.sql