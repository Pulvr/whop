package bib.local.domain;

import java.io.IOException;
import java.util.List;

import bib.local.domain.WarenVerwaltung.Sortierung;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.valueobjects.Ware;

/**
 * Klasse zur Verwaltung eines (sehr einfachen) Lagers.
 * Bietet Methoden zum Zur�ckgeben aller Waren im Bestand, 
 * zur Suche nach Waren, zum Einf�gen neuer Waren 
 * und zum Speichern des Bestands.
 * 
 * @author teschke
 * @version 3 (Verwaltung der Waren in Vector mit Generics)
 */
public class LagerVerwaltung {
	// Pr�fix f�r Namen der Dateien, in der die Bibliotheksdaten gespeichert sind
	private String datei = "";
	
	private WarenVerwaltung meineWaren;
	// private KundenVerwaltung meineKunden;
	// hier weitere Verwaltungsklassen, z.B. f�r Autoren oder Angestellte
	
	/**
	 * Konstruktor, der die Basisdaten (B�cher, Kunden, Autoren) aus Dateien einliest
	 * (Initialisierung der Bibliothek).
	 * 
	 * Namensmuster f�r Dateien:
	 *   datei+"_B.txt" ist die Datei der Waren
	 *   datei+"_K.txt" ist die Datei der Kunden
	 * 
	 * @param datei
	 * @throws IOException, z.B. wenn eine der Dateien nicht existiert.
	 */
	public LagerVerwaltung(String datei) throws IOException {
		this.datei = datei;
		
		// Warenbestand aus Datei einlesen
		meineWaren = new WarenVerwaltung();
		meineWaren.liesDaten(datei+"_B.txt");
		// Kundenkartei aus Datei einlesen
//		meineKunden = new KundenVerwaltung();
//		meineKunden.liesDaten(datei+"_K.txt");
//		meineKunden.schreibeDaten(datei+"_K.txt");
	}


	/**
	 * Methode, die eine Liste aller im Bestand befindlichen B�cher zur�ckgibt.
	 * 
	 * @return Liste aller B�cher im Bestand der Bibliothek
	 */
	public List<Ware> gibAlleWaren() {
		// einfach delegieren an meineWaren
		return meineWaren.getWarenBestand();
	}

	/**
	 * Methode zum Suchen von Waren anhand der Bezeichnung. Es wird eine Liste von Waren
	 * zur�ckgegeben, die alle Waren mit exakt �bereinstimmender Bezeichnung enth�lt.
	 * 
	 * @param bezeichnung Bezeichnung der gesuchten Ware
	 * @return Liste der gefundenen Waren (evtl. leer)
	 */
	public List<Ware> sucheNachBezeichnung(String bezeichnung) {
		// einfach delegieren an meineWaren
		return meineWaren.sucheWaren(bezeichnung); 
	}

	/**
	 * Methode zum Einf�gen einer neuen Ware in den Bestand. 
	 * Wenn die Ware bereits im Bestand ist, wird der Bestand nicht ge�ndert.
	 * 
	 * @param bezeichnung Bezeichnung des Ware
	 * @param nummer Nummer der Waren
	 * @throws WareExistiertBereitsException wenn die Ware bereits existiert
	 */
	public void fuegeWareEin(String bezeichnung, int nummer, int bestand) throws WareExistiertBereitsException {
		Ware w = new Ware(bezeichnung, nummer,  bestand);
//		try {
		meineWaren.einfuegen(w);
//		} catch (WareExistiertBereitsException e) {
//			return false;
//		}
//		return true;
	}
	
	 public void sortiereDieWaren(String aufgabe) {
		 	
		 if (aufgabe.equals ("b")) 
			 meineWaren.artikelSortieren(Sortierung.Bezeichnung);   
		 else if (aufgabe.equals("n")) 
			 meineWaren.artikelSortieren(Sortierung.Nummer); 
	 }
	
	public void entferneWare(String bezeichnung, int nummer, int bestand){
		Ware w = new Ware(bezeichnung, nummer , bestand);
		
		meineWaren.entfernen(w);
		
	}

	/**
	 * Methode zum Speichern des Warenbestands in einer Datei.
	 * 
	 * @throws IOException
	 */
	public void schreibeWaren() throws IOException {
		meineWaren.schreibeDaten(datei+"_B.txt");
	}

	// TODO: Weitere Funktionen der LagerVerwaltung, z.B. ausleihen, zur�ckgeben etc.
	// ...
}
