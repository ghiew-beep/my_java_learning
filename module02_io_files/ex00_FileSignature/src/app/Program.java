package app;

import exception.SignatureNotFoundException;
import utility.SignatureLoader;
import utility.SignatureAnalyzer;
import utility.ResultWriter;
import model.FileSignature;

import java.io.IOException;
import java.util.Scanner;

public class Program {
	public static void main(String[] args) {

		//--[fields]------------------------------------------------------------
		SignatureLoader loader = new SignatureLoader();
		SignatureAnalyzer analyzer = new SignatureAnalyzer();
		ResultWriter writer = new ResultWriter("result.txt");

		final String EXIT_TAG = "42";
		Scanner scanner = new Scanner(System.in);
		String userInput;

		//load the signatures the program is expected to know
		try {
			loader.loadSignature("signatures.txt");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		//--[program flow]------------------------------------------------------
		while (true){
			System.out.println("-> ");

			userInput = scanner.nextLine();
			if (userInput.isEmpty()) {
				continue;
			}

			//input "42" detected, end the program
			if (userInput.equals(EXIT_TAG)) {
				System.exit(0);
			}

			if (analyzer.isMatched(loader.getSignature(), userInput)) {
				String type = analyzer.getType();
				//writer append type to result.txt
			}
		}
	}
}