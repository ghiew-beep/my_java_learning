package com.example.transactionsystem.model;

public class User {
	//fields
	private static int nextID = 1;
	private int identifier;
	private String name;
	private Integer balance;

	//constructor
		//disable User x = new User{}, must require name and balance
	private User() {};
	public User(String name, Integer balance) {
		if (balance < 0) {
			throw new IllegalArgumentException("Initial balance for new user cannot be negative");
		}
		this.identifier = nextID++;
		this.name = name;
		this.balance = balance;
	}

	//method
		//return Integer instead of int due to main::println uses generics
		//generics cannot work with primitive
	public Integer getIdentifier() { return identifier; }
	public String getName() { return name; }
	public Integer getBalance() { return balance; }

	private void receiveMoney(Integer amount) {
		balance += amount;
	}

	public void sendMoney(User recipient, Integer amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Transaction unsuccessful: Outgoing amount cannot be less than or equal to zero");
		}
		if (amount > this.balance) {
			System.out.println("Transaction unsuccessful: Insufficient balance!");
			return ;
		}
		//later exe - validate if recipient exists
		balance -= amount;
		recipient.receiveMoney(Math.abs(amount));
		//later exe - create transaction record
		System.out.println("Transaction successful!\n");
	}
}