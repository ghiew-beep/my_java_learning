package app;

class SharedState {
	boolean eggTurn = true;
}

class MyThread extends Thread {
	//--[fields]----------------------------------------------------------------
	private final String tag;
	private SharedState state;
	private int count = 0;
	final Object lock;
	boolean	mySignal;

	//--[custom constructor]----------------------------------------------------
	MyThread(String			tag,
			 int			count,
			 Object			lock,
			 SharedState	state,
			 boolean		mySignal) {
		if (tag.isEmpty()) {
			throw new IllegalArgumentException("Tag cannot be empty");
		}
		if (count < 0) {
			throw new IllegalArgumentException("Count cannot be < 0");
		}
		this.tag = tag;
		this.count = count;
		this.lock = lock;
		this.state = state;
		this.mySignal = mySignal;
	}

	//--[methods]---------------------------------------------------------------
	public void run() {
		//repeat count-amount of time as requested
		for (int i = 0; i < count; i++) {
			synchronized (lock) {
				//skip this while loop if it is my turn
				// else suspend flow, release lock, wait to be notified
				while (state.eggTurn != mySignal) {
					try { lock.wait(); } catch (InterruptedException e) {}
				}
				System.out.println(
						Thread.currentThread().getName() + ": " + tag);
				state.eggTurn = !state.eggTurn;
				lock.notifyAll();
			}
		}
	}
}

public class Program {
	public static void main(String[] args) {
		int count = 0;
		SharedState state = new SharedState();

		//--[validate input]----------------------------------------------------
		if (args.length == 1 && args[0].startsWith("--count=")) {
			String value = args[0].substring("--count=".length());
			try {
				count = Integer.parseInt(value);
				if (count <= 0) {
					System.out.println("Must be > 0");
					System.exit(1);
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid integer (format or out of range)");
				System.exit(1);
			}
		} else {
			System.out.println(
					"Invalid format, try: Program --count=POSITIVE_INTEGER");
			System.exit(1);
		}

		//--[thread creation]---------------------------------------------------
		Object lock = new Object();

		Thread egg = new MyThread("Egg", count, lock, state, true);
		Thread hen = new MyThread("Hen", count, lock, state, false);

		//--[start threads]-----------------------------------------------------
		egg.start();
		hen.start();

		//--[threads end]-------------------------------------------------------
		try {
			hen.join();
			egg.join();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}