package com.example.transactionsystem.ui;

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
			}
		}
	}

	private void processUserChoice(int choice) {
		if ((!devMode && (choice < 0 || choice > 5))
				|| devMode && (choice < 0 || choice > 7)){
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
			case 6: service.getFailedTransactionList(); break;
			case 7: System.exit(0); break;
		}
		showMenu = true;
	}
}