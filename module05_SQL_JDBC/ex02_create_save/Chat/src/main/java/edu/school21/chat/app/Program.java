package edu.school21.chat.app;

import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Program {
	public static void main(String[] args) {

		// Configure connection pool using HikariCP
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/chat");
		config.setUsername("chat_app");
		config.setPassword("chat123");
		config.setMaximumPoolSize(10);

		// Implement constructor injection for dependencies
		try (HikariDataSource dataSource = new HikariDataSource(config)) {
			MessagesRepository repository = new MessagesRepositoryJdbcImpl(dataSource);

			System.out.println("=== EX02: Save Message ===\n");

			// User and room MUST exist in database (from data.sql)
			// To get positive feedback, make sure User belongs to said chatroom, check the insertion in data.sql
			User creator = new User(1L, "user", "user", new ArrayList<>(), new ArrayList<>());
			Chatroom room = new Chatroom(2L, "General", creator, new ArrayList<>());

			Message message = new Message(
					null,
					creator,
					room,
					"Hello from EX02!",
					LocalDateTime.now()
			);

			System.out.println("Before save:");
			System.out.println("  Message ID: " + message.getId());
			System.out.println("  Text: " + message.getText());
			System.out.println("  Author ID: " + message.getAuthor().getId());
			System.out.println("  Room ID: " + message.getRoom().getId());
			System.out.println();

			repository.save(message);

			System.out.println("After save:");
			System.out.println("  Message ID: " + message.getId());  // Should be auto-generated
			System.out.println("  Text: " + message.getText());
			System.out.println();

			// Verify the message was saved by retrieving it
			Optional<Message> saved = repository.findById(message.getId());
			if (saved.isPresent()) {
				System.out.println("✓ Verification: Message found in database!");
				System.out.println("  Retrieved message: \n" + saved.get());
			} else {
				System.out.println("✗ Verification: Message NOT found in database!");
			}

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}