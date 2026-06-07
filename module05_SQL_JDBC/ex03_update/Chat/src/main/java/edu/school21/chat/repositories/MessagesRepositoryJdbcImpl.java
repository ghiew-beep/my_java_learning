package edu.school21.chat.repositories;

import edu.school21.chat.models.Message;
import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.User;
import edu.school21.chat.exception.NotSavedSubEntityException;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Optional;
import java.sql.*;

// Implement DAO (Repository) pattern
public class MessagesRepositoryJdbcImpl implements MessagesRepository {

	private final DataSource dataSource;

	// Implement constructor injection for dependencies
	public MessagesRepositoryJdbcImpl(DataSource dataSource){
		this.dataSource = dataSource;
	}

	@Override
	public Optional<Message> findById(Long id) {
		// Write JOIN queries across multiple tables
		// Use PreparedStatement to prevent SQL injection
		String sql = "SELECT " +
				"m.id AS message_id, " +
				"m.text AS message_text, " +
				"m.message_date AS message_date, " +
				"u.id AS author_id, " +
				"u.login AS author_login, " +
				"u.password AS author_password, " +
				"c.id AS room_id, " +
				"c.name AS room_name " +
				"FROM messages m " +
				"JOIN users u ON m.author_id = u.id " +
				"JOIN chatrooms c ON m.room_id = c.id " +
				"WHERE m.id = ?";

		// Handle SQLException appropriately
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, id);

