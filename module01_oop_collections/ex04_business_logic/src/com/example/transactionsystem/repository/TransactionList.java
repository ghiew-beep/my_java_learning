package com.example.transactionsystem.repository;

import com.example.transactionsystem.model.Transaction;

import java.util.UUID;

public interface TransactionList {
	public void add(Transaction item);
	public void remove(UUID transactionID);
	public Transaction[] toArray();
	public Transaction[] extractFailedTransactionRecord();
}