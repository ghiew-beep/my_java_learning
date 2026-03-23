package com.example.transactionsystem.ui;

import java.util.Objects;

public class Menu {
	//--[class fields]----------------------------------------------------------
	String profile;
	Boolean	devMode = false;

	//--[constructor]-----------------------------------------------------------
	private Menu() {}

	public Menu(String profile) {
		Objects.requireNonNull(profile, "Profile cannot be null");

		if (profile.equals("dev") || profile.equals("production")) {
			this.profile = profile;
			if (profile.equals("dev")) {
				this.devMode = true;
			}
		} else {
			throw new IllegalArgumentException("Only accept profile: dev or production");
		}
	}

	//--[methods]---------------------------------------------------------------
	public void start() {
		while (true) {

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
		} else
			System.out.println(menuProduction);
	}
}