			// Map ResultSet to Java objects manually
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {

					User author = new User(
							resultSet.getLong("author_id"),
							resultSet.getString("author_login"),
							resultSet.getString("author_password"),
							null,
							null
					);

					Chatroom room = new Chatroom(
							resultSet.getLong("room_id"),
							resultSet.getString("room_name"),
							null,
							null
					);

					// updated to handle null time stamp
					Timestamp timestamp = resultSet.getTimestamp("message_date");
					LocalDateTime dateTime = timestamp != null ? timestamp.toLocalDateTime() : null;

					Message message = new Message(
							resultSet.getLong("message_id"),
							author,
							room,
							resultSet.getString("message_text"),
							dateTime
					);

					return Optional.of(message);
				}
			}
		} catch (SQLException e) {
			System.err.println("Database error while finding message by ID: " + e.getMessage());
		}
		// Use Optional to handle null safety
		return Optional.empty();
	}

	@Override
	public void save(Message message) throws NotSavedSubEntityException {
		// Basic null check
		if (message.getAuthor() == null || message.getAuthor().getId() == null) {
			throw new NotSavedSubEntityException("Author ID is null");
		}

		if (message.getRoom() == null || message.getRoom().getId() == null) {
			throw new NotSavedSubEntityException("Chatroom ID is null");
		}

		// SQL insert statement
		String sql = "INSERT INTO messages (text, author_id, room_id, message_date)" +
						"VALUES (?, ?, ?, ?)";

		// try-with-resources(connection)
		try (Connection connection = dataSource.getConnection()) {
			// begin transaction
			connection.setAutoCommit(false);

			// check if user exists in db
			if (!userExists(connection, message.getAuthor().getId())) {
				connection.rollback();
				throw new NotSavedSubEntityException("Author not found in DB");
			}
			// check if chatroom exists in db
			if (!chatroomExists(connection, message.getRoom().getId())) {
				connection.rollback();
				throw new NotSavedSubEntityException("chatroom not found in DB");
			}
			// Check if 'author' already joined said 'chatroom'
			if (!userBelongsToRoom(connection, message.getAuthor().getId(), message.getRoom().getId())) {
				connection.rollback();
				throw new NotSavedSubEntityException("User does not belong in said chatroom");
			}

			// PreparedStatement, get generated key so can perform System.out.println(message.getId()) post save as per module required
			try (PreparedStatement statement = connection.prepareStatement(
					sql, Statement.RETURN_GENERATED_KEYS)) {

				// can also do statement.setObject(index, value) for all four but perhaps less readable in future tbs
				statement.setString(1, message.getText());
				statement.setLong(2, message.getAuthor().getId());
				statement.setLong(3, message.getRoom().getId());
				statement.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));

				int affectedRows = statement.executeUpdate();

				if (affectedRows == 0) {
					connection.rollback();
					throw new NotSavedSubEntityException(
							"Creating message failed, no rows affected"
					);
				}

				// Resultset
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						// remember from testcase in main(), message.id is set to null
						message.setId(generatedKeys.getLong(1));
					} else {
						connection.rollback();
						throw new NotSavedSubEntityException(
								"Creating message failed, no ID obtained"
						);
					}
				}
			}
			// save the changes
			connection.commit();
		} catch (SQLException e) {
			throw new NotSavedSubEntityException(
					"Database error while saving message: " + e.getMessage(), e
			);
		}
	}

	public void update(Message message) throws NotSavedSubEntityException {
		// Basic null check
		if (message.getId() == null) {
			throw new NotSavedSubEntityException(
					"Cannot update message with null ID (message not saved yet)"
			);
		}

		if (message.getAuthor() != null && message.getAuthor().getId() == null) {
			throw new NotSavedSubEntityException(
					"Author ID is null - author must be saved to database"
			);
		}

		if (message.getRoom() != null && message.getRoom().getId() == null) {
			throw new NotSavedSubEntityException(
					"Chatroom ID is null - chatroom must be saved to database"
			);
		}

		// SQL insert statement
		String sql = "UPDATE messages SET " +
						"author_id = ?, " +
						"room_id = ?, " +
						"text = ?, " +
						"message_date = ? " +
						"WHERE id = ?";

		try (Connection connection = dataSource.getConnection()) {
			connection.setAutoCommit(false);

			if (!messageExists(connection, message.getId())) {
				connection.rollback();
				throw new NotSavedSubEntityException("Message is not existing in DB");
			}
			// check if user exists in db
			if (!userExists(connection, message.getAuthor().getId())) {
				connection.rollback();
				throw new NotSavedSubEntityException("Author not found in DB");
			}
			// check if chatroom exists in db
			if (!chatroomExists(connection, message.getRoom().getId())) {
				connection.rollback();
				throw new NotSavedSubEntityException("chatroom not found in DB");
			}
			// Check if 'author' already joined said 'chatroom'
			if (!userBelongsToRoom(connection, message.getAuthor().getId(), message.getRoom().getId())) {
				connection.rollback();
				throw new NotSavedSubEntityException("User does not belong in said chatroom");
			}

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setLong(1, message.getAuthor().getId());
				statement.setLong(2, message.getRoom().getId());
				statement.setString(3, message.getText());

				if (message.getDateTime() == null) {
					statement.setNull(4, Types.TIMESTAMP);
				} else {
					statement.setTimestamp(4, Timestamp.valueOf(message.getDateTime()));
				}

				statement.setLong(5, message.getId());

				int affectedRows = statement.executeUpdate();

				if (affectedRows == 0) {
					connection.rollback();
					throw new NotSavedSubEntityException(
							"Update failed, no rows affected. Message ID: " + message.getId()
					);
				}

				// safety check, should not happen
				if (affectedRows > 1) {
					connection.rollback();
					throw new NotSavedSubEntityException(
							"Update affected " + affectedRows + " rows. Expected 1."
					);
				}

				connection.commit();

			} catch (SQLException e) {
				throw new NotSavedSubEntityException("Database error during update", e);
			}

		} catch (SQLException e) {
			throw new NotSavedSubEntityException("Database error during update", e);
		}
	}

	private boolean messageExists(Connection connection, Long msgId) throws SQLException {
		String sql = "Select 1 FROM messages WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, msgId);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	private boolean userExists(Connection connection, Long userId) throws SQLException {
		String sql = "Select 1 FROM users WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, userId);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	private  boolean chatroomExists(Connection connection, Long roomId) throws SQLException {
		String sql = "SELECT 1 FROM chatrooms WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, roomId);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	private boolean userBelongsToRoom(Connection connection, long userId, long roomId) throws SQLException {
		String sql = "SELECT 1 FROM users_chatrooms WHERE user_id = ? AND chatroom_id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, userId);
			statement.setLong(2, roomId);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		}
	}
}
