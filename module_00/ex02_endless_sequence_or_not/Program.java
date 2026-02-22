import java.util.Scanner;

public class Program {
	public static boolean isPrime(int n) {
		//chk for 2 & 3
		if (n <= 3)
			return (true);

		//chk 6k, 6k+2, 6k+3, 6k+4
		if (n % 2 == 0 || n % 3 == 0)
			return (false);

		int		i = 5;

		while (i * i <= n) {
			if (n % i == 0 || n % (i + 2) == 0)
				return (false);
			i += 6;
		}
		return (true);
	}

	public static boolean isSumPrime(int n) {
		int	sum = 0;

		while (n > 0) {
			sum += n % 10;
			n /= 10;
		}

		return (isPrime(sum));
	}

	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		int count = 0;
		int	num = 0;

		while (true) {
			System.out.print("-> ");
			if (!userInput.hasNextInt()) {
				System.err.println("   IllegalArgument");
				userInput.close();
				System.exit(-1);
			}

			num = userInput.nextInt();

			if (num < 2) {
				System.err.println("   IllegalArgument");
				userInput.close();
				System.exit(-1);
			}

			if (num == 42) {
				userInput.close();
				break ;
			}

			if (isSumPrime(num))
				count++;
		}

		System.out.println("Count of coffee-request - " + count);
	}
}