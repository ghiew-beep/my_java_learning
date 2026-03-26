package app;

import service.TextSimilarityAnalyzer;

import java.io.File;
import java.io.IOException;

public class Program {

	public static void main(String[] args) throws IOException {
		// validate arg count
		if (args.length != 2) {
			throw new IllegalArgumentException(
					"expected 2 arguments, got " + args.length + ". Usage: java Program <file1.txt> <file2.txt>");
		}

		// validate both files
		validateFile(args[0]);
		validateFile(args[1]);

		// analyze
		TextSimilarityAnalyzer analyzer = new TextSimilarityAnalyzer(args[0], args[1]);

		// output
		System.out.printf("Similarity = %.2f%n", analyzer.getSimilarity());
	}

	private static void validateFile(String path) {
		if (!path.endsWith(".txt")) {
			throw new IllegalArgumentException("expected a .txt file, got: " + path);
		}
		File file = new File(path);
		if (!file.exists()) {
			throw new IllegalArgumentException("file not found: " + path);
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException("not a file: " + path);
		}
	}
}
