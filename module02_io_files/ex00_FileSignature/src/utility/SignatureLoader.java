package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignatureLoader {
	//--[fields]----------------------------------------------------------------

	//--[constructors]----------------------------------------------------------
	public SignatureLoader() {}

	//--[methods]---------------------------------------------------------------
	public void loadSignature(String signatureReferenceFileName)
			throws IOException {
		//if file is invalid/not found,
		// try() will call close() to prevent resource leak
		try (InputStream in = new FileInputStream(signatureReferenceFileName)) {

		}
	}
}