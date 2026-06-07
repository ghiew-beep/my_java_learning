package edu.school21.chat.app;

import edu.school21.chat.exception.NotSavedSubEntityException;
import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

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

			// check data.sql, make sure user already added to said room
			// the targetId below refers to message id in table messages
			long targetId = 4;

			Optional<Message> existingMsg = repository.findById(targetId);
			if (!existingMsg.isPresent()) {
				System.err.println("Invalid message id: " + targetId);
			}
			System.out.println("== Before update:\n" + existingMsg.get());

			Message message = existingMsg.get();

			// Can set text/message_date to null only
			message.setText("I am here to chew bubble gum and kick ass");
			// message.setText(null);
			message.setDateTime(null);
			repository.update(message);

			System.out.println("\n\n\n== Post update:");
			existingMsg = repository.findById(targetId);
			if (!existingMsg.isPresent()) {
				System.err.println("Invalid message id: " + targetId);
			}
			System.out.println(existingMsg.get());

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}