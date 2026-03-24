package com.example.transactionsystem.model;

import com.example.transactionsystem.repository.TransactionsLinkedList;
import com.example.transactionsystem.utility.UserIdsGenerator;
import com.example.transactionsystem.repository.TransactionsList;

import java.util.Objects;
import java.util.UUID;

public class User {
	//fields
	private int identifier;
	private String name;
	private Integer balance;
	private TransactionsList transactionsList;

	//constructor
		//disable User x = new User{}, must require name and balance
	private User() {};
	public User(String name, Integer balance)
			throws IllegalArgumentException {

		if (name.isEmpty()) {
			throw new IllegalArgumentException("User name cannot be empty");
		}
		if (balance < 0) {
			throw new IllegalArgumentException(
					"Initial balance for new user cannot be negative");
		}

		this.identifier = UserIdsGenerator.getInstance().generateID();
		this.name = name;
		this.balance = balance;
		transactionsList = new TransactionsLinkedList(identifier);
	}

	//method
		//return Integer instead of int due to main::println uses generics
		//generics cannot work with primitive
	public Integer getIdentifier() { return identifier; }
	public String getName() { return name; }
	public Integer getBalance() { return balance; }

	private void receiveMoney(UUID transactionID, User sender, Integer amount) {
		balance += amount;
		transactionsList.add(new Transaction(transactionID, sender, this, TransferCategory.CREDITS, amount));
	}

	public void sendMoney(UUID transactionID, User recipient, Integer amount) {

		Objects.requireNonNull(transactionID, "Transaction ID is null");
		Objects.requireNonNull(recipient, "Recipient is null");
		Objects.requireNonNull(amount, "Transfer amount is null");

		if (amount <= 0) {
			throw new IllegalArgumentException("Transaction unsuccessful: Outgoing amount cannot be less than or equal to zero");
		}
		if (amount > this.balance) {
			System.out.println("Transaction unsuccessful: Insufficient balance!");
			return ;
		}

		balance -= amount;
		transactionsList.add(new Transaction(transactionID, this, recipient, TransferCategory.DEBITS, amount));
		recipient.receiveMoney(transactionID, this, Math.abs(amount));
	}

	/**
	 *
	 * @return {@link Transaction}[] or null if the user has no transaction record
	 */
	public Transaction[] getTransactionsList() {
		return transactionsList.toArray();
	}

	/**
	 *
	 * @return {@link TransactionsList} or null
	 */
	public TransactionsList getTransactionsListReference() {
		return transactionsList;
	}
}