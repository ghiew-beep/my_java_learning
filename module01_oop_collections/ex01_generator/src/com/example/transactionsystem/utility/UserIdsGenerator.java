package com.example.transactionsystem.utility;

public class UserIdsGenerator {
	//fields
	private static Integer nextID = 0;
	private static final UserIdsGenerator INSTANCE = new UserIdsGenerator();

	//constructor
	private UserIdsGenerator() {}

	//method
	public static UserIdsGenerator getInstance() { return INSTANCE; }
	public Integer generateID() { return ++nextID; }
}