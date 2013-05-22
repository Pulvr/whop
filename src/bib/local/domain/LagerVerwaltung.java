package bib.local.domain;

import java.io.IOException;
import java.util.List;

import bib.local.domain.WarenVerwaltung.Sortierung;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 * Klasse zur Verwaltung eines (sehr einfachen) Lagers.
 * Bietet Methoden zum Zur�ckgeben aller Waren im Bestand, 
 * zur Suche nach Waren, zum Einf�gen neuer Waren 
 * und zum Speichern des Bestands.
 * 
 * @author Meisermann
 * @version 3 (Verwaltung der Waren in Vector mit Generics)
 */
public class LagerVerwaltung {
	// Pr�fix f�r Namen der Dateien, in der die Lagerdaten gespeichert sind
	private String datei = "";
	
	private WarenVerwaltung meineWaren;
	private PersonenVerwaltung meinePersonen;
	// private KundenVerwaltung meineKunden;
	// hier weitere Verwaltungsklassen, z.B. f�r Waren oder Angestellte
	
	/**
	 * Konstruktor, der die Basisdaten (Waren, Personen) aus Dateien einliest
	 * (Initialisierung des shops).
	 * 
	 * Namensmuster f�r Dateien:
	 *   datei+"_B.txt" ist die Datei der Waren
	 *   datei+"_P.txt" ist die Datei der Personen
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
		meinePersonen = new PersonenVerwaltung();
		meinePersonen.liesDaten(datei+"_P.txt");
	}


	/**
	 * Methode, die eine Liste aller im Bestand befindlichen Waren zur�ckgibt.
	 * 
	 * @return Liste aller Waren im Lager
	 */
	public List<Ware> gibAlleWaren() {
		// einfach delegieren an meineWaren
		return meineWaren.getWarenBestand();
	}
	
	/**
	 * Methode, die eine Liste aller Personen
	 * 
	 * @return Liste aller Personen
	 */
	
	public List<Person> gibAllePersonen() {
		// einfach delegieren an meinePersonen
		return meinePersonen.getPersonen();
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
		meineWaren.wareEinfuegen(w);
//		} catch (WareExistiertBereitsException e) {
//			return false;
//		}
//		return true;
	}
	/**
	 * Methode zum Einf�gen einer Person in eine Liste
	 * 
	 * 
	 * @param nr Nummer der Person
	 * @param name Name der Person
	 * @param anr Anrede der Person
	 * @param strasse 
	 * @param plz
	 * @param ort
	 * @param email
	 * @param usr
	 * @param pw 
	 * @throws PersonExistiertBereitsException wenn die Ware bereits existiert wird aber noch nicht verwendet
	 */
	
	public void fuegePersonEin(int nr, String name, String anr, String strasse, String plz, String ort ,String email, String usr, String pw) throws PersonExistiertBereitsException {
		Person p = new Person(nr,name,anr,strasse,plz,ort , email, usr, pw);
//		try {
		meinePersonen.personEinfuegen(p);
//		} catch (WareExistiertBereitsException e) {
//			return false;
//		}
//		return true;
	}
	
	 // Sortiert die Waren entweder nach bezeichnung oder nummer 
	 public void sortiereDieWaren(String aufgabe) {
		 	
		 if (aufgabe.equals ("b")) 
			 meineWaren.artikelSortieren(Sortierung.Bezeichnung);   
		 else if (aufgabe.equals("n")) 
			 meineWaren.artikelSortieren(Sortierung.Nummer); 
	 }
	
	/** Entfernt waren aus dem Vector, wird nicht verwendet weil letztendlich nur der bestand ver�ndert wird und
	 *  keine Waren aus dem Vector entfernt werden, vielleicht noch n�tzlich momementan nicht
	 */
	 /*public void entferneWare(String bezeichnung, int nummer, int bestand){
		Ware w = new Ware(bezeichnung, nummer , bestand);
		
		meineWaren.entfernen(w);
		
	 }*/

	/**
	 * Methode zum Speichern des Warenbestands in einer Datei.
	 * 
	 * @throws IOException
	 */
	public void schreibeWaren() throws IOException {
		meineWaren.schreibeDaten(datei+"_B.txt");
	}
	/**
	 * Methode zum speichern der Personen in einer Datei
	 * 
	 * @throws IOException
	 */
	public void schreibePersonen() throws IOException {
		meinePersonen.schreibeDaten(datei+"_P.txt");
	}

	// TODO: Weitere Funktionen der LagerVerwaltung, z.B. ausleihen, zur�ckgeben etc.
	// ...
}
