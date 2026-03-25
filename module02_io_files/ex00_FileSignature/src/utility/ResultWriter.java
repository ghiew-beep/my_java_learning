package utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResultWriter implements AutoCloseable {
	//--[fields]----------------------------------------------------------------
	private OutputStream out = null;

	//--[constructors]----------------------------------------------------------
	private ResultWriter() {}

	public ResultWriter(String outputFileName) throws IOException {
		if (outputFileName.isEmpty()) {
			throw new IllegalArgumentException("Output filename cannot be empty");
		}
		out = new FileOutputStream(outputFileName, false);
	}

	//--[methods]---------------------------------------------------------------
	public void update(String content) throws IOException {
		if (out == null) {
			throw new IllegalStateException("ResultWriter is already closed");
		}
		out.write((content + "\n").getBytes());
	}

	public void close() throws IOException {
		out.close();
		out = null;
	}
}