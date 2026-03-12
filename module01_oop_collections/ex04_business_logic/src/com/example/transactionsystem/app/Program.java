package com.example.transactionsystem.app;

import com.example.transactionsystem.exception.IllegalTransactionException;
import com.example.transactionsystem.exception.TransactionNotFoundException;
import com.example.transactionsystem.model.Transaction;
import com.example.transactionsystem.model.TransferCategory;
import com.example.transactionsystem.model.User;
import com.example.transactionsystem.repository.UsersList;
import com.example.transactionsystem.repository.UsersArrayList;
import com.example.transactionsystem.service.TransactionService;

import java.util.UUID;

public class Program {
	public static void main(String[] args) {
		User p1 = new User("John", 1000);
		User p2 = new User("Kate", 100);

		System.out.println(p1.getName() + "'s balance is: " + p1.getBalance());
		System.out.println(p2.getName() + "'s balance is: " + p2.getBalance());

		System.out.println("p1 identifier: " + p1.getIdentifier());
		System.out.println("p1 identifier: " + p2.getIdentifier());

		UsersList lst = UsersArrayList.getInstance();
		lst.addUser(p1);
		lst.addUser(p2);

		System.out.println("Total user count: " + lst.getUserCount());

		//demo checked exception that will keep the program running even in the case of error
		UUID testing = UUID.randomUUID();
		try {
			p1.removeTransaction(testing);
		} catch(TransactionNotFoundException e) {
			System.out.println((e.getMessage()));
		}

		TransactionService agent = TransactionService.getInstance();
		agent.makeTransaction(p1, p2, TransferCategory.DEBITS, 100);

		System.out.println(p1.getName() + "'s balance is: " + p1.getBalance());
		System.out.println(p2.getName() + "'s balance is: " + p2.getBalance());

		//this last transaction will trigger error, how to prevent it from halting the program
		try {
			agent.makeTransaction(p1, p2, TransferCategory.DEBITS, 1000);
		} catch(IllegalTransactionException e) {
			System.out.println((e.getMessage()));
			agent.makeTransaction(p1, p2, TransferCategory.FAILURE, 1000);
		}

		System.out.println(p1.getName() + "'s balance is: " + p1.getBalance());
		System.out.println(p2.getName() + "'s balance is: " + p2.getBalance());

		Transaction[] record = agent.getTransactionRecord(p1);
		System.out.println(record.length);
		Transaction[] failedRecord = agent.getFailedTransactionRecord(p1);
		System.out.println(failedRecord.length);
	}
}