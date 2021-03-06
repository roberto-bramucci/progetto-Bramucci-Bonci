# Progetto OOP Bramucci-Bonci
# Introduzione
L'applicazione presente in questa repository permette di ottenere dati, metadati e statistiche relativamente a tweet contenenti geolocalizzazione precedentemente selezionati tramite l'uso delle API di Twitter, eventualmente filtrati. É infatti possibile eseguire filtraggi legati alla distanza rispetto ad Ancona (e altre città italiane), al testo presente nei tweet e all'id dei tweet.
# Cosa si può fare?
Il seguente diagramma dei casi d'uso permette di visualizzare ciò che l'utente può fare tramite l'applicazione:
![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/UseCase.jpg)
# Come è fatto un tweet?
Ogni tweet che viene restituito tramite una chiamata mostrerà le informazioni descritte in tabella

|campo|significato|
|-|-|
|id|Stringa numerica che identifica ogni tweet |
|text|Testo presente all'interno del tweet|
|geo| Coordinate geografiche relative al tweet|
|distanceRoma|Distanza in chilometri da Roma rispetto alla geolocalizzazione del tweet|
|distanceAncona|Distanza in chilometri da Ancona rispetto alla geolocalizzazione del tweet|
|distanceMilano|Distanza in chilometri da Milano rispetto alla geolocalizzazione del tweet|
|distanceNapoli|Distanza in chilometri da Napoli rispetto alla geolocalizzazione del tweet|
# Gli id dei tweet
Vengono qui sotto riportati gli id dei tweet che abbiamo selezionato:
1264994235180253190,
1265011663150989314,
1269715922270531586,
1265019461968343042,
1269673618243563520,
1269697614120914952,
1269700437902589956,
1265023704679043072,
1269703231560658945,
1265023477146357760,
1265009995063734272,
1265018699704123397,
1264978710358155265,
1264987144499556352,
1264613421569490950,
1264967817977307139,
1264973453490151424,
1262800745730117632,
1264827517854191616,
1264819444724961280,
1265028908723798017,
1265023215971241984,
1265052257596264448,
1265052710027624450,
1265167808423505921,
1269676942766288897,
1265153348698689536,
1265189493876781056,
1265188848621019136,
1264958770758782977,
1264850093624233984,
1264781761466249216
# Come vengono restituiti i dati?
Tutti i dati vengono forniti tramite Postman, attraverso il quale è possibile effettuare le varie chiamate GET e POST, con risposta in formato JSON.  Riportiamo come esempio come appare la restituzione di un tweet e di una statistica in seguito ad una chiamata.

## Tweet

 	{
	        "id": "1264994235180253190",

		"text": "TRICOLOR!!!\n\nThe Italian Frecce Tricolore (the 
		acrobatic Italian Air Force Squad) flew over Turin 
		today.\n\nThey started from #Codogno (the town which 
		the symbol of the fight against #covid_19) and will 
		fly over many… https://t.co/i99LXh2k8K",

		"geo": {

			"x": 7.6777,

			"y": 45.0702

		},

		"distanceAncona": 491.6370694241606,

		"distanceRoma": 525.2659832732877,

		"distanceMilano": 126.22409086728408,

		"distanceNapoli": 712.2732630019941
	}


## Statistica

	   
	{
		    "field": "geo",

			"avgDist": 240.04996892770572,

			"maxDist": 674.2102661095064,

			"minDist": 0.01162228454654779,

			"sumDist": 7681.599005686583,

			"varDist": 36946.66245788262,

			"devStdDist": 192.2151462759442,

			"numTweetAnalyzed": 32
	}

