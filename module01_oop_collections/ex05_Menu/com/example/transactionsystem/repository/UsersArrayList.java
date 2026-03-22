package com.example.transactionsystem.repository;

import com.example.transactionsystem.model.User;
import com.example.transactionsystem.exception.UserNotFoundException;

public class UsersArrayList implements UsersList{
	//fields
	private static final int defaultSize = 10;
	private User[] list = new User[defaultSize];
	private int userCount = 0;

	//constructor
	public UsersArrayList() {}

	//methods
	public void add(User user) {
		if (userCount == list.length) {
			int new_length = (int)(1.5 * list.length);
			User[] new_list = new User[new_length];

			for (int i = 0; i < userCount; i++) {
				new_list[i] = list[i];
			}
			list = new_list;
		}
		list[userCount] = user;
		++userCount;
	}

	public User getUserByID(int userID) {
		if (userID <= 0 || userID > userCount) {
			throw new UserNotFoundException("Valid range: 1 - " + userCount);
		}
		return list[userID - 1];
	}

	public User getUserByIndex(int index) {
		if (index < 0 || index >= userCount) {
			throw new IndexOutOfBoundsException("Valid range: 0 - " + (userCount - 1));
		}
		return list[index];
	}

	public int	getUserCount() { return userCount; }
}