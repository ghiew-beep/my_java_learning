import java.util.Scanner;

public class Program {

	private static final String	prefix 			= "W";
	private static final int	maxWeekCount 	= 18;
	private static final int	maxTestPerWeek	= 5;
	private static final int	maxGrade 		= 9;
	private static final int	minGrade 		= 1;

	public static void main(String[] args) {

		//user instruction
		System.out.println("Example:");
		System.out.println("-> W1");
		System.out.println("-> 5 5 7 8 9\n");

		boolean	inputted	= false;
		int		curWeek 	= 1;
		String	tag 		= prefix + curWeek;
		Scanner userInput 	= new Scanner(System.in);
		String	line;

		long	grade 		= 0;
		long	tempGrade 	= 9;
		long	concatGrade = 0;
		int		i 			= 0;
		int		pwr 		= 0;

		while (true) {
			System.out.print("-> ");
			line = userInput.nextLine();

			//stop at '42'
			if (line.equals("42")) {
				curWeek--;
				break ;
			}

			//else expect 'Week X'
			if (!tag.equals(line)) {
				System.out.println("   IllegalArgument");
				userInput.close();
				System.exit(-1);
			}

			curWeek++;
			if (curWeek > maxWeekCount) {
				curWeek--;
				break ;
			}
			tag = prefix + curWeek;

			//expect 'X X X X X' where 1 <= x <= 9
			System.out.print("-> ");

			for (i = 0; i < maxTestPerWeek; i++) {

				if (!userInput.hasNextInt()) {
					System.out.println("   IllegalArgument");
					userInput.close();
					System.exit(-1);
				}

				grade = userInput.nextInt();

				if (grade < minGrade || grade > maxGrade) {
					System.out.println("   IllegalArgument");
					userInput.close();
					System.exit(-1);
				}

				if (grade < tempGrade)
					tempGrade = grade;

				inputted = true;
			}

			line = userInput.nextLine();
			concatGrade = concatGrade + (long)(grade * Math.pow(10, pwr++));
			System.out.println(concatGrade);
		}
		userInput.close();
		// draw logic
		i = 0;
		while (inputted && i < curWeek) {
			System.out.print(prefix + (i + 1) + " ");
			grade = concatGrade % 10;
			concatGrade /= 10;
			while (grade > 0) {
				System.out.print("=");
				grade--;
			}
			System.out.println(">");
			i++;
		}
	}
}