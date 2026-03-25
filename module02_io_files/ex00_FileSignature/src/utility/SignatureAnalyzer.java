package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SignatureAnalyzer {
	//--[fields]----------------------------------------------------------------
	private int longestByteCount = 0;

	//--[constructors]----------------------------------------------------------
	public SignatureAnalyzer() {}

	//--[methods]---------------------------------------------------------------
	public String detect(Map<String, byte[]> signatures,
							 String targetFileName) throws IOException {
		//determine the longest byte count
		if (longestByteCount == 0) {
			longestByteCount = determineLongestSignatureByte(signatures);
		}
		//try open the file and read the file signature
		byte[] header = new byte[longestByteCount];
		try (InputStream in = new FileInputStream(targetFileName)) {
			in.read(header);
		}

		//try to find a match on signature from signatures map
		for (Map.Entry<String, byte[]> entry : signatures.entrySet()) {
			byte[] sig = entry.getValue();
			boolean match = true;
			for (int i = 0; i < sig.length; i++) {
				if (header[i] != sig[i]) {
					match = false;
					break;
				}
			}
			if (match) return entry.getKey();
		}
		return "UNDEFINED";
	}

	private int determineLongestSignatureByte(Map<String, byte[]> signatures) {
		int maxLength = 0;
		for (byte[] sig : signatures.values()) {
			if (sig.length > maxLength) maxLength = sig.length;
		}
		return maxLength;
	}
}