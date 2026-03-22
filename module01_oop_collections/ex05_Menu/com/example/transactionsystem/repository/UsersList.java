package com.example.transactionsystem.repository;

import com.example.transactionsystem.model.User;

public interface UsersList {
	void add(User user);

	/**
	 * Retrieves a user by their unique identifier.
	 *
	 * @param id the unique identifier of the user
	 * @return the User associated with the given id, never null
	 * @throws UserNotFoundException if no user exists with the given id
	 */
	User getUserByID(int userID);

	User getUserByIndex(int index);
	int	getUserCount();
}