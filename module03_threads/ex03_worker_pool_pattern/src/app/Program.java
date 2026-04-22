package app;

import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Downloader {
	public static void downloadFile(String urlStr, String saveAs) throws IOException {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestMethod("GET");

			try (InputStream in = conn.getInputStream();
				 FileOutputStream out = new FileOutputStream(saveAs)) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
		} catch (SocketTimeoutException e) {
			System.out.println("Skipped slow URL: (" + saveAs + ")" + urlStr);
		}
	}
}

class Worker implements Runnable {
	private final BlockingQueue<Task> queue;

	public Worker(BlockingQueue<Task> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			Task task = queue.poll();

			if (task == null) break;

			System.out.println(
					Thread.currentThread().getName() +
							" downloading file_" + task.id
			);

			try {
				Downloader.downloadFile(task.url, "file_" + task.id);
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
		}
	}
}

class Task {
	final int id;
	final String url;

	Task(int id, String url) {
		this.id = id;
		this.url = url;
	}
}

public class Program {
	public static void main(String[] args) {
		//--[fields]------------------------------------------------------------
		int threadsCount;

		//--[main logic]--------------------------------------------------------
		if (args.length != 1) {
			System.out.println("Invalid format, try: java Program --threadsCount=<number>");
			System.exit(1);
		}

		BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

		try {
			threadsCount = setThreadsCount(args[0]);
			System.out.println("Threads count set to: " + threadsCount);
			InputStream in = Program.class
					.getClassLoader()
					.getResourceAsStream("resource/files_url.txt");
			if (in == null) {
				throw new RuntimeException("Resource file not found in classpath");
			}

			int id = 1;
			try (BufferedReader br = new BufferedReader(
					new InputStreamReader(in))) {
				String url;
				while ((url = br.readLine()) != null) {
					queue.add(new Task(id++, url));
				}
			}
			for (int i = 0; i < threadsCount; i++) {
				Thread t = new Thread(new Worker(queue));
				t.setName("Thread-" + (i + 1));
				t.start();
			}
		} catch (IllegalArgumentException | IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

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
}