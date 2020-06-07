package it.univpm.progettoOOP.util.stats;

public class StatsText {
	private final String field = "text";
	private double avgLength;
	private double maxLength;
	private double minLength;
	private double sumLength;
	private int numTweetAnalyzed;
	
	public StatsText() {
		this.avgLength = 0;
		this.maxLength = 0;
		this.minLength = 0;
		this.sumLength = 0;
	}
	
	public String getField() {
		return field;
	}

	public double getAvgLength() {
		return avgLength;
	}
	
	public void setAvgLength(double avgLength) {
		this.avgLength = avgLength;
	}
	
	public double getMaxLength() {
		return maxLength;
	}
	
	public void setMaxLength(double maxLength) {
		this.maxLength = maxLength;
	}
	
	public double getMinLength() {
		return minLength;
	}
	
	public void setMinLength(double minLength) {
		this.minLength = minLength;
	}
	
	public double getSumLength() {
		return sumLength;
	}
	
	public void setSumLength(double sumLength) {
		this.sumLength = sumLength;
	}

	public int getNumTweetAnalyzed() {
		return numTweetAnalyzed;
	}

	public void setNumTweetAnalyzed(int numTweetAnalyzed) {
		this.numTweetAnalyzed = numTweetAnalyzed;
	}
}
