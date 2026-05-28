package edu.school21.chat.repositories;

import edu.school21.chat.models.Message;
import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.User;

import javax.sql.DataSource;
import java.util.Optional;
import java.sql.*;

//2. Implement DAO (Repository) pattern
public class MessagesRepositoryJdbcImpl implements MessagesRepository {

	private final DataSource dataSource;

	//8. Implement constructor injection for dependencies
	public MessagesRepositoryJdbcImpl(DataSource dataSource){
		this.dataSource = dataSource;
	}

	@Override
	public Optional<Message> findById(Long id) {
		//3. Write JOIN queries across multiple tables
		//4. Use PreparedStatement to prevent SQL injection
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

		//10. Handle SQLException appropriately
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, id);

			//5. Map ResultSet to Java objects manually
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

					Message message = new Message(
							resultSet.getLong("message_id"),
							author,
							room,
							resultSet.getString("message_text"),
							resultSet.getTimestamp("message_date").toLocalDateTime()
					);

					return Optional.of(message);
				}
			}
		} catch (SQLException e) {
			System.err.println("Database error while finding message by ID: " + e.getMessage());
		}
		//6. Use Optional to handle null safety
		return Optional.empty();
	}
}
