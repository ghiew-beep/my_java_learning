package com.example.transactionsystem.app;

import com.example.transactionsystem.model.User;
import com.example.transactionsystem.repository.UsersArrayList;

public class Program {
	public static void printUserInfo(User who) {
		System.out.printf("%-15s: %-15s%n", "ID", who.getIdentifier());
		System.out.printf("%-15s: %-15s%n", "Name", who.getName());
		System.out.printf("%-15s: %-15s%n%n", "Balance(USD)", who.getBalance());
	}
	public static void main(String[] args) {
		UsersArrayList list = new UsersArrayList();
		list.add(new User("John Kennedy", 1000));
		list.add(new User("Sally Barbara", 5));

		printUserInfo(list.getUserByIndex(0));
		printUserInfo(list.getUserByIndex(1));

		User u1 = list.getUserByIndex(0);
		User u2 = list.getUserByIndex(1);
		u1.sendMoney(u2, 200);

		printUserInfo(list.getUserByIndex(0));
		printUserInfo(list.getUserByIndex(1));
	}
}
