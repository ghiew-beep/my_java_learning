package com.example.transactionsystem.service;

import com.example.transactionsystem.exception.IllegalTransactionException;
import com.example.transactionsystem.exception.TransactionNotFoundException;
import com.example.transactionsystem.exception.UserNoTransactionException;
import com.example.transactionsystem.exception.UserNotFoundException;
import com.example.transactionsystem.model.Transaction;
import com.example.transactionsystem.model.TransferCategory;
import com.example.transactionsystem.model.User;
import com.example.transactionsystem.repository.TransactionList;
import com.example.transactionsystem.repository.UsersArrayList;
import com.example.transactionsystem.repository.UsersList;

import java.util.UUID;

public class TransactionService {
	private static final UsersList lst = UsersArrayList.getInstance();
	private static final String APIkey = "ccc6dcc8ecd959d09a9f2e082e2e9fb8";

	private TransactionService() {};

	private static class Holder {
		 private static final TransactionService INSTANCE = new TransactionService();
	}

	public static TransactionService getInstance() {
		return Holder.INSTANCE;
	}

	//add a user
	public void addUser(User input) {
		lst.addUser(input);
	}

	//retrieve user balance
	public Integer getBalance(Integer id) {
		return (lst.getUser(id).getBalance());
	}

	//perform transfer
	public void makeTransaction(User sender, User recipient, TransferCategory value, Integer transferAmount)
			throws IllegalTransactionException {
		transferAmount = Math.abs(transferAmount);
		UUID transactionID = UUID.randomUUID();
		switch (value) {
			case DEBITS -> {
				if (getBalance(sender.getIdentifier()) < transferAmount) {
					throw new IllegalTransactionException("User: " + sender.getName() + " has insufficient fund!");
				}
				sender.addTransaction(APIkey, new Transaction(sender, recipient, transactionID, value, transferAmount));
				sender.debit(APIkey, transferAmount);
				recipient.addTransaction(APIkey, new Transaction(sender, recipient, transactionID, transferAmount));
				recipient.credit(APIkey, transferAmount);
			}
			case FAILURE -> {
				sender.addTransaction(APIkey, new Transaction(sender, recipient, transactionID, TransferCategory.FAILURE, transferAmount));
			}
		}
	}

	//get user transaction record in ary form via user name and user ID
	public Transaction[] getTransactionRecord(User subject) {
		if (!lst.userExists(subject.getIdentifier())) {
			throw new UserNotFoundException("User not found");
		}
		if (subject.getTransactionCount() < 0) {
			throw new UserNoTransactionException("User has no transaction record");
		}
		return (subject.getTransactionHistory());
	}

	//inspect list of failed transaction per user
	public Transaction[] getFailedTransactionRecord(User subject) {
		if (!lst.userExists(subject.getIdentifier())) {
			throw new UserNotFoundException("User not found");
		}
		if (subject.getTransactionCount() < 0) {
			throw new UserNoTransactionException("User has no transaction record");
		}
		return (subject.getFailedTransactionHistory());
	}

	//remove record via userID and transactionID
	public void removeRecord(Integer userID, UUID transactionID) {
		try {
			User temp = lst.getUser(userID);
			if (temp.getTransactionCount() <= 0) {
				System.out.println("User has no transaction record");
				return ;
			}
			temp.removeTransaction(transactionID);
		} catch (UserNotFoundException e) {
			System.out.println("User not found");
		}
	}
}