package service;

import model.WordFrequency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextSimilarityAnalyzer {

	//--[fields]----------------------------------------------------------------
	private final Map<String, WordFrequency> frequencyMap = new HashMap<>();
	private double similarity = 0.0;

	//--[constructors]----------------------------------------------------------
	/**
	 * Constructs analyzer and immediately builds frequency map and computes similarity.
	 *
	 * @param file1 path to first txt file
	 * @param file2 path to second txt file
	 * @throws IOException if either file cannot be read
	 */
	public TextSimilarityAnalyzer(String file1, String file2)
			throws IOException {
		buildMap(file1, 1);
		buildMap(file2, 2);
		similarity = computeCosineSimilarity();
	}

	//--[methods]---------------------------------------------------------------
	/**
	 * Reads a file and populates frequency map for the given source (1 or 2).
	 */
	private void buildMap(String filePath, int source) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			boolean hasContent = false;

			while ((line = reader.readLine()) != null) {
				if (!line.trim().isEmpty()) hasContent = true;

				String[] tokens = line.split(" ");
				for (String token : tokens) {
					String word = sanitize(token);
					if (word.isEmpty()) continue;
					if (!containsAlphabet(word)) continue;

					frequencyMap.putIfAbsent(word, new WordFrequency(word));
					if (source == 1) {
						frequencyMap.get(word).incrementFreq1();
					} else {
						frequencyMap.get(word).incrementFreq2();
					}
				}
			}
			if (!hasContent) {
				throw new IllegalStateException("file is empty: " + filePath);
			}
		}
	}

	/**
	 * Strips leading/trailing punctuation and quotes, lowercases the token.
	 */
	private String sanitize(String token) {
		return token.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "").toLowerCase();
	}

	/**
	 * Returns true if the word contains at least one alphabetic character.
	 */
	private boolean containsAlphabet(String word) {
		for (char c : word.toCharArray()) {
			if (Character.isLetter(c)) return true;
		}
		return false;
	}

	/**
	 * Computes cosine similarity between the two frequency vectors.
	 * similarity = (A · B) / (||A|| * ||B||)
	 */
	private double computeCosineSimilarity() {
		double dotProduct = 0.0;
		double magnitudeA = 0.0;
		double magnitudeB = 0.0;

		for (WordFrequency wf : frequencyMap.values()) {
			dotProduct  += (double) wf.getFreq1() * wf.getFreq2();
			magnitudeA  += (double) wf.getFreq1() * wf.getFreq1();
			magnitudeB  += (double) wf.getFreq2() * wf.getFreq2();
		}

		double denominator = Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB);
		if (denominator == 0) return 0.0;

		return dotProduct / denominator;
	}

	/**
	 * Returns the computed cosine similarity score.
	 */
	public double getSimilarity() {
		return similarity;
	}
}
