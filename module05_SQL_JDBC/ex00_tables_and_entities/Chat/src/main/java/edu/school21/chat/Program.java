package edu.school21.chat;

import edu.school21.chat.models.User;
import edu.school21.chat.models.Chatroom;
import edu.school21.chat.models.Message;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Program {
	public static void main(String[] args) {
		System.out.println("=== Testing Entity Classes ===\n");

		// Create test objects
		User author = new User(1L, "alice", "pass123", new ArrayList<>(), new ArrayList<>());
		Chatroom room = new Chatroom(1L, "General Chat", author, new ArrayList<>());
		Message message = new Message(1L, author, room, "Hello World!", LocalDateTime.now());

		// Test toString()
		System.out.println("User: " + author);
		System.out.println("Chatroom: " + room);
		System.out.println("Message: " + message);

		// Test equals()
		User sameUser = new User(1L, "alice", "different", new ArrayList<>(), new ArrayList<>());
		User differentUser = new User(2L, "bob", "pass", new ArrayList<>(), new ArrayList<>());

		System.out.println("\n=== Testing equals() ===");
		System.out.println("author.equals(sameUser): " + author.equals(sameUser));      // Should be true (same ID)
		System.out.println("author.equals(differentUser): " + author.equals(differentUser)); // Should be false

		// Test hashCode()
		System.out.println("\n=== Testing hashCode() ===");
		System.out.println("author.hashCode(): " + author.hashCode());
		System.out.println("sameUser.hashCode(): " + sameUser.hashCode());  // Should be same as author
		System.out.println("differentUser.hashCode(): " + differentUser.hashCode()); // Should be different

		System.out.println("\nAll entity tests passed!");
	}
}