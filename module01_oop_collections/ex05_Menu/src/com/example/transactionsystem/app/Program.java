package com.example.transactionsystem.app;

import com.example.transactionsystem.ui.Menu;

public class Program {
	public static void main(String[] args) {
		if (args.length != 1 || !args[0].startsWith("--profile=")) {
			System.out.println("Try 'Program --profile=dev'" +
					" or 'Program --profile=production'");
			return ;
		}
		String profile = args[0].substring("--profile".length());
		try {
			Menu menu = new Menu(profile);
			menu.start();
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}
