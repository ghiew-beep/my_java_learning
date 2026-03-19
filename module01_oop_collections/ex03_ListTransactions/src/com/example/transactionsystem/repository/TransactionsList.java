package com.example.transactionsystem.repository;

import com.example.transactionsystem.model.Transaction;

public interface TransactionsList {
	void add(Transaction item);
	void remove(String UUID_identifier);
	Transaction[] toArray();
}