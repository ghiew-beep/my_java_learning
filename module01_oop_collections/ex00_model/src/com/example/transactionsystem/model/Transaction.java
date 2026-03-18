package com.example.transactionsystem.model;

import java.util.UUID;

public class Transaction {
	//fields
	private UUID transactionID;
	private User sender;
	private User recipient;
	private TransferCategory type;
	private Integer amount;

	//constructor
	public Transaction(User sender, User recipient, TransferCategory type, Integer amount) {
		this.transactionID = UUID.randomUUID();
		this.sender = sender;
		this.recipient = recipient;
		this.type = type;
		this.amount = amount;
	}

	//method
	public UUID getTransactionID() { return transactionID; }
	public User getSender() { return sender; }
	public User getRecipient() { return  recipient; }
	public TransferCategory getType() { return type; }
	public Integer getAmount() { return amount; }
}