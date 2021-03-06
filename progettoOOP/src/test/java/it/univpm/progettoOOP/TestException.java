package it.univpm.progettoOOP;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import it.univpm.progettoOOP.exceptions.CityNotFoundException;
import it.univpm.progettoOOP.exceptions.EmptyCollectionException;
import it.univpm.progettoOOP.exceptions.FilterNotFoundException;
import it.univpm.progettoOOP.exceptions.GenericFilterException;
import it.univpm.progettoOOP.exceptions.IllegalIdException;
import it.univpm.progettoOOP.exceptions.IllegalIntervalException;
import it.univpm.progettoOOP.exceptions.IllegalWordException;
import it.univpm.progettoOOP.exceptions.NegativeValueException;
import it.univpm.progettoOOP.model.Tweet;
import it.univpm.progettoOOP.model.TweetFilter;
import it.univpm.progettoOOP.service.TweetService;
import it.univpm.progettoOOP.service.TweetServiceImpl;
import it.univpm.progettoOOP.util.filter.FilterIdTextImpl;
import it.univpm.progettoOOP.util.filter.FilterUtils;
import it.univpm.progettoOOP.util.stats.TweetStatsGeo;
import it.univpm.progettoOOP.util.stats.TweetStatsGeoImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Classe che esegue i test sulle eccezioni personalizzate
 * @author Roberto Bramucci
 * @author Stefano Bonci
*/

class TestException {
	@BeforeEach
	void setUp() throws Exception {		
	}

	@AfterEach
	void tearDown() throws Exception {
	}
		
	/**
	* Test verifica se viene lanciata l'eccezione CityNotFoundException quando viene passata
	* una città diversa da quelle disponibili per eseguire filtri o ottenere statistiche
	*/
	@Test
	public void cityNotFoundExceptionSucceeds() {
		Exception exception = assertThrows(CityNotFoundException.class, () -> { 
			Point2D.Double point = new Point2D.Double(12.135, 44.124);
			Tweet t = new Tweet();
			t.setGeo(point);
			t.chooseCity("TO");});
			
		String expectedMessage = "Questa città non è disponibile";
		String actualMessage = exception.getMessage();
		 
		assertTrue(actualMessage.contains(expectedMessage));
	}
		
	/**	
	 * Test che verifica se viene lanciata l'eccezione IllegalIdException se viene passato un id non presente tra quelli disponibili
	 * al metodo getTweetFromId 
	 */
	@Test
	public void illegalIdExceptionSucceeds() {
		Exception exception = assertThrows(IllegalIdException.class, () -> { 
			Collection<Tweet> sample = new ArrayList<Tweet>();
			FilterIdTextImpl fiti = new FilterIdTextImpl();
			fiti.getTweetFromId(sample, "126499423518025319");});
		
		String expectedMessage = "Questo id non esiste";	
		String actualMessage = exception.getMessage();
		 
		assertTrue(actualMessage.contains(expectedMessage));
	}
			
	/**
	 * Test che verifica se viene lanciata l'eccezione IllegalIntervalException se vengono passati valori errati al filtro "$bt"
	 */
	@Test
	public void illegalIntervalExceptionSucceeds() {
		Exception exception = assertThrows(IllegalIntervalException.class, () -> { 
			Point2D.Double p = new Point2D.Double(7.6777,45.0702);
			Tweet value = new Tweet("1264994235180253190","TRICOLOR!!!\n\nThe Italian Frecce Tricolore (the acrobatic Italian Air Force Squad) flew over Turin today.\n\nThey started from #Codogno (the town which the symbol of the fight against #covid_19) and will fly over many… https://t.co/i99LXh2k8K",p);
			FilterUtils.check(value, "$bt", "AN", 10.0, 5.0);});
		String expectedMessage = "Il primo valore deve essere maggiore del secondo";
		String actualMessage = exception.getMessage();
		 
		assertTrue(actualMessage.contains(expectedMessage));
	}
		
	/**
	 * Test che verifica se viene lanciata l'eccezione NegativeValueException se viene passato un valore negativo per 
	 * la distanza nella quale eseguire il filtro
	 */
		
	@Test
	public void negativeValueExceptionSucceeds() {
		Exception exception = assertThrows(NegativeValueException.class, () -> { 
			Point2D.Double p = new Point2D.Double(7.6777,45.0702);
			Tweet value = new Tweet("1264994235180253190","TRICOLOR!!!\n\nThe Italian Frecce Tricolore (the acrobatic Italian Air Force Squad) flew over Turin today.\n\nThey started from #Codogno (the town which the symbol of the fight against #covid_19) and will fly over many… https://t.co/i99LXh2k8K",p);
			Double th = -10.0;
			FilterUtils.check(value, "$lt", "AN", th);});
		String expectedMessage = "Il valore inserito è negativo";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
		
	/**
	 * Test che verifica se viene lanciata l'eccezione GenericFilterException se vengono passati valori errati per eseguire un filtro
	 */
	@Test
	public void genericFilterExceptionSucceeds() {
		Exception exception = assertThrows(GenericFilterException.class, () -> { 
			Tweet value = null;
			Double th = null ;
			FilterUtils.check(value, "$lt", "AN", th);});
		String expectedMessage = "Errore nella scelta del filtro";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
		
	/**
	 * Test che verifica se viene lanciata l'eccezione IllegalWordException quando viene passata una parola troppo corta
	 *  al metodo getTweetFromText
	 */
	@Test
	public void illegalWordExceptionSucceeds() {
		Exception exception = assertThrows(IllegalWordException.class, () -> { 
			Collection<Tweet> sample = new ArrayList<Tweet>();
			FilterIdTextImpl fiti = new FilterIdTextImpl();
			fiti.getTweetsFromText(sample, "a");});
		String expectedMessage = "Inserire una parola più lunga";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	/**
	 * Test che verifica se viene lanciata l'eccezione EmptyCollectionException quando viene passata una Collection vuota
	 *  per calcolare le statistiche
	 */
	@Test
	public void emptyCollectionExceptionSucceeds() {
		Exception exception = assertThrows(EmptyCollectionException.class, () -> {
			TweetStatsGeo twst = new TweetStatsGeoImpl();
			Collection<Tweet> sample = new ArrayList<>();
			twst.setStatsGeo(sample, "AN");
		});
		String expectedMessage = "Nessun dato analizzabile con questo filtro";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	/**
	 * Test che verifica se viene lanciata l'eccezione FilterNotFoundException quando viene inserito un filtro
	 * non esistente
	 */
	
	@Test
	public void filterNotFoundExceptionSucceeds() {
		Exception exception = assertThrows(FilterNotFoundException.class, () -> {
			TweetService twSer = new TweetServiceImpl();
			TweetFilter fil = new TweetFilter((ArrayList<Tweet>)twSer.getData());
			org.json.JSONObject obj = new org.json.JSONObject("{\"$st\":100}");
			fil.parseFilter(obj, "AN");
		});
		String expectedMessage = "Il filtro inserito non esiste";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
}
