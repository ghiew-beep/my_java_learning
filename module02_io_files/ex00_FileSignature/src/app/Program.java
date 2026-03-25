package app;

import utility.SignatureLoader;
import utility.SignatureAnalyzer;
import utility.ResultWriter;

import java.io.IOException;
import java.util.Scanner;

public class Program {
	public static void main(String[] args) throws IOException {

		//--[fields]------------------------------------------------------------
		SignatureLoader loader = new SignatureLoader();
		SignatureAnalyzer analyzer = new SignatureAnalyzer();
		ResultWriter writer = new ResultWriter("result.txt");

		final String EXIT_TAG = "42";
		Scanner scanner = new Scanner(System.in);
		String userInput;

		//load the signatures the program is expected to know
		try {
			loader.load("signatures.txt");
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

			String type = analyzer.detect(loader.getSignatures(), userInput);
			if (!type.equals("UNDEFINED")) {
				writer.update(type);
				System.out.println("PROCESSED");
			} else {
				System.out.println("UNDEFINED");
			}
		}
	}
}