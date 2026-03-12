package com.example.transactionsystem.repository;

import com.example.transactionsystem.exception.UserNotFoundException;
import com.example.transactionsystem.model.User;

public class UsersArrayList implements UsersList {
	private static final UsersArrayList INSTANCE = new UsersArrayList();
	private static final int defaultArraySize = 10;
	private static User[] users = new User[defaultArraySize];
	private static int userCount = 0;

	private UsersArrayList() {}

	public static UsersArrayList getInstance() {
		return INSTANCE;
	}

	@Override
	public void addUser(User input) {
		//if full, extend ary
		if (userCount == users.length) {
			int newLength = users.length + users.length/2;
			User[] neoList = new User[newLength];

			System.arraycopy(users, 0, neoList, 0, users.length);
			users = neoList;
		}
		//take in new user
		users[userCount] = input;
		userCount++;
	}

	@Override
	public User getUser(Integer id){
		for (int i = 0; i < userCount; i++) {
			if (users[i].getIdentifier().equals(id)) {
				return users[i];
			}
		}
		throw new UserNotFoundException("User with ID: " + id + " not found");
	}

	@Override
	public User getUser(int idx) {
		if (idx < 0 || idx >= userCount) {
			throw new UserNotFoundException("Invalid user index: " + idx);
		}
		return users[idx];
	}

	@Override
	public int getUserCount() {
		return userCount;
	}
}