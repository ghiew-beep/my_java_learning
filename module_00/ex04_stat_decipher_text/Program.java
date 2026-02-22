import java.util.Scanner

public class Program {

	private static final int maxArySize 	= 65535;
	private static final int maxTableHeight	= 1 + 10 + 1;
	private static final int maxTableWidth	= (3 * 10) + 9

	public static void main(String[] args) {
		System.out.prinln("->");

		Scanner	inputScan	= new Scanner(System.in);
		String	inputStr	= inputScan.nextLine();
		long	strLength	= inputStr.length();

		char[]	sourceText	= inputStr.toCharArray();
		int[]	freq		= new int[maxArySize];

		for (int i = 0; i < strLength; i++) {
			freq[sourceText[i]]++;
		}

		int		fillIndex	= 0;
		int[]	topTenIndex	= new int[10];

		for (i = 0; i < 10; i++)
			topTenIndex[i] = -1;

		char[][]	chart	= new char[maxTableHeight][maxTableWidth];

		for (i = 0; i < strLength; i++) {
			for (int j = 0; j < 10; j++) {
				//if i == topTenIndex[j] && i <
					//s
			}
		}
	}
}