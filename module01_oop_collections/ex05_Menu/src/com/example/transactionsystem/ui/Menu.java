package com.example.transactionsystem.ui;

import com.example.transactionsystem.exception.IllegalTransactionException;
import com.example.transactionsystem.exception.TransactionNotFoundException;
import com.example.transactionsystem.exception.UserNotFoundException;
import com.example.transactionsystem.model.Transaction;
import com.example.transactionsystem.model.TransferCategory;
import com.example.transactionsystem.model.User;
import com.example.transactionsystem.service.TransactionsService;

import java.util.Objects;
import java.util.Scanner;

public class Menu {
	//--[class fields]----------------------------------------------------------
	private final TransactionsService service;

	private final boolean	devMode;
	private boolean	showMenu = true;

	private final Scanner scanner = new Scanner(System.in);

	//--[constructor]-----------------------------------------------------------
	public Menu(String profile) {
		Objects.requireNonNull(profile, "Profile cannot be null");

		if (!profile.equals("dev") && !profile.equals("production")) {
			throw new IllegalArgumentException("Only accept profile: dev or production");
		}
		this.devMode = profile.equals("dev");
		service = TransactionsService.getInstance();
	}

	//--[methods]---------------------------------------------------------------
	public void start() {
		while (true) {
			if (showMenu) {
				printMenu();
				showMenu = false;
			}

			System.out.print("->");
			int choice = readInt();

			processUserChoice(choice);
			System.out.println("---------------------------------------------");
		}
	}

	private void printMenu() {
		String menuDev = """
				1. Add a user
				2. View user balances
				3. Perform a transfer
				4. View all transactions for a specific user
				5. DEV - remove a transfer by ID
				6. DEV - check transfer validity
				7. Finish execution
				""";
		String menuProduction = """
				1. Add a user
				2. View user balances
				3. Perform a transfer
				4. View all transactions for a specific user
				5. Finish execution
				""";
		if (devMode) {
			System.out.println(menuDev);
		} else {
			System.out.println(menuProduction);
		}
	}

	private int readInt() {
		while (true) {
			try {
				return Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input, please enter a number");
				System.out.print("-> ");
			}
		}
	}

	private void processUserChoice(int choice) {
		if ((!devMode && !(choice >= 1 && choice <= 5))
				|| devMode && !(choice >= 1 && choice <= 7)){
			System.out.println("Invalid choice, refer the menu");
			return ;
		}
		switch (choice) {
			case 1: promptAddUser(); break;
			case 2: promptViewUserBalance(); break;
			case 3: promptPerformTransfer(); break;
			case 4: promptViewUserTransactionHistory(); break;
			case 5:
				if (devMode)
					promptRemoveTransaction();
				else
					System.exit(0);
				break;
			case 6: inspectFailedTransactions(); break;
			case 7: System.exit(0); break;
		}
		showMenu = true;
	}

	private void promptAddUser() {
		System.out.println("Enter a user name and a balance");
		System.out.print("-> ");

		try {
			//trim trailing "'"
			String input = scanner.nextLine().trim().replaceAll("^'+|'+$", "");

			//splits on whitespace only if it is followed by a digit
			String[] token = input.split("\\s+(?=\\d)");

			if (token.length != 2) {
				System.out.println("Invalid input, try: 'name' 'balance'");
				return;
			}

			String name = token[0];
			Integer balance = Integer.parseInt(token[1].trim().replace("'",""));
			User newUser = service.addUser(name, balance);
			System.out.println("User with id = "
					+ newUser.getIdentifier() + " is added");
		} catch (NumberFormatException e) {
			System.out.println("Invalid balance");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

	private void promptViewUserBalance() {
		System.out.println("Enter a user ID");
		System.out.print("-> ");

		int targetID;
		if (scanner.hasNextInt()) {
			targetID = scanner.nextInt();
			scanner.nextLine();
		} else {
			System.out.println("Invalid input");
			return ;
		}

		try {
			User target = service.retrieveBalance(targetID);
			//could improve using DTO/MapStruct but overkill
			System.out.println(target.getName() + " - " + target.getBalance());
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private void promptPerformTransfer() {
		System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
		System.out.print("-> ");

		String input = scanner.nextLine().trim().replaceAll("^'+|'+$", "");
		String[] token = input.split("\\s+(?=\\d)");

		if (token.length != 3) {
			System.out.println("Invalid input, try: 'senderID' 'recipientID' 'amount'");
			return;
		}

		int senderID;
		int recipientID;
		int transferAmount;

		try {
			senderID = Integer.parseInt(token[0]);
			recipientID = Integer.parseInt(token[1]);
			transferAmount = Integer.parseInt(token[2]);
		} catch (NumberFormatException e) {
			System.out.println("Invalid input, IDs and amount must be numbers");
			return;
		}

		try {
			service.performTransaction(senderID, recipientID, transferAmount);
			System.out.println("The transfer is completed");
		} catch (UserNotFoundException | IllegalTransactionException e) {
			System.out.println(e.getMessage());
		}
	}

	private void promptViewUserTransactionHistory() {
		System.out.println("Enter a user ID");
		System.out.print("-> ");

		int userID;

		if (scanner.hasNextInt()) {
			userID = scanner.nextInt();
		} else {
			scanner.nextLine();
			System.out.println("Invalid input");
			return;
		}

		try {
			Transaction[] record = service.retrieveTransactionsHistory(userID);
			for (Transaction transaction: record) {
				if (transaction.getType().equals(TransferCategory.DEBITS)) {
					System.out.printf("To %s(id = %d) -%d with id = %s%n",
							transaction.getRecipient().getName(),
							transaction.getRecipient().getIdentifier(),
							transaction.getAmount(),
							transaction.getTransactionID());
				} else {
					System.out.printf("From %s(id = %d) +%d with id = %s%n",
							transaction.getSender().getName(),
							transaction.getSender().getIdentifier(),
							transaction.getAmount(),
							transaction.getTransactionID());
				}
			}
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			return ;
		}

		scanner.nextLine();
	}

	private void promptRemoveTransaction() {
		System.out.println("Enter a user ID and a transfer ID");
		System.out.print("-> ");

		String input = scanner.nextLine().trim();
		String[] token = input.split("\\s+");

		if (token.length != 2) {
			System.out.println("Invalid input");
			return;
		}

		try {
			int userID = Integer.parseInt(token[0]);
			String transferID = token[1];

			String statement = service.removeTransactionRecord(userID, transferID);
			System.out.println(statement);
		} catch (NumberFormatException e) {
			System.out.println("Invalid input");
		} catch (TransactionNotFoundException | UserNotFoundException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	private void inspectFailedTransactions() {
		try {
			Transaction[] failList = service.getFailedTransactionList();
			for (Transaction failedItem : failList) {
				System.out.printf("From %s(id = %d) to %s(id = %d) -%d with id = %s%n",
						failedItem.getSender().getName(),
						failedItem.getSender().getIdentifier(),
						failedItem.getRecipient().getName(),
						failedItem.getRecipient().getIdentifier(),
						failedItem.getAmount(),
						failedItem.getTransactionID());
			}
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}
}