# Quali chiamate si possono fare?
La Tabella mostra le possibili rotte dell'applicazione:
|Rotta  | Metodo | Body |Cosa viene restituito|
|---|---|---|---|
| /metadata |  GET||Metadati relativi al dataset|
|/data|GET||Dati di tipo Tweet relativi al dataset|
|/data/{city}|POST|Un filtro valido |Dati di tipo Tweet filtrati in base alla distanza da una delle città disponibili (Ancona, Milano, Napoli, Roma)|
|/data/id/{id}|GET||Un dato di tipo Tweet in base al suo id|
|/data/text/{word}|GET||Dati di tipo Tweet filtrati in base ad una parola contenuta nel testo|
|/data/stats/text|GET||Statistiche relative alla lunghezza del testo di tutti i Tweet del dataset|
|/data/stats/text/{word}|GET||Statistiche relative alla lunghezza del testo dei dati di tipo Tweet filtrati in base ad una parola contenuta nel testo|
|/data/stats/text/{word}/{city}|POST|Un filtro valido|Statistiche relative alla lunghezza del testo dei dati di tipo filtrati in base ad una parola contenuta nel testo e alla distanza da una delle città disponibili|
|/data/stats/geo/{city}|POST|Un filtro valido|Statistiche relative alla distanza dei dati di tipo Tweet dalla città specificata, con la possibilità di inserire filtri di distanza |
# Filtri
I filtri possono essere di due tipi:

 - **Filtri sulle parole**
 - **Filtri sulle distanze**


## Filtro sulle parole

 Questo filtro permette di ottenere i risultati solo per i tweet che contengono una determinata parola. In questo caso è sufficiente specificare la parola che vogliamo utilizzare come filtro e inserirla come path variable nelle rotte in cui è possibile eseguire tale azione.
 Se ad esempio vogliamo utilizzare la rotta 
 
`/data/text/{word} ` 
 
 per ottenere i dati relativi ai tweet che contengono la parola "Tricolore" tale rotta diventerà  :
 
 `/data/text/Tricolore`
 

## Filtro sulla distanza

Questo filtro serve ad ottenere i risultati solo per i tweet che rispettano i parametri relativi alla distanza da alcune città italiane. 
Questo filtro può essere utilizzato solo nelle rotte che utilizzano il metodo POST attraverso una stringa in formato JSON da inserire nel body della richiesta.

Gli operatori che permettono di eseguire il filtraggio sono riportati in questa tabella
|Operatore|Parametro| A cosa serve |
|--|--|--|
| "$gt" |Un valore numerico di tipo Double| Restituisce solo i dati o le statistiche relative ai tweet che si trovano a una distanza maggiore di quella indicata nel filtro |
|"$gte"|Un valore numerico di tipo Double| Restituisce solo i dati o le statistiche relative ai tweet che si trovano a una distanza maggiore o uguale a quella indicata nel filtro|
|"$lt"|Un valore numerico di tipo Double|Restituisce solo i dati o le statistiche relative ai tweet che si trovano a una distanza minore di quella indicata nel filtro|
|"$lte"|Un valore numerico di tipo Double|Restituisce solo i dati o le statistiche relative ai tweet che si trovano a una distanza minore o uguale a quella indicata nel filtro|
|"$bt"|Due valori numerici di tipo Double (il primo deve essere minore del secondo)|Restituisce solo i dati o le statistiche relative ai tweet che si trovano ad una distanza compresa tra i due valori indicati nel filtro|

| Alcuni esempi di filtri corretti |  
|--|
| {"$gt":100} |  
|{"$gte":100}|
|{"$lt":300}|
|{"$lte":300}|
|{"$bt":[100, 300]}|

É inoltre necessario specificare la città rispetto alla quale si vuole eseguire l'operazione di filtraggio inserendola come path variable nelle rotte in cui è possibile farlo.
Tali città sono le seguenti:
AN (Ancona)
MI (Milano)
RM (Roma)
NP (Napoli)
Ogni qualvolta venga inserita una città diversa da queste l'applicazione lancerà un'eccezione.

Se ad esempio vogliamo usare la rotta POST 

    /data/{city}

