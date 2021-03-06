package it.univpm.progettoOOP.util.filter;

import java.util.ArrayList;
import java.util.Collection;

import it.univpm.progettoOOP.exceptions.CityNotFoundException;
import it.univpm.progettoOOP.exceptions.GenericFilterException;
import it.univpm.progettoOOP.exceptions.GenericServiceException;
import it.univpm.progettoOOP.exceptions.IllegalIntervalException;
import it.univpm.progettoOOP.exceptions.NegativeValueException;
import it.univpm.progettoOOP.model.Tweet;

/**
 * Classe per la determinazione del filtro richiesto e la selezione dei dati che soddisfano 
 * i requisiti inseriti
 * 
 * @author Roberto Bramucci
 * @author Stefano Bonci
 * @version 1.0
 * @param <E> Tipo dell'oggetto da filtrare
 */
public class FilterUtils<E> {
	
	/**
	 * Metodo per la verifica del filtro di distanza inserito e la sua applicazione ad un {@link Tweet}
	 * 
	 * @param value Oggetto {@link Tweet} da confrontare con il valore del filtro 
	 * @param operator Operatore del filtro
	 * @param city Citta' da cui si vuole considerare la distanza 
	 * @param th Valori relativi al filtro
	 * @return Valore boolean. Restituisce true se il campo geo del Tweet verifica la condizione del filtro
	 * @throws CityNotFoundException Eccezione lanciata se si inserisce una citta' non disponibile
	 * @throws IllegalIntervalException Eccezione lanciata se si inserisce nel filtro di distanza un intervallo in cui il primo valore e' maggiore del secondo
	 * @throws NegativeValueException Eccezione lanciata se si inserisce un valore negativo nel filtro di distanza
	 * @throws GenericFilterException Eccezione lanciata se si verifica un errore generico nella scelta del filtro
	 */
	
	public static boolean check(Tweet value, String operator, String city, Double... th)
			throws CityNotFoundException, IllegalIntervalException, NegativeValueException, GenericFilterException{
		if (th.length == 1 && th[0] instanceof Double && value instanceof Tweet) {	
			Double thC = (Double)th[0];
			if(thC < 0)
				throw new NegativeValueException("Il valore inserito è negativo");
			Double valuec = value.chooseCity(city);
			if (operator.equals("$gt"))
				return valuec > thC;
			else if (operator.equals("$gte"))
				return valuec >= thC;
			else if (operator.equals("$lt"))
				return valuec < thC;
			else if (operator.equals("$lte"))
				return valuec <= thC;
		}
		else if (th.length == 2 && th[0] instanceof Double && th[1] instanceof Double && value instanceof Tweet) {
			Double thC1 = (Double)th[0];
			Double thC2 = (Double)th[1];
			Double valuec = value.chooseCity(city);
			if (operator.equals("$bt")) {
				if (thC1<=thC2)
					return valuec >= thC1 && valuec <= thC2;
				else
					throw new IllegalIntervalException("Il primo valore deve essere maggiore del secondo");
			}
		}
		throw new GenericFilterException("Errore nella scelta del filtro");
	}
	
	/**
	 * Metodo per selezionare i dati che soddisfano i requisiti relativi al filtro di distanza inserito
	 * 
	 * @param src Dataset iniziale
	 * @param operator Operatore del filtro
	 * @param city Citta' da cui si vuole considerare la distanza 
	 * @param value Valori relativi al filtro
	 * @return Dataset filtrato
	 * @throws CityNotFoundException Eccezione lanciata se si inserisce una citta' non disponibile
	 * @throws IllegalIntervalException Eccezione lanciata se si inserisce nel filtro di distanza un intervallo in cui il primo valore e' maggiore del secondo
	 * @throws NegativeValueException Eccezione lanciata se si inserisce un valore negativo nel filtro di distanza
	 * @throws GenericFilterException Eccezione lanciata se si verifica un errore generico nella scelta del filtro
	 */
	
	public Collection<Tweet> select(Collection<Tweet> src, String operator, String city, Double... value) 
			throws CityNotFoundException, IllegalIntervalException, NegativeValueException, GenericFilterException{
		Collection<Tweet> out = new ArrayList<>();
		for(Tweet item : src) {
			try {
				if(FilterUtils.check(item, operator, city, value))
					out.add(item);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.out.println("Errore nei parametri del metodo");
				System.exit(1);
			} catch (SecurityException e) {
				e.printStackTrace();
				System.out.println("Violazione di sicurezza");
				System.exit(1);
			}
		}
		return out;
	}
}
