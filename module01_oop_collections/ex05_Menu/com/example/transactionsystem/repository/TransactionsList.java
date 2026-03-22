package com.example.transactionsystem.repository;

import com.example.transactionsystem.model.Transaction;

public interface TransactionsList {
	void add(Transaction item);
	void remove(String UUID_identifier);

	/**
	 * Convert the underlying data structure to an array
	 *
	 * @return either the Transaction[] or null if the user has no transaction record
	 */
	Transaction[] toArray();
	boolean isEmpty();
}