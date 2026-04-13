package app;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

class Downloader {
	public static void downloadFile(String urlStr, String saveAs) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		try (InputStream in = conn.getInputStream();
			 FileOutputStream out = new FileOutputStream(saveAs)) {
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
	}
}

class MyThread extends Thread {
	//--[fields]----------------------------------------------------------------
	int	currentIndex;

	//--[custom constructor]----------------------------------------------------
	MyThread() {

	}
	//--[runnable]--------------------------------------------------------------
	public void run() {

	}
}

class DataObject {
	String[] 	url;
	int			size;
	int			currentIndex;
}

public class Program {
	public static void main(String[] args) {
		//--[fields]------------------------------------------------------------
		int threadsCount = 0;
		String urlFilePath = "/resource/files_url.txt";

		//--[input validation and init thread count]----------------------------
		if (args.length != 1) {
			System.out.println("Invalid format, try: java Program --threadsCount=<number>");
			System.exit(1);
		}

		try {
			threadsCount = setThreadsCount(args[0]);
			System.out.println("Threads count set to: " + threadsCount);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		//

	}

	// Make it static so main can call it
	public static int setThreadsCount(String arg) {
		final String prefix = "--threadsCount=";

		if (!arg.startsWith(prefix)) {
			throw new IllegalArgumentException("Argument must start with " + prefix);
		}

		String valueStr = arg.substring(prefix.length());

		int count;
		try {
			count = Integer.parseInt(valueStr);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Threads count must be a valid integer");
		}

		if (count <= 0) {
			throw new IllegalArgumentException("Threads count must be a positive number");
		}

		return count;
	}

	public static DataObject loadURL (String path) {
		DataObject data = new DataObject(path);
		return data;
	}
}