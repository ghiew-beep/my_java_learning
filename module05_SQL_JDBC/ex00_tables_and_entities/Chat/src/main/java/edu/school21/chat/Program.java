package edu.school21.chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/chat";
		String user = "chat_app";  // or your username
		String password = "chat123";

		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			System.out.println("✅ Connected to PostgreSQL successfully!");
		} catch (SQLException e) {
			System.err.println("❌ Connection failed: " + e.getMessage());
		}
	}
}