per ottenere i dati filtrati rispetto alla città di Ancona tale rotta diventerà:

    /data/AN

inoltre nel body si dovrà inserire un filtro valido. 

# Statistiche

 Le statistiche sono di due tipologie:

 - **Statistiche sulla distanza** 
 - **Statistiche sul testo**

## Statistiche sulla distanza

Le statistiche sulla distanza permettono di avere informazioni riguardanti le distanze in chilometri dei tweet (eventualmente filtrati) rispetto alla città selezionata nella rotta. 
Nella tabella descriviamo il significato dei diversi termini riguardanti questo tipo di statistica.
| Statistica | significato |
|--|--|
| avgDist | Distanza media dei tweet rispetto alla città scelta |
|maxDist|Distanza relativa al tweet che si trova più lontano dalla città scelta|
|minDist|Distanza relativa al tweet che si trova più vicino alla città scelta|
|sumDist|Somma delle distanze dei tweet calcolata rispetto alla città scelta|
|varDist| Varianza delle distanze dei tweet calcolata rispetto alla città scelta|
|devStdDist|Deviazione standard delle distanze dei tweet calcolata rispetto alla città scelta|
|numTweetAnalyzed|Numero di tweet analizzati per calcolare le statistiche|

Qui sotto viene riportato un esempio di statistiche rispetto alla città di Ancona effettuate su tutti i tweet disponibili:
   
    {
		"field": "geo",

		"avgDist": 240.04996892770572,

		"maxDist": 674.2102661095064,

		"minDist": 0.01162228454654779,

		"sumDist": 7681.599005686583,

		"varDist": 36946.66245788262,

		"devStdDist": 192.2151462759442,

		"numTweetAnalyzed": 32
	}

## Statistiche sul testo

Questo tipo di statistiche permettono di avere informazioni riguardo la lunghezza in caratteri dei testi relativi ai tweet presi in esame.
Nella tabella descriviamo il significato dei diversi termini riguardanti le statistiche sul testo.
|statistica | significato |
|--|--|
| avgLength | Lunghezza media del testo dei  Tweet analizzati |
|maxLength|Lunghezza massima del testo dei Tweet analizzati|
|minLength|Lunghezza minima del testo dei  Tweet analizzati|
|sumLength|Lunghezza totale del testo dei Tweet analizzati|
|numTweetAnalyzed|Numero di Tweet analizzati per calcolare le statistiche|

Qui sotto viene riportato un esempio di statistiche sul testo relative effettuate su tutti i tweet disponibili.

    {
	    "field": "text",

		"avgLength": 171.28125,

		"maxLength": 241.0,

		"minLength": 37.0,

		"sumLength": 5481.0,

		"numTweetAnalyzed": 32
	}
	
# Diagrammi UML
Qui sotto vengono riportati solo alcuni dei diagrammi UML realizzati, ma essi sono tutti disponibili in questa repository.

## Diagramma dei package

![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/package.jpg)

## Diagrammi delle classi

 - **Controller**

![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/controller.png)

 - **Model e util.filter**

![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/model_util.filter.png)

 - **util.stats**

![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/util.stats.png)

## Diagrammi delle sequenze

 - **getMeta**

![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/getMeta.jpg)

 - **getData**

![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/getData.jpg)

 - **getDataWithFilter**

![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/getDataWithFilter.jpg)

 - **getStatsTextWithFilter**

![](https://github.com/roberto-bramucci/progetto-Bramucci-Bonci/blob/master/getStatsTextWithFilter.jpg)

## Risorse Utilizzate

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [Eclipse](https://www.eclipse.org/)
- [UMLDesigner](http://www.umldesigner.org/)

## Autori

 - **Roberto Bramucci** (Service, gestione rotte, filtri, statistiche, gestione eccezioni, documentazione)
 - **Stefano Bonci** (diagrammi UML, filtri, gestione eccezioni, JUnit test, ReadMe.md, documentazione)


