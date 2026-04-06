package app;

class MyThread extends Thread {
	private final String tag;

	MyThread(String tag) {
		this.tag = tag;
	}

	public void run() {
		for (int i = 0; i < 50; i++) {
			System.out.println(Thread.currentThread().getName() + ": " + tag);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}

public class Program {
	public static void main(String[] args) {
		int count = 0;

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
			System.out.println("Invalid format, try: Program --count=POSITIVE_INTEGER");
			System.exit(1);
		}

		//create thread the normal way
		// -write a class extends Thread
		// -custom constructor
		// -and define runnable under run()
		Thread hen = new MyThread("Hen");

		//create thread using lambda expression for runnable
		Thread egg = new Thread(() -> {
			for (int i = 0; i < 50; i++) {
				System.out.println(Thread.currentThread().getName() + ": " + "Egg");
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					return;
				}
			}
		});
		
		hen.start();
		egg.start();

		try {
			hen.join();
			egg.join();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		for (int i = 0; i < count; i++) {
			System.out.println("Human");
		}
	}
}