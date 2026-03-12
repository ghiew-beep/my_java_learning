package com.example.transactionsystem.model;

import com.example.transactionsystem.repository.TransactionList;
import com.example.transactionsystem.repository.TransactionsLinkedList;
import com.example.transactionsystem.utility.UserIdsGenerator;

import java.util.UUID;

public class User {
	private Integer identifier;
	private String name;
	private Integer balance;
	private TransactionList lst = new TransactionsLinkedList();
	private int transactionCount;
	private final String APIkey = "ccc6dcc8ecd959d09a9f2e082e2e9fb8";

	private User () {}

	public User (String name, Integer balance) {
		this.identifier = UserIdsGenerator.getInstance().generateId();
		this.name = name;
		this.balance = balance;
		this.transactionCount = 0;
	}

	public Integer getIdentifier() { return identifier; }
	public String getName() { return name; }
	public Integer getBalance() { return balance; }

	public void	addTransaction(String APIkey, Transaction item) {
		if (this.APIkey.equals(APIkey)) {
			lst.add(item);
			transactionCount++;
		} else {
			System.out.println("Only authorized party can make transaction request");
		}
	}

	public void removeTransaction(UUID transactionID) {
		lst.remove(transactionID);
	}

	public void debit(String APIkey, Integer amount) {
		if (this.APIkey.equals(APIkey)) {
			balance-=amount;
		}
	}

	public void credit(String APIkey, Integer amount) {
		if (this.APIkey.equals(APIkey)) {
			balance+=amount;
		}
	}
}