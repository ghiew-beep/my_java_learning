package edu.school21.chat.models;

import java.util.List;
import java.util.Objects;

public class User {
	private Long id;
	private String login;
	private String password;
	private List<Chatroom> createdRooms;
	private List<Chatroom> socializedRooms;

	//--[Constructor]-----------------------------------------------------------
	public User() {}

	public User(Long id, String login, String password,
				List<Chatroom> createdRooms, List<Chatroom> socializedRooms) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.createdRooms = createdRooms;
		this.socializedRooms = socializedRooms;
	}

	//--[Getter/Setter]---------------------------------------------------------
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Chatroom> getCreatedRooms() {
		return createdRooms;
	}

	public void setCreatedRooms(List<Chatroom> createdRooms) {
		this.createdRooms = createdRooms;
	}

	public List<Chatroom> getSocializedRooms() {
		return socializedRooms;
	}

	public void setSocializedRooms(List<Chatroom> socializedRooms) {
		this.socializedRooms = socializedRooms;
	}

	//--[Override equals(), hashCode(), toString()]-----------------------------
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode():0;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", login='" + login + '\'' +
				", password='" + password + '\'' +
				", createdRoomsCount=" + (createdRooms != null ? createdRooms.size() : 0) +
				", socializedRoomsCount=" + (socializedRooms != null ? socializedRooms.size() : 0) +
				'}';
	}
}