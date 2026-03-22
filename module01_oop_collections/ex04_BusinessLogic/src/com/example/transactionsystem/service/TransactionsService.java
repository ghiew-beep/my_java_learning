package com.example.transactionsystem.service;

import com.example.transactionsystem.exception.TransactionNotFoundException;
import com.example.transactionsystem.exception.UserNotFoundException;
import com.example.transactionsystem.exception.IllegalTransactionException;
import com.example.transactionsystem.model.Transaction;
import com.example.transactionsystem.model.TransferCategory;
import com.example.transactionsystem.model.User;
import com.example.transactionsystem.repository.TransactionsLinkedList;
import com.example.transactionsystem.repository.TransactionsList;
import com.example.transactionsystem.repository.UsersList;
import com.example.transactionsystem.repository.UsersArrayList;

import java.util.Objects;
import java.util.UUID;

public class TransactionsService {
	//--[class fields]----------------------------------------------------------
	//in this case, TransactionsService is made into a singleton
	private static final TransactionsService INSTANCE =
			new TransactionsService();
	private final UsersList userList = new UsersArrayList();
	private static final int serviceID = 1;
	private final TransactionsList failedTransactionList =
			new TransactionsLinkedList(serviceID);

	//--[class constructors]----------------------------------------------------
	//singleton pattern
	private TransactionsService() {}
	public static TransactionsService getInstance() { return INSTANCE; }

	//--[class methods]---------------------------------------------------------
	public void addUser(String username, Integer initialBalance)
			throws IllegalArgumentException {
		userList.add(new User(username, initialBalance));
	}

	public Integer retrieveBalance(int userID)
			throws UserNotFoundException {
		User reference = userList.getUserByID(userID);
		return reference.getBalance();
	}

	public void
	performTransaction(int senderID, int recipientID, Integer transferAmount)
			throws UserNotFoundException, IllegalTransactionException {
		User sender = userList.getUserByID(senderID);
		User recipient = userList.getUserByID(recipientID);
		Objects.requireNonNull(transferAmount, "Transfer amount is null");

		UUID transactionID = UUID.randomUUID();

		if (transferAmount < 10) {
			failedTransactionList.add(new Transaction(transactionID, sender, recipient, TransferCategory.DEBITS, transferAmount));
			throw new IllegalArgumentException("Transaction unsuccessful: Transfer amount must be $10 and above");
		} else if (transferAmount > 100000) {
			failedTransactionList.add(new Transaction(transactionID, sender, recipient, TransferCategory.DEBITS, transferAmount));
			throw new IllegalTransactionException(("Transaction unsuccessful: Transfer amount exceeded $100000"));
		}
		if (transferAmount > sender.getBalance()) {
			failedTransactionList.add(new Transaction(transactionID, sender, recipient, TransferCategory.DEBITS, transferAmount));
			throw new IllegalTransactionException("Transaction unsuccessful: Sender balance insufficient!");
		}

		sender.sendMoney(transactionID, recipient, transferAmount);
	}

	/**
	 *
	 * @param userID
	 * @return either Transaction[] or null if user has no transaction history
	 * @throws UserNotFoundException
	 */
	public Transaction[]
	retrieveTransactionsHistory(int userID) throws UserNotFoundException {
		User reference = userList.getUserByID(userID);
		Objects.requireNonNull(userList.getUserByID(userID).getTransactionsList(), "User " + userID + "has no transaction history");
		return reference.getTransactionsList();
	}

	public void removeTransactionRecord(int userID, String transactionID)
			throws UserNotFoundException, TransactionNotFoundException {
		userList.getUserByID(userID).getTransactionsListReference().remove(transactionID);
	}

	/**
	 *
	 * @return Transaction[] or null if there is zero failed transactions
	 */
	public Transaction[] getFailedTransactionList() {
		if (failedTransactionList.isEmpty()) {
			return null;
		}
		return failedTransactionList.toArray();
	}
}