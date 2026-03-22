package com.example.transactionsystem.app;

import com.example.transactionsystem.exception.IllegalTransactionException;
import com.example.transactionsystem.exception.TransactionNotFoundException;
import com.example.transactionsystem.exception.UserNotFoundException;
import com.example.transactionsystem.model.Transaction;
import com.example.transactionsystem.model.User;
import com.example.transactionsystem.service.TransactionsService;

import java.util.UUID;

public class Program {
	public static void main(String[] args) {
		//get the service instance
		TransactionsService service = TransactionsService.getInstance();

		//use of helpers to avoid try-catch block cluttering main()
		tryAddUser(service, "Badabuk", 1000);
		tryAddUser(service, "Sally Barbara", 5);
		tryGetUserBalance(service, 1);
		tryPerformTransaction(service, 1, 2, 1000);
		tryPerformTransaction(service, 1, 2, 1000);
		tryGetUserBalance(service, 1);
		tryRetrieveTransactionRecord(service, 1);
		tryRemoveTransactionRecord(service, 1, "26a77445-bd60-42b3-ade9-b56b09e0e7b2");
		tryCheckTransactionValidity(service);
	}

	public static void
	tryAddUser(TransactionsService service, String name, Integer initBalance) {
		try {
			service.addUser(name, initBalance);
			System.out.println("User " + name + " added to system");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void
	tryGetUserBalance(TransactionsService service, int userID) {
		try {
			System.out.println("User id = " + userID + " - Balance: " + service.retrieveBalance(userID));
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void
	tryPerformTransaction(
			TransactionsService service,
			int sender,
			int recipient,
			Integer transferAmount) {
		try {
			service.performTransaction(sender, recipient, transferAmount);
			System.out.println("Transaction successful!");
		} catch (UserNotFoundException | IllegalTransactionException e) {
			System.out.print(e.getMessage());
		}
	}

	public static void
	tryRetrieveTransactionRecord(TransactionsService service, int userID) {
		try {
			Transaction[] record = service.retrieveTransactionsHistory(userID);
			if (record == null) {
				System.out.println("User with id " + userID + "has no transaction history");
				return ;
			}
			for (Transaction transaction : record) {
				User recipient = transaction.getRecipient();
				System.out.println("To " + recipient.getName()
						+ "(id = " + recipient.getIdentifier() + ")-"
						+ transaction.getAmount() + " with id = "
						+ transaction.getTransactionID());
			}
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void tryRemoveTransactionRecord(
			TransactionsService service,
			int userID,
			String transactionID) {
		try {
			Transaction[] record = service.retrieveTransactionsHistory(userID);
			if (record == null) {
				System.out.println("This user has no transaction record");
				return ;
			}
			UUID target = UUID.fromString(transactionID);
			for (Transaction transaction : record) {
				if (transaction.getTransactionID().equals(target)) {
					System.out.println("Transfer to "
							+ transaction.getRecipient().getName()
							+ "(id = " + transaction.getRecipient().getIdentifier() + ") "
							+ transaction.getAmount() + " removed");
					service.removeTransactionRecord(userID, transactionID);
					return;
				}
			}
			throw new TransactionNotFoundException("No relevant transaction found for this user!");
		} catch (UserNotFoundException | TransactionNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid transaction ID format: " + transactionID);
		}
	}

	public static void
	tryCheckTransactionValidity(TransactionsService service) {
		Transaction[] failedList = service.getFailedTransactionList();

		if (failedList == null) {
			System.out.println("No failed transaction so far :)");
			return ;
		}
		System.out.println("Check results:");
		for (Transaction transaction: failedList) {
			System.out.printf(
					"%s(id = %d) has an unacknowledged transfer id = %s from %s(id = %d) for %d%n",
					transaction.getRecipient().getName(),
					transaction.getRecipient().getIdentifier(),
					transaction.getTransactionID(),
					transaction.getSender().getName(),
					transaction.getSender().getIdentifier(),
					transaction.getAmount());
		}
	}
}
