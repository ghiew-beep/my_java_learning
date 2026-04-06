package src;

class MyThread extends Thread {
	private final String tag;

	MyThread(String tag) {
		this.tag = tag;
	}

	public void run() {
		for (int i = 0; i < 50; i++) {
			System.out.println(Thread.currentThread().getName() + ": " + tag);
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

		Thread hen = new MyThread("Egg");
		Thread egg = new MyThread("Hen");
		Thread pip = new Thread(() -> {
			for (int i = 0; i < 50; i++) {
				System.out.println(Thread.currentThread().getName() + ": " + "Pipo");
			}
		});
		
		hen.start();
		egg.start();
		pip.start();

		try {
			hen.join();
			egg.join();
			pip.join();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		for (int i = 0; i < count; i++) {
			System.out.println("Human");
		}
	}
}