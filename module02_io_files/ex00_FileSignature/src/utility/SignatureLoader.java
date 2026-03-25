package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SignatureLoader {
	//--[fields]----------------------------------------------------------------
	Map<String, byte[]> signatures = new LinkedHashMap<>();
	Map<String, byte[]> descendingSignatures = null;

	//--[constructors]----------------------------------------------------------
	public SignatureLoader() {}

	//--[methods]---------------------------------------------------------------
	/**
	 * Scan the file specified and build a LinkedHashMap sorted descendingly
	 *
	 * @param signatureReferenceFileName txt file containing "FILE_FORMAT, XX XX XX"
	 * @throws IOException if the file cannot be read
	 * @throws IllegalStateException if the file contains no valid signatures
	 * @throws IllegalArgumentException if the specified file is not a txt file
	 * or content is not following format "FILE_FORMAT, XX XX XX XX"
	 * or invalid hex figure detected
	 */
	public void load(String signatureReferenceFileName)
			throws IOException {
		if (!signatureReferenceFileName.endsWith(".txt")) {
			throw new IllegalArgumentException(
					"expected a .txt file, got: " + signatureReferenceFileName);
		}
		//if file is invalid/not found,
		// try() will call close() to prevent resource leak
		try (InputStream in = new FileInputStream(signatureReferenceFileName)) {
			Scanner scanner = new Scanner(in);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) continue;

				try {
					String[] parts = line.split(", ", 2);
					if (parts.length < 2 || parts[1].trim().isEmpty()) {
						throw new IllegalArgumentException(
								"invalid format on line: " + line);
					}

					String typeName = parts[0];
					String[] hexTokens = parts[1].split(" ");

					byte[] magicBytes = parseHexTokens(hexTokens, line);

					signatures.put(typeName, magicBytes);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"invalid hex value on line: " + line, e);
				}
			}
			if (signatures.isEmpty()) {
				throw new IllegalStateException("File contains no valid signature");
			}
		}
		//sort the mapping descending fashion
		descendingSignatures = sortDescending(signatures);
	}

	private byte[] parseHexTokens(String[] hexTokens, String line) {
		byte[] magicBytes = new byte[hexTokens.length];
		try {
			for (int i = 0; i < hexTokens.length; i++) {
				magicBytes[i] = (byte) Integer.parseInt(hexTokens[i].trim(), 16);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"invalid hex value on line: " + line, e);
		}
		return magicBytes;
	}

	private Map<String, byte[]> sortDescending(Map<String, byte[]> map) {
		List<Map.Entry<String, byte[]>> entries = new ArrayList<>(map.entrySet());
		entries.sort((a, b) ->
				b.getValue().length - a.getValue().length);

		Map<String, byte[]> sorted = new LinkedHashMap<>();
		for (Map.Entry<String, byte[]> entry : entries) {
			sorted.put(entry.getKey(), entry.getValue());
		}
		return sorted;
	}

	/**
	 * Returns the loaded signatures sorted by length descending.
	 *
	 * @return map of file type names to their magic bytes
	 * @throws IllegalStateException if {@link #load(String)} has not been called yet
	 */
	public Map<String, byte[]> getSignature() {//what if user call this method without 1st load()
		if (descendingSignatures == null) {
			throw new IllegalStateException(
					"SignatureLoader.getSignature is called without first calling " +
							"SignatureLoader.load(String signatureReferenceFile) first");
		}
		return descendingSignatures;
	}
}