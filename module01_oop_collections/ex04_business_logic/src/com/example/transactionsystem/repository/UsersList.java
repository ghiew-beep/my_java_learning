package com.example.transactionsystem.repository;

import com.example.transactionsystem.model.User;

public interface UsersList {
	public void addUser(User input);
	public User getUser(Integer id);
	public User getUser(int idx);
	public int getUserCount();
}