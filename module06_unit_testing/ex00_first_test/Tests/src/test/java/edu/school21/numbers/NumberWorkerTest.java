package edu.school21.numbers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class NumberWorkerTest {

    /**
     * Rigorous Test :-)
     */
	@ParameterizedTest
	@ValueSource(ints = {2, 3, 5, 7, 11, 67})
	void isPrimeForPrimes(int number) {
		NumberWorker nw = new NumberWorker();
		assertTrue(nw.isPrime(number));
	}

	@ParameterizedTest
	@ValueSource(ints = {4, 6, 8, 14, 200, 21, 56, 35, 77})
	void isPrimeForNotPrimes(int number) {
		NumberWorker nw = new NumberWorker();
		assertFalse(nw.isPrime(number));
	}

	@ParameterizedTest
	@ValueSource(ints = {-2, -1, 0, 1})
	void isPrimeForIncorrect(int number) {
		NumberWorker nw = new NumberWorker();
		assertThrows(IllegalNumberException.class, () -> nw.isPrime(number));
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/test.csv")
	void testSumDigits(int input, int expected) {
		NumberWorker nw = new NumberWorker();
		assertEquals(expected, nw.digitSum(input));
	}
}
