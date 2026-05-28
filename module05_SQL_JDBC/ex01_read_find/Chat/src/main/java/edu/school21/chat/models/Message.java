package edu.school21.chat.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
	private Long id;
	private User author;          // many-to-one: one user can write many messages
	private Chatroom room;        // many-to-one: one room can have many messages
	private String text;
	private LocalDateTime dateTime;

	//--[Constructor]-----------------------------------------------------------
	public Message() {}

	public Message(Long id, User author, Chatroom room, String text, LocalDateTime dateTime) {
		this.id = id;
		this.author = author;
		this.room = room;
		this.text = text;
		this.dateTime = dateTime;
	}

	//--[Getter/Setter]---------------------------------------------------------
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Chatroom getRoom() {
		return room;
	}

	public void setRoom(Chatroom room) {
		this.room = room;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	//--[Override equals(), hashCode(), toString()]-----------------------------
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Message message = (Message) o;
		return Objects.equals(id, message.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Message: {\n" +
				"id-" + id + ",\n" +
				"author={" + (author != null ? author.toShortString(): "null") + "}\n" +
				"room={" + (room != null ? room.toShortString() : "null") + "},\n" +
				"text=\"" + text + "\",\n" +
				"dateTime=" + dateTime + "\n" +
				'}';
	}
}