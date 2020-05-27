package it.univpm.progettoOOP.util.filter;



import it.univpm.progettoOOP.model.Tweet;

public class FilterDistanceGreater extends FilterDistance implements Filter{

	public FilterDistanceGreater(Double distance) {
		super(distance);
		
	}
	
	

	@Override
	public boolean filter(Tweet tweet) {
		if(tweet.getDistance()>distance)
			return true;
		else
			return false;
	}
	
}