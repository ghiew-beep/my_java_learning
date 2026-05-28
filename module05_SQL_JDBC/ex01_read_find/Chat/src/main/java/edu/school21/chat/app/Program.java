package main.java.edu.school21.chat.app;

import edu.school21.chat.models.Message;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Optional;
import java.util.Scanner;

public class Program {
	public static void main(String[] args) {

		//1. Configure connection pool using HikariCP
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/chat");
		config.setUsername("chat_app");
		config.setPassword("chat123");
		config.setMaximumPoolSize(10);

		//8. Implement constructor injection for dependencies
		try (HikariDataSource dataSource = new HikariDataSource(config)) {
			MessagesRepository repository = new MessagesRepositoryJdbcImpl(dataSource);

			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter a message ID");
			System.out.print("-> ");

			long id = scanner.nextLong();

			Optional<Message> optionalMessage = repository.findById(id);

			if (optionalMessage.isPresent()) {
				Message message = optionalMessage.get();
				System.out.println(message);
			} else {
				System.out.println("Message with ID = " + id + " not found");
			}

			scanner.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}