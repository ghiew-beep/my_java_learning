package model;

public class WordFrequency {
	//--[fields]----------------------------------------------------------------
	private int freq1;
	private int freq2;

	//--[constructors]----------------------------------------------------------
	public WordFrequency(String word) {
		this.freq1 = 0;
		this.freq2 = 0;
	}

	//--[methods]---------------------------------------------------------------
	public void incrementFreq1() { this.freq1++; }
	public void incrementFreq2() { this.freq2++; }

	public int getFreq1()       { return freq1; }
	public int getFreq2()       { return freq2; }
}
