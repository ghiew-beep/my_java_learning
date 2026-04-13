package app;

import java.util.Random;

class SubThread extends Thread {
	//--[fields]----------------------------------------------------------------
	int[] array;
	int startIndex;
	int size;
	int sum;
	int index;
	int[] results;

	//--[custom constructor]----------------------------------------------------
	SubThread(int[] array, int startIndex, int size, int index, int[] results) {
		this.array = array;
		this.startIndex = startIndex;
		this.size = size;
		this.index = index;
		this.results = results;
		this.sum = 0;
	}

	//--[runnable]--------------------------------------------------------------
	public void run() {
		sum = 0;
		int end = Math.min(array.length, startIndex + size);
		for (int i = startIndex; i < end; i++) {
			sum += array[i];
		}
		// store the sum in the results array
		results[index] = sum;
		System.out.println("Thread " + (index + 1) + ": from " + startIndex + " to " + (end - 1) + " sum is " + sum);
	}
}

public class Program {
	public static void main(String[] args) {
		//--[fields]------------------------------------------------------------
		final int maxArraySize = 2000000;
		final int moduloValue = 1000;

		int arraySize;
		int threadCount;

		//--[validation]--------------------------------------------------------
		if (args.length != 2 || !args[0].startsWith("--arraySize=")
				|| !args[1].startsWith("--threadsCount=")) {
			System.out.println(
					"Invalid try: java -cp src app.Program " +
							"--arraySize=INTEGER --threadsCount=INTEGER");
			System.exit(1);
		}
		try {
			arraySize = Integer.parseInt(args[0].substring("--arraySize=".length()));
			threadCount = Integer.parseInt(args[1].substring("--threadsCount=".length()));

			// Validate arraySize
			if (arraySize <= 0 || arraySize > maxArraySize) {
				System.out.println("Error: arraySize must be between 1 and " + maxArraySize);
				System.exit(1);
			}

			// Validate threadCount
			if (threadCount <= 0 || threadCount > arraySize) {
				System.out.println("Error: threadsCount must be between 1 and " + arraySize);
				System.exit(1);
			}

		} catch (NumberFormatException e) {
			System.out.println("Error: arraySize and threadsCount must be integers");
			arraySize = -1;
			threadCount = -1;
			System.exit(1);
		}

		//--[generate array with random size and values]------------------------
		int[] array = new int[arraySize];
		Random rand = new Random();
		for (int i = 0; i < arraySize; i++) {
			array[i] = rand.nextInt(moduloValue);
		}

		//--[compute single-thread sum for verification]------------------------
		int singleThreadSum = 0;
		for (int v : array) singleThreadSum += v;
		System.out.println("Sum: " + singleThreadSum);

		//--[threads creation]--------------------------------------------------
		int[] results = new int[threadCount];
		SubThread[] threads = new SubThread[threadCount];
		int chunkSize = arraySize / threadCount;
		int remainder = arraySize % threadCount;

		int startIndex = 0;
		for (int i = 0; i < threadCount; i++) {
			int size = chunkSize + (i < remainder ? 1 : 0); // distribute remainder
			threads[i] = new SubThread(array, startIndex, size, i, results);
			startIndex += size;
		}

		//--[threads start]-----------------------------------------------------
		for (SubThread t : threads) t.start();

		//--[threads end]-------------------------------------------------------
		try {
			for (SubThread t : threads) t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//--[display sum]-------------------------------------------------------
		int totalSum = 0;
		for (int s : results) totalSum += s;
		System.out.println("Sum by threads: " + totalSum);
	}
}