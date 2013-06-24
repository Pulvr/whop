package bib.local.domain;

import java.io.IOException;
import java.util.List;

import bib.local.domain.WarenVerwaltung.Sortierung;
import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertNichtException;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 * Klasse zur Verwaltung eines (sehr einfachen) Lagers.
 * Bietet Methoden zum Zurückgeben aller Waren im Bestand, 
 * zur Suche nach Waren, zum Einfügen neuer Waren 
 * und zum Speichern des Bestands.
 * 
 * @author Maik
 */
public class LagerVerwaltung {
	// Präfix für Namen der Dateien, in der die Lagerdaten gespeichert sind
	private String datei = "";
	
	private WarenVerwaltung meineWaren;
	private PersonenVerwaltung meinePersonen;
	// private KundenVerwaltung meineKunden;
	// hier weitere Verwaltungsklassen, z.B. für Waren oder Angestellte
	
	/**
	 * Konstruktor, der die Basisdaten (Waren, Personen) aus Dateien einliest
	 * (Initialisierung des shops).
	 * 
	 * Namensmuster für Dateien:
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
		meineWaren.liesDaten(datei+"_W.txt");
		
		// Kundenkartei aus Datei einlesen
		meinePersonen = new PersonenVerwaltung();
		meinePersonen.liesDaten(datei+"_P.txt");
	}


	/**
	 * Methode, die eine Liste aller im Bestand befindlichen Waren zurückgibt.
	 * 
	 * @return Liste aller Waren im Lager
	 */
	public List<Ware> gibAlleWaren() {
		// einfach delegieren an meineWaren
		return meineWaren.getWarenBestand();
	}
	
	/**
	 * Methode, die eine Liste aller Personen gibt
	 * 
	 * @return Liste aller Personen
	 */
	public List<Person> gibAllePersonen() {
		// einfach delegieren an meinePersonen
		return meinePersonen.getPersonen();
	}

	/**
	 * Methode zum Suchen von Waren anhand der Bezeichnung. Es wird eine Liste von Waren
	 * zurückgegeben, die alle Waren mit exakt übereinstimmender Bezeichnung enthält.
	 * 
	 * @param bezeichnung Bezeichnung der gesuchten Ware
	 * @return Liste der gefundenen Waren (evtl. leer)
	 */
	public List<Ware> sucheNachBezeichnung(String bezeichnung) {
		// einfach delegieren an meineWaren
		return meineWaren.sucheWaren(bezeichnung); 
	}

	/**
	 * Methode zum Einfügen einer neuen Ware in den Bestand. 
	 * Wenn die Ware bereits im Bestand ist, wird der Bestand nicht geändert.
	 * 
	 * @param bezeichnung Bezeichnung des Ware
	 * @param nummer Nummer der Waren
	 * @throws WareExistiertBereitsException wenn die Ware bereits existiert
	 */
	public void fuegeWareEin(String bezeichnung, int nummer, int bestand, float preis) throws WareExistiertBereitsException {
		Ware w = new Ware(bezeichnung, nummer,  bestand, preis);
		meineWaren.wareEinfuegen(w);
	}
	
	public void aendereBestand(Ware w,int neuerBestand)throws WareExistiertNichtException{
		meineWaren.aendereBestand(w, neuerBestand);
	}
	
	/**
	 * Methode zum Einfügen einer Person in eine Liste
	 * 
	 * @param Daten der Person
	 * @throws PersonExistiertBereitsException wenn die Ware bereits existiert wird aber noch nicht verwendet
	 */
	
	public void fuegePersonEin(int nr, String name, String anr, String strasse, String plz, String ort ,String email, String usr, String pw, boolean ma) throws PersonExistiertBereitsException {
		Person p = new Person(nr,name,anr,strasse,plz,ort , email, usr, pw, ma);
		meinePersonen.personEinfuegen(p);
	}
	
	/**
	 * Methode zum sortieren des Vectors der die Waren speichert.
	 * Nach Bezeichnung oder Waren nummer
	 * 
	 * @param aufgabe soll nach Bezeichnung oder Nummer sortiert werden?
	 */
	 public void sortiereDieWaren(String aufgabe) {
		 	
		 if (aufgabe.equals ("b")){
			 meineWaren.artikelSortieren(Sortierung.Bezeichnung);   
		 }else if (aufgabe.equals("n")){
			 meineWaren.artikelSortieren(Sortierung.Nummer); 
		 }else if (aufgabe.equals("e")){
			 meineWaren.artikelSortieren(Sortierung.Bestand);
		 }else if (aufgabe.equals("p")){
			 meineWaren.artikelSortieren(Sortierung.Preis);
		 }
	 }
	
	/** Entfernt waren aus dem Vector, wird nicht verwendet weil letztendlich nur der bestand verändert wird und
	 *  keine Waren aus dem Vector entfernt werden, vielleicht noch nützlich momementan nicht
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
	
	/**
	 * Methode mit der Waren in den Korb gelegt werden
	 * 
	 * @param menge wieviele Waren sollen gekauft werden?
	 * @param ware welche Ware?
	 * @param p welche Person?
	 * @throws BestellteMengeNegativException
	 */
	public void inWarenKorbLegen(int menge, Ware ware, Person p) throws BestellteMengeNegativException{
		meinePersonen.inWarenkorbLegen(menge, ware, p);
	}
	
	public void entferneAusWarenkorb(int menge, Ware ware, Person p)throws BestellteMengeNegativException{
		meinePersonen.entferneAusWarenkorb(menge, ware, p);
	}
	
	public void warenkorbLeeren(Person p){
		meinePersonen.warenkorbLeeren(p);
	}
	
	/**
	 * Gibt die PersonenVerwaltung zurück
	 * @return
	 */
	public PersonenVerwaltung getMeinePersonenVerwaltung(){
		return this.meinePersonen;
	}
	
	/**
	 * Gibt die WarenVerwaltung zurück
	 * @return
	 */
	public WarenVerwaltung getMeineWarenVerwaltung(){
		return this.meineWaren;
	}
}
