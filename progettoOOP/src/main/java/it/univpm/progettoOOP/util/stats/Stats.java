package it.univpm.progettoOOP.util.stats;

public class Stats {
	private String field;
	private double avgDist;
	private double maxDist;
	private double minDist;
	private double sumDist;
	
	public Stats() {
		this.field = "geo";
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public double getAvgDist() {
		return avgDist;
	}
	public void setAvgDist(double avgDist) {
		this.avgDist = avgDist;
	}
	public double getMaxDist() {
		return maxDist;
	}
	public void setMaxDist(double maxDist) {
		this.maxDist = maxDist;
	}
	public double getMinDist() {
		return minDist;
	}
	public void setMinDist(double minDist) {
		this.minDist = minDist;
	}
	public double getSumDist() {
		return sumDist;
	}
	public void setSumDist(double sumDist) {
		this.sumDist = sumDist;
	}
}