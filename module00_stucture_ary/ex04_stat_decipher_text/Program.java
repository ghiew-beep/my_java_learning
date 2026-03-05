import java.util.Scanner;

public class Program {

	private static final int	designatedOutput	= 10;
	private static final int	maxOccurrence 		= 999;
	private static final int	maxFigureWidth		= 3;
	private static final int	maxColumnHeight		= 10;
	private static final int	symbolOffset		= 2;
	private static final int	gapPerData			= 4;
	private static final int 	columnSeparation 	= designatedOutput - 1;
	private static final int	maxUniqueChar		= 65535;
	private static final int	maxTableHeight		= 1 + maxColumnHeight + 1;//label + chart + value
	private static final int	maxTableWidth		= (maxFigureWidth * designatedOutput) + columnSeparation;

	private static int[] extractSelection(int[] input) {
		int[] result = new int[designatedOutput];

		//copy in the 1st ten index
		for (int i = 0; i < designatedOutput; i++)
			result[i] = i;

		//sort the 1st ten based on content pointed by the index
		int temp = 0;
		for (int j = 0; j < designatedOutput - 1; j++) {
			for (int k = 0; k < designatedOutput - 1 - j; k++) {
				if (input[result[k]] < input[result[k + 1]]) {
					temp = result[k];
					result[k] = result[k + 1];
					result[k + 1] = temp;
				}
			}
		}

		//update result[] with the rest of entries in input[]
		for (int l = designatedOutput; l < input.length; l++) {
			//compare each of the rest with the lowest member of the top 10
			if (input[result[designatedOutput - 1]] >= input[l])
				continue;
			//encountered legit value to be inserted into top 10
			for (int m = designatedOutput - 2; m >= 0; m--) {
				if (input[l] <= input[result[m]] || (m == 0) && input[l] > input[result[0]]) {
					//shift the lower elements one unit down before insert new value
					for (int n = designatedOutput - 1; n > m; n--) {
						result[n] = result[n - 1];
					}
					//insert the new value
					if (m == 0)
						result[m] = l;
					else
						result[m + 1] = l;
					break;
				}
			}
		}
		return (result);
	}

	private static char[][] createChart(int[] selection, int[] freq, int scale) {
		char[][] chart = new char[maxTableHeight][maxTableWidth];

		for (int i = 0; i < maxTableHeight; i++) {
			for (int j = 0; j < maxTableWidth; j++) {
				chart[i][j] = ' ';
			}
		}

		int drawX = 0;
		int drawY = 0;
		int colWidth = 0;
		int	xOffset = 0;
		long symbolCount = 0;
		String figure;
		for (int i = 0; i < selection.length; i++) {
			colWidth = Integer.toString(freq[selection[i]]).length();
			if (colWidth < maxFigureWidth)
				xOffset = maxFigureWidth - colWidth;

			symbolCount = ((long)freq[selection[i]] * maxColumnHeight) / scale;
			drawY = (maxTableHeight - 1) - 1 - (int)symbolCount;

			//fill in the value;
			figure = Integer.toString(freq[selection[i]]);
			for (int j = 0; j < colWidth; j++) {
				chart[drawY][drawX + xOffset + j] = figure.charAt(j);
			}
			drawY++;

			//fill in the chart symbol
			for (int j = 0; j < symbolCount; j++) {
				chart[drawY + j][drawX + symbolOffset] = '#';
			}

			//fill in the label
			drawY += (int)symbolCount;
			chart[drawY][drawX + symbolOffset] = (char)selection[i];

			drawX += gapPerData;
		}
		return (chart);
	}

	public static void main(String[] args) {
		System.out.println("->");

		Scanner inputScan = new Scanner(System.in);
		String inputStr = inputScan.nextLine();

		long strLength = inputStr.length();
		char[] sourceText = inputStr.toCharArray();
		int[] freq = new int[maxUniqueChar];

		for (int i = 0; i < strLength; i++) {
			freq[sourceText[i]]++;//implicit widening, cha to int
		}

		//identify the top 10, set those > 999 to 999, order already secured
		int[]	finalSelection = extractSelection(freq);
		for (int i = 0; i < designatedOutput; i++) {
			if (freq[finalSelection[i]] > maxOccurrence)
				freq[finalSelection[i]] = maxOccurrence;
		}

		int chartScale = freq[finalSelection[0]];
		char[][] chart = createChart(finalSelection, freq, chartScale);

		for (int i = 0; i < maxTableHeight; i++) {
			for (int j = 0; j < maxTableWidth; j++) {
				System.out.print(chart[i][j]);
			}
			System.out.println();
		}
	}
}