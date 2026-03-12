package com.example.transactionsystem.model;

import java.util.UUID;

public class Transaction {
	private final UUID identifier;
	private final User recipient;
	private final User sender;
	private final TransferCategory transfer_category;
	private final boolean transactionSuccess;
	private final Integer transfer_amount;

	//for debit
	public Transaction(User sender, User recipient, UUID transactionID, TransferCategory value, Integer transfer_amount) {
		this.identifier = transactionID;
		this.recipient = recipient;
		this.sender = sender;
		this.transfer_category = value;
		this.transfer_amount = transfer_amount;
		this.transactionSuccess = !value.equals(TransferCategory.FAILURE);
	}

	//for credit
	public Transaction(User sender, User recipient, UUID transactionID, Integer transfer_amount) {
		this.identifier = transactionID;
		this.recipient = recipient;
		this.sender = sender;
		this.transfer_category = TransferCategory.CREDITS;
		this.transfer_amount = transfer_amount;
		this.transactionSuccess = true;
	}

	public UUID getUUID() { return identifier; }
	public User getRecipient() { return recipient; }
	public User getSender() { return sender; }
	public TransferCategory getTransferCategory() { return transfer_category; }
	public Integer getTransferAmount() { return transfer_amount; }
	public boolean getTransactionStatus() { return transactionSuccess; }
}