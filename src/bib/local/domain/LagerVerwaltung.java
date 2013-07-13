package bib.local.domain;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import bib.local.domain.WarenVerwaltung.Sortierung;
import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertNichtException;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Rechnung;
import bib.local.valueobjects.Ware;
import bib.local.valueobjects.WarenLog;

/**
 * Klasse zur Verwaltung eines (sehr einfachen) Lagers.
 * Bietet Methoden zum Zur�ckgeben aller Waren im Bestand, 
 * zur Suche nach Waren, zum Einf�gen neuer Waren 
 * und zum Speichern des Bestands.
 * 
 * @author Maik
 */
public class LagerVerwaltung {
	// Pr�fix f�r Namen der Dateien, in der die Lagerdaten gespeichert sind
	private String datei = "";
	
	private WarenVerwaltung meineWaren;
	private PersonenVerwaltung meinePersonen;
	private Rechnung rechnung;
	
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
		meineWaren.liesDaten(datei+"_W.ser");
		
		// Kundenkartei aus Datei einlesen
		meinePersonen = new PersonenVerwaltung();
		meinePersonen.liesDaten(datei+"_P.ser");
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
	public void fuegeWareEin(String bezeichnung, int nummer, int bestand, float preis) throws WareExistiertBereitsException {
		Ware w = new Ware(bezeichnung, nummer,  bestand, preis);
		meineWaren.wareEinfuegen(w);
	}
	
	/**
	 * Methode zum neusetzen des Bestands eines Artikels, bisher ohne addition oder subtraktion
	 * @param w
	 * @param neuerBestand
	 * @throws WareExistiertNichtException
	 */
	public void aendereBestand(Ware w,int neuerBestand)throws WareExistiertNichtException, IOException{
		meineWaren.aendereBestand(w, neuerBestand);
	}
	
	/**
	 * Methode zum Einf�gen einer Person in eine Liste
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

	/**
	 * Methode zum Speichern des Warenbestands in einer Datei.
	 * 
	 * @throws IOException
	 */
	public void schreibeWaren() throws IOException {
		meineWaren.schreibeDaten(this.datei+"_W.ser");
	}
	
	/**
	 * Methode zum speichern der Personen in einer Datei
	 * @throws IOException
	 */
	public void schreibePersonen() throws IOException {
		meinePersonen.schreibeDaten(this.datei+"_P.ser");
	}
	
	/**
	 * den Warenkorb kaufen trololol
	 * @param p
	 * @param warenkorb
	 */
	public void warenkorbKaufen(Person p, Vector<Ware> warenkorb){
		meinePersonen.warenkorbKaufen(p, warenkorb);
	}
	
	/**
	 * Methode mit der Waren in den Korb gelegt werden
	 * @param menge wieviele Waren sollen gekauft werden?
	 * @param ware welche Ware?
	 * @param p welche Person?
	 * @throws BestellteMengeNegativException
	 */
	public void inWarenKorbLegen(int menge, Ware ware, Person p) throws BestellteMengeNegativException{
		meinePersonen.inWarenkorbLegen(menge, ware, p);
	}
	
	/**
	 * Methode die Waren aus dem Korb entfernen kann
	 * @param menge
	 * @param ware
	 * @param p
	 * @throws BestellteMengeNegativException
	 */
	public void entferneAusWarenkorb(int menge, Ware ware, Person p)throws BestellteMengeNegativException{
		meinePersonen.entferneAusWarenkorb(menge, ware, p);
	}
	
	/**
	 * Methode die den Warenkorb eines angegeben Users leert
	 * @param p Name der Person
	 */
	public void warenkorbLeeren(Person p){
		meinePersonen.warenkorbLeeren(p);
	}
	
	public Vector<WarenLog> getWarenLog(String bezeichnung, int daysInPast)throws IOException,ParseException{
		return meineWaren.getWarenLog(bezeichnung, daysInPast);
	}
	
	/**
	 * Gibt die PersonenVerwaltung zur�ck
	 * @return
	 */
	public PersonenVerwaltung getMeinePersonenVerwaltung(){
		return this.meinePersonen;
	}
	
	/**
	 * Gibt die WarenVerwaltung zur�ck
	 * @return
	 */
	public WarenVerwaltung getMeineWarenVerwaltung(){
		return this.meineWaren;
	}
	
	public Rechnung getRechnung(){
		return this.rechnung;
	}
	
	public void setRechnung(Rechnung r){
		this.rechnung = r;
	}
}
