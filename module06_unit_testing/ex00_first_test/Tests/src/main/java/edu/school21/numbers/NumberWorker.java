package edu.school21.numbers;
/**
 * Hello world!
 */
public class NumberWorker {

	public NumberWorker() {}

	public boolean isPrime(int number) {
		if (number <= 1) {
			throw new IllegalNumberException("Illegal, none-prime: " + number);
		}

		//chk for 2 & 3
		if (number <= 3) {
			return true;
		}

		//chk 6k, 6k+2, 6k+3, 6k+4
		if (number % 2 == 0 || number % 3 == 0) {
			return false;
		}

		int		i = 5;

		while (i * i <= number) {
			if (number % i == 0 || number % (i + 2) == 0) {
				return false;
			}
			i += 6;
		}
		return true;
	}

	public int digitSum(int number) {
		int sum = 0;
		number = Math.abs(number); // handle negative numbers
		while (number > 0) {
			sum += number % 10;
			number /= 10;
		}
		return sum;
	}
}
