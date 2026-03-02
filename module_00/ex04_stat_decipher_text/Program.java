import java.util.Scanner

public class Program {

	private static final int	designatedOutput	= 10;
	private static final int	maxOccurance		= 999;
	private static final int	maxFigureWidth		= 3;
	private static final int	minColumnHeight		= 0;
	private static final int	maxColumnHeight		= 10;
	private static final int	columnSeperation	= designatedOutput - 1;
	private static final int	maxUniqueChar		= 65535;
	private static final int	maxTableHeight		= 1 + maxColumnHeight + 1;
	private static final int	maxTableWidth		= (maxFigure * maxData) + columnSeperation;

	private static int[] sortArray(int[] input) {
		int temp;

		for (int i = 0; i < input.length - 1; i++) {
			if (input[i] == 0)
				break ;
			if (input[i] < input[i + 1]) {
				temp = input[i];
				input[i] = input[i + 1];
				input[i + 1] = temp;
			}
		}
		return (input);
	}

	private static int[] extractSelection(int[] input) {
		int[]	result = new int[designatedOutput];

		//copy in the 1st ten index
		for (int i = 0; i < designatedOutput; i++)
			result[i] = i;

		//sort based on content pointed by the index
		int	temp = 0;
		for (int j = 0; j < designatedOutput - 1; j++) {
			for (int k = 0; k < designatedOutput - 1 - j; k++) {
				if (input[result[k]] < input[result[k + 1]]) {
					temp = result[k];
					result[k] = result[k + 1];
					result[k + 1] = temp;
				}
			}
		}

		//update the result[] with the rest of entries in input[]
		for (int l = designatedOutput; l < input.length; l++) {
			if (input[result[designatedOutput - 1]] >= input[l])
				continue ;
			for (int m = designatedOutput - 2; m >= 0; m--) {
				if (input[l] <= input[result[m]]) {
					//overwrite the lower elements
					for (int n = m; m < designatedOutput; m++)
					result[m + 1] = l;
					break;
				}
			}
		}
			//check if input[j] is smaller than the last entry in result[]
				//if yes, ignore
				//if equal or greater than any entry in result[], update result[]
		}
	}

	public static void main(String[] args) {
		System.out.prinln("->");

		Scanner	inputScan	= new Scanner(System.in);
		String	inputStr	= inputScan.nextLine();

		long	strLength	= inputStr.length();
		char[]	sourceText	= inputStr.toCharArray();
		int[]	freq		= new int[maxUniqueChar];

		for (int i = 0; i < strLength; i++) {
			freq[sourceText[i]]++;//implicit widening, cha to int
		}

		//identify the top 10
		int[]	topTenIdx = extractSelection(freq);

		//fill in the chart
		//display the chart

	}
}