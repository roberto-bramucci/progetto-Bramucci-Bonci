package it.univpm.progettoOOP.controller;


import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.univpm.progettoOOP.service.*;
import it.univpm.progettoOOP.util.filter.*;
import it.univpm.progettoOOP.util.stats.*;
import it.univpm.progettoOOP.exceptions.BlankFilterException;
import it.univpm.progettoOOP.exceptions.CityNotFoundException;
import it.univpm.progettoOOP.exceptions.EmptyCollectionException;
import it.univpm.progettoOOP.exceptions.FilterNotFoundException;
import it.univpm.progettoOOP.exceptions.GenericFilterException;
import it.univpm.progettoOOP.exceptions.GenericServiceException;
import it.univpm.progettoOOP.exceptions.IllegalIdException;
import it.univpm.progettoOOP.exceptions.IllegalIntervalException;
import it.univpm.progettoOOP.exceptions.IllegalWordException;
import it.univpm.progettoOOP.exceptions.NegativeValueException;
import it.univpm.progettoOOP.exceptions.WordNotFoundException;
import it.univpm.progettoOOP.model.*;
/**
 * Classe Controller per la gestione delle rotte della REST API 
 * 
 * 
 * @author Roberto Bramucci
 * @author Stefano Bonci
 * 
 * @version 1.0
 * 
 */
@RestController
public class TweetController {
	
	@Autowired
	private TweetService tweetService;
	/**
	 * Rotta per ottenere i metadati relativi al dataset in formato JSON
	 * 
	 * @return Metadati relativi a un dato di tipo {@link Tweet}
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati
	 */
	
	@RequestMapping(value ="/metadata", method = RequestMethod.GET)
	public ResponseEntity<Object> getMeta() throws GenericServiceException{
		return new ResponseEntity<>(tweetService.getMetadata(), HttpStatus.OK);
	}
	/**
	 * Rotta per ottenere i dati di tipo {@link Tweet} relativi al dataset in formato JSON
	 * 
	 * @return Dati di tipo {@link Tweet}
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati
	 */
	
	@RequestMapping(value ="/data", method = RequestMethod.GET)
	public ResponseEntity<Object> getData() throws GenericServiceException{
		return new ResponseEntity<>(tweetService.getData(), HttpStatus.OK);
	}
	
	/**
	 * Rotta per ottenere i dati di tipo {@link Tweet} filtrati in base alla distanza 
	 * da una delle citta' disponibili (Ancona, Milano, Napoli, Roma)
	 * 
	 * @param city Citta' da cui si vuole considerare la distanza
	 * @param filter Filtro di distanza da applicare al dataset
	 * @return Dati di tipo Tweet filtrati in base alla citta' scelta, se presenti
	 * 
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati	
	 * @throws FilterNotFoundException Eccezione lanciata se si inserisce un filtro incompleto o scorretto
	 * @throws CityNotFoundException Eccezione lanciata se si inserisce una citta' non disponibile
	 * @throws NegativeValueException Eccezione lanciata se si inserisce un valore negativo nel filtro di distanza
	 * @throws IllegalIntervalException Eccezione lanciata se si inserisce nel filtro di distanza un intervallo in cui il primo valore e' maggiore del secondo
	 * @throws GenericFilterException Eccezione lanciata se si verifica un errore generico nella scelta del filtro
	 * @throws BlankFilterException Eccezione lanciata se si inserisce un filtro vuoto
	 */
	
	@RequestMapping(value ="/data/{city}", method = RequestMethod.POST)
	public ResponseEntity<Object> getDataWithFilter(@PathVariable ("city") String city, 
			@RequestBody String filter) 
					throws GenericServiceException, FilterNotFoundException, CityNotFoundException, NegativeValueException,
					IllegalIntervalException, GenericFilterException, BlankFilterException{
			try {
			JSONObject obj = new JSONObject(filter);
			FilterUtils<Tweet> utl = new FilterUtils<>();
			TweetFilter tweetsFil = new TweetFilter((ArrayList<Tweet>)tweetService.getData(), utl);
			return new ResponseEntity<>(tweetsFil.parseFilter(obj, city), HttpStatus.OK);
			}
			catch(NoSuchElementException e) {
				throw new BlankFilterException("Nessun filtro inserito");
			}
			catch(JSONException e) {
				throw new FilterNotFoundException("Il filtro inserito è incompleto o scorretto");
			}
		}
	
	/**
	 * Rotta per ottenere un dato di tipo {@link Tweet} in base al suo id
	 * 
	 * @param id id del dato di tipo {@link Tweet} che si vuole ottenere
	 * @return {@link Tweet} corrispondente all'id specificato
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati
	 * @throws IllegalIdException Eccezione lanciata quando viene inserito un id non disponibile
	 */
		
