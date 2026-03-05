import java.util.Scanner;

public class Program {
	public static void isPrime(int n) {
		int	step = 0;

		//chk for 2 & 3
		if (n <= 3) {
			step++;
			System.out.println("   true " + step);
			return ;
		}

		step++;
		//chk 6k, 6k+2, 6k+3, 6k+4
		if (n % 2 == 0 || n % 3 == 0) {
			System.out.println("   false " + step);
			return ;
		}

		int		i = 5;

		while (i * i <= n) {
			step++;
			if (n % i == 0 || n % (i + 2) == 0) {
				System.out.println("   false " + step);
				return ;
			}
			i += 6;
		}
		System.out.println("   true " + step);
	}

	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		System.out.print("-> ");

		if (!userInput.hasNextInt()) {
			System.err.println("   IllegalArgument");
			userInput.close();
			System.exit(-1);
		}

		int	num = userInput.nextInt();

		if (num < 2) {
			System.err.println("   IllegalArgument");
			userInput.close();
			System.exit(-1);
		}

		userInput.close();

		isPrime(num);
	}
}