package com.example.transactionsystem.utility;

public class UserIdsGenerator {

	private static final UserIdsGenerator INSTANCE = new UserIdsGenerator();
	private static int lastId;

	//default ctor
	private UserIdsGenerator() {}

	//to create an instance of the generator but only once
	public static UserIdsGenerator getInstance() {
		return INSTANCE;
	}

	//generate next ID
	public int generateId() {
		return ++lastId;
	}
}