	@RequestMapping (value = "/data/id/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getTweetId (@PathVariable ("id") String id) 
			throws GenericServiceException, IllegalIdException{
		FilterIdText fil = new FilterIdTextImpl();
		return new ResponseEntity<>(fil.getTweetFromId(tweetService.getData(), id), HttpStatus.OK);
	}
	
	/**
	 * Rotta per ottenere i dati di tipo {@link Tweet} filtrati in base ad una parola 
	 * di almeno due lettere contenuta nel testo
	 * 
	 * @param word Parola contenuta nel testo del {@link Tweet}
	 * @return {@link Tweet} che contengono la parola specificata, se presenti
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati
	 * @throws IllegalWordException Eccezione lanciata se si inserisce una parola troppo corta
	 * @throws WordNotFoundException Eccezione lanciata se si inserisce una parola non disponibile
	 */
	
	@RequestMapping (value = "/data/text/{word}", method = RequestMethod.GET)
	public ResponseEntity<Object> getTweetText(@PathVariable ("word") String word)
			throws GenericServiceException, IllegalWordException, WordNotFoundException{
		FilterIdText fil = new FilterIdTextImpl();
		return new ResponseEntity<>(fil.getTweetsFromText(tweetService.getData(), word), HttpStatus.OK);
	}
	
	/**
	 * Rotta per ottenere statistiche relative alla lunghezza del testo di tutti i {@link Tweet} del dataset
	 *  
	 * @return Statistiche sulla lunghezza del testo
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati
	 * @throws EmptyCollectionException Eccezione lanciata quando viene analizzata una Collection vuota
	 */
	
	@RequestMapping(value = "data/stats/text", method = RequestMethod.GET)
	public ResponseEntity<Object> getStatsTextNoFilter() 
			throws GenericServiceException, EmptyCollectionException{
		TweetStatsText tweetStatsText = new TweetStatsTextImpl();
		tweetStatsText.setStatsText(tweetService.getData());
		return new ResponseEntity<>(tweetStatsText.getStatsText(), HttpStatus.OK); 
	}
	
	/**
	 * Rotta per ottenere statistiche relative alla lunghezza del testo dei dati di tipo {@link Tweet}
	 * filtrati in base ad una parola di almeno due lettere contenuta nel testo
	 * 
	 * @param word Parola contenuta nel testo del {@link Tweet}
	 * @return Statistiche sui {@link Tweet} che contengono la parola specificata, se presenti
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati
	 * @throws EmptyCollectionException Eccezione lanciata quando viene analizzata una Collection vuota
	 * @throws IllegalWordException Eccezione lanciata se si inserisce una parola troppo corta
	 * @throws WordNotFoundException Eccezione lanciata se si inserisce una parola non disponibile
	 */
	
	@RequestMapping(value = "data/stats/text/{word}", method = RequestMethod.GET)
	public ResponseEntity<Object> getStatsText(@PathVariable("word") String word) 
					throws GenericServiceException, EmptyCollectionException, IllegalWordException, WordNotFoundException{
			FilterIdText fil =  new FilterIdTextImpl();
			ArrayList<Tweet> filteredArray = (ArrayList<Tweet>)fil.getTweetsFromText(tweetService.getData(), word);
			TweetStatsText tweetStatsText = new TweetStatsTextImpl();
			tweetStatsText.setStatsText(filteredArray);
			return new ResponseEntity<>(tweetStatsText.getStatsText(), HttpStatus.OK);
		
	}
	/**
	 * Rotta per ottenere statistiche relative alla lunghezza del testo dei dati di tipo {@link Tweet},
	 * filtrati in base ad una parola di almeno due lettere contenuta nel testo e alla distanza da una delle citta' disponibili
	 *
	 * @param word Parola contenuta nel testo del {@link Tweet}
	 * @param city Citta' da cui si vuole considerare la distanza
	 * @param filter Filtro di distanza ad applicare al dataset
	 * @return Statistiche sul testo dei Tweet che contengono la parola specificata e 
	 * filtrati in base alla citta' scelta
	 * 
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati
	 * @throws FilterNotFoundException Eccezione lanciata se si inserisce un filtro incompleto o scorretto
	 * @throws EmptyCollectionException Eccezione lanciata quando viene analizzata una Collection vuota
	 * @throws CityNotFoundException Eccezione lanciata se si inserisce una citta' non disponibile
	 * @throws NegativeValueException Eccezione lanciata se si inserisce un valore negativo nel filtro di distanza
	 * @throws IllegalIntervalException Eccezione lanciata se si inserisce nel filtro di distanza un intervallo in cui il primo valore e' maggiore del secondo
	 * @throws GenericFilterException Eccezione lanciata se si verifica un errore generico nella scelta del filtro
	 * @throws IllegalWordException Eccezione lanciata se si inserisce una parola troppo corta
	 * @throws WordNotFoundException Eccezione lanciata se si inserisce una parola non disponibile
	 * @throws BlankFilterException Eccezione lanciata se si inserisce un filtro vuoto
	 */
	
	
	@RequestMapping(value = "data/stats/text/{word}/{city}", method = RequestMethod.POST)
	public ResponseEntity<Object> getStatsTextWithFilter(@PathVariable("word") String word, 
			@PathVariable ("city") String city, @RequestBody String filter) 
					throws GenericServiceException, FilterNotFoundException, EmptyCollectionException, CityNotFoundException, NegativeValueException,
					IllegalIntervalException, GenericFilterException, IllegalWordException, WordNotFoundException, BlankFilterException{
		
			try {
				JSONObject obj = new JSONObject(filter);
				FilterUtils<Tweet> utl = new FilterUtils<>();
				TweetFilter tweetsFil = new TweetFilter((ArrayList<Tweet>)tweetService.getData(), utl);
				ArrayList<Tweet> filteredArray = tweetsFil.parseFilter(obj, city);
				FilterIdText fil = new FilterIdTextImpl();
				ArrayList<Tweet> newFilteredArray = (ArrayList<Tweet>)fil.getTweetsFromText(filteredArray, word);
				TweetStatsText tweetStatsText = new TweetStatsTextImpl();
				tweetStatsText.setStatsText(newFilteredArray);
				return new ResponseEntity<>(tweetStatsText.getStatsText(), HttpStatus.OK);
			}
			catch(NoSuchElementException e) {
				throw new BlankFilterException("Nessun filtro inserito");
			}
			catch(JSONException e) {
				throw new FilterNotFoundException("Il filtro inserito è incompleto o scorretto");
			}
	}
	
	/**
	 * Rotta per ottenere statistiche relative alla distanza dei dati di tipo {@link Tweet} dalla citta' 
	 * specificata, con la possibilita' di inserire filtri di distanza 
	 * 
	 * @param city Citta' da cui si vuole considerare la distanza
	 * @param filter Filtro di distanza ad applicare al dataset (facoltativo)
	 * @return Statistiche sulla distanza dei {@link Tweet}, eventualmente
	 * filtrati in base alla citta' scelta
	 * 
	 * @throws GenericServiceException Eccezione lanciata se si verifica un errore nella deserializzazione dei dati caricati
	 * @throws FilterNotFoundException Eccezione lanciata se si inserisce un filtro incompleto o scorretto
	 * @throws EmptyCollectionException Eccezione lanciata quando viene analizzata una Collection vuota
	 * @throws CityNotFoundException Eccezione lanciata se si inserisce una citta' non disponibile
	 * @throws NegativeValueException Eccezione lanciata se si inserisce un valore negativo nel filtro di distanza
	 * @throws IllegalIntervalException Eccezione lanciata se si inserisce nel filtro di distanza un intervallo in cui il primo valore e' maggiore del secondo
	 * @throws GenericFilterException Eccezione lanciata se si verifica un errore generico nella scelta del filtro
	 * @throws BlankFilterException Eccezione lanciata se si inserisce un filtro vuoto
	 */
	
	
	
	@RequestMapping(value = "data/stats/geo/{city}", method = RequestMethod.POST)
	public ResponseEntity<Object> getStatsGeoWithFilter(@PathVariable ("city") String city, 
			@RequestBody (required = false) String filter) 
					throws GenericServiceException, FilterNotFoundException, EmptyCollectionException, CityNotFoundException, NegativeValueException,
					IllegalIntervalException, GenericFilterException, BlankFilterException{
		if (filter == null) {
			TweetStatsGeo tweetStatsGeo = new TweetStatsGeoImpl();
			tweetStatsGeo.setStatsGeo(tweetService.getData(), city);
			return new ResponseEntity<>(tweetStatsGeo.getStatsGeo(), HttpStatus.OK);
		}
		else {
			try {
				JSONObject obj = new JSONObject(filter);
				FilterUtils<Tweet> utl = new FilterUtils<>();
				TweetFilter tweetsFil = new TweetFilter((ArrayList<Tweet>)tweetService.getData(), utl);
				ArrayList<Tweet> filteredArray = tweetsFil.parseFilter(obj, city);
				TweetStatsGeo tweetStatsGeo = new TweetStatsGeoImpl();
				tweetStatsGeo.setStatsGeo(filteredArray, city);
				return new ResponseEntity<>(tweetStatsGeo.getStatsGeo(), HttpStatus.OK);
			}
			catch(NoSuchElementException e) {
				throw new BlankFilterException("Nessun filtro inserito");
			}
			catch(JSONException e) {
				throw new FilterNotFoundException("Il filtro inserito è incompleto o scorretto");
			}
		}
	}
}
