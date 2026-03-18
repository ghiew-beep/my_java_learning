package com.example.transactionsystem.repository;

import com.example.transactionsystem.model.User;

interface UsersList {
	void add(User user);
	User getUserByID(int userID);
	User getUserByIndex(int index);
	int	getUserCount();
}