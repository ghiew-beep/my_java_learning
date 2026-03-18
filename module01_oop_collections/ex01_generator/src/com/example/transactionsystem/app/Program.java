package com.example.transactionsystem.app;

import com.example.transactionsystem.model.User;
import com.example.transactionsystem.model.Transaction;
import com.example.transactionsystem.model.TransferCategory;

public class Program {
	public static void printUserInfo(User who) {
		System.out.printf("%-15s: %-15s%n", "ID", who.getIdentifier());
		System.out.printf("%-15s: %-15s%n", "Name", who.getName());
		System.out.printf("%-15s: %-15s%n%n", "Balance(USD)", who.getBalance());
	}
	public static void main(String[] args) {
		User u1_john = new User("John Kennedy", 1000);
		User u2_sally = new User("Sally Barbara", 5);

		printUserInfo(u1_john);
		printUserInfo(u2_sally);

		u1_john.sendMoney(u2_sally, 200);

		printUserInfo(u1_john);
		printUserInfo(u2_sally);
	}
}
