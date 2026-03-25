package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SignatureLoader {
	//--[fields]----------------------------------------------------------------
	boolean signatureFound = false;
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
	 */
	public void load(String signatureReferenceFileName)
			throws IOException {
		//if file is invalid/not found,
		// try() will call close() to prevent resource leak
		try (InputStream in = new FileInputStream(signatureReferenceFileName)) {
			Scanner scanner = new Scanner(in);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.isEmpty()) {
					continue;
				}

				String[] parts = line.split(", ", 2);
				String typeName = parts[0];
				String[] hexTokens = parts[1].split(" ");

				byte[] magicBytes = new byte[hexTokens.length];
				for (int i = 0; i < hexTokens.length; i++) {
					magicBytes[i] = (byte) Integer.parseInt(hexTokens[i], 16);
				}
				signatures.put(typeName, magicBytes);

				if (!signatureFound) {
					signatureFound = true;
				}
			}
			if (!signatureFound) {
				throw new IllegalStateException("File contains no valid signature");
			}
		}

		//sort the mapping
		List<Map.Entry<String, byte[]>> entries = new ArrayList<>(signatures.entrySet());
		entries.sort((a, b) -> b.getValue().length - a.getValue().length);

		descendingSignatures = new LinkedHashMap<>();
		for (Map.Entry<String, byte[]> entry : entries) {
			descendingSignatures.put(entry.getKey(), entry.getValue());
		}
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