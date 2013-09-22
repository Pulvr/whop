package bib.local.domain;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;


import bib.local.domain.WarenVerwaltung.Sortierung;
import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.NichtVielfachesVonPackGroesseException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.PersonExistiertNichtException;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertNichtException;
import bib.local.valueobjects.MassengutWare;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Rechnung;
import bib.local.valueobjects.Ware;
import bib.local.valueobjects.WarenLog;

/**
 * Klasse zur Verwaltung eines (sehr einfachen) Lagers.
 * Bietet Methoden zum Zurückgeben aller Waren im Bestand, 
 * zur Suche nach Waren, zum Einfügen neuer Waren 
 * und zum Speichern des Bestands.
 * 
 */
public class LagerVerwaltung {
	// Präfix für Namen der Dateien, in der die Lagerdaten gespeichert sind
	private String datei = "";
	
	private WarenVerwaltung meineWaren;
	private PersonenVerwaltung meinePersonen;
	private Rechnung rechnung;
	
	/**
	 * Konstruktor, der die Basisdaten (Waren, Personen) aus Dateien einliest
	 * (Initialisierung des shops).
	 * 
	 * Namensmuster für Dateien:
	 *   datei+"_W.ser" ist die Datei der Waren
	 *   datei+"_P.ser" ist die Datei der Personen
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
	 * Methode, die eine java.util.List aller im Bestand befindlichen Waren zurückgibt.
	 * 
	 * @return Liste aller Waren im Lager
	 */
	public List<Ware> gibAlleWaren() {
		return meineWaren.getWarenBestand();
	}
	
	/**
	 * Methode, die eine java.util.List aller Personen gibt
	 * 
	 * @return Liste aller Personen
	 */
	public List<Person> gibAllePersonen() {
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
		return meineWaren.sucheWaren(bezeichnung); 
	}

    /**
     * Methode zum Einfügen einer neuen Ware in den Bestand. 
     * Wenn die Ware bereits im Bestand ist, wird der Bestand nicht geändert.
     * 
     * @param bezeichnung Bezeichnung des Ware
     * @param nummer Nummer der Waren
     * @param bestand Bestand der Ware
     * @param preis Preis der Ware in float
     * @param packungsGroesse sollte dieser wert größer als eins sein handelt es sich um eine MassengutWare
     * @throws WareExistiertBereitsException wenn die Ware bereits existiert
     */
    public void fuegeWareEin(String bezeichnung, int nummer, int bestand, float preis, int packungsGroesse) throws WareExistiertBereitsException {
        Ware w = null;
        if (packungsGroesse > 1) {
            w = new MassengutWare(bezeichnung, nummer,  bestand, preis, packungsGroesse);
        } else {
            w = new Ware(bezeichnung, nummer,  bestand, preis);
        }
        meineWaren.wareEinfuegen(w);
    }
    
	/**
	 * Methode zum löschen von Waren aus dem Bestand
	 * @param eineWare die zu löschende Ware
	 * @throws WareExistiertNichtException
	 */
	public void entferneWare(Ware eineWare)throws WareExistiertNichtException{
		meineWaren.entferneWare(eineWare);
	}
	
	/**
	 * Methode zum neusetzen des Bestands einer Ware, ohne Addition oder Subtraktion
	 * @param w Die Ware
	 * @param neuerBestand der neue Bestand
	 * @throws WareExistiertNichtException
	 */
	public void aendereBestand(Ware w,int neuerBestand)throws WareExistiertNichtException, IOException{
		meineWaren.aendereBestand(w, neuerBestand);
	}
	
	/**
	 * Methode zum Einfügen einer Person in eine Liste
	 * @throws PersonExistiertBereitsException 
	 */
	public void fuegePersonEin(int nr, String name, String anr, String strasse, String plz, String ort ,String email, String usr, String pw, boolean ma) throws PersonExistiertBereitsException {
		Person p = new Person(nr,name,anr,strasse,plz,ort , email, usr, pw, ma);
		meinePersonen.personEinfuegen(p);
	}
	
	/**
	 * Methode zum löschen einer Person
	 * 
	 * @param einePerson
	 * @throws PersonExistiertNichtException
	 */
	public void personEntfernen(Person einePerson) throws PersonExistiertNichtException{
		meinePersonen.personEntfernen(einePerson);
	}
	/**
	 * Setzt das Mitarbeiter attribut einer Person auf true um ihr mitarbeiterBerechtigungen zu geben
	 * @param einePerson
	 * @throws PersonExistiertNichtException
	 */
	public void personBefördern(Person einePerson)throws PersonExistiertNichtException{
		meinePersonen.personBefördern(einePerson);
	}
	
	/**
	 * Methode zum sortieren des Vectors der die Waren speichert.
	 * Nach Bezeichnung , Waren nummer oder Preis
	 * @param aufgabe soll nach Bezeichnung oder Nummer sortiert werden?
	 */
	 public void sortiereDieWaren(String aufgabe) {
		 if (aufgabe.equals ("b")){
			 meineWaren.artikelSortieren(Sortierung.Bezeichnung);   
		 }else if (aufgabe.equals("n")){
			 meineWaren.artikelSortieren(Sortierung.Nummer); 
		 }else if (aufgabe.equals("e")){
			 meineWaren.artikelSortieren(Sortierung.Bestand);
		 }
	 }

	/**
	 * Methode zum Speichern des Warenbestands in einer Datei.
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
	 * den Inhalt des Warenkorbs kaufen
	 * @param p Die Person
	 * @param warenkorb der Warenkorb der Person
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
	 * @throws NichtVielfachesVonPackGroesseException
	 */
	public void inWarenKorbLegen(int menge, Ware ware, Person p) throws BestellteMengeNegativException, NichtVielfachesVonPackGroesseException {
		meinePersonen.inWarenkorbLegen(menge, ware, p);
	}
	
	/**
	 * Methode die Waren aus dem Korb entfernen kann, sollte die angegebene Zahl die Anzahl der Waren im Korb übersteigen
	 * wird die Anzahl dieser Ware im Korb auf 0 gesetzt anstatt ins negative zu gehen
	 * @param menge Wieviele von der Ware entfernen
	 * @param ware welche Ware soll entfernt werden
	 * @param p Der Korb der Person
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
	
	/**
	 * Gibt den Warenlog für eine Ware zurück mit einer Angabe wie weit der Log zurück liegen soll
	 * @param bezeichnung Log für welche Ware?
	 * @param daysInPast wieviele Tage soll der Log zurück liegen
	 * @return den WarenLog
	 * @throws IOException
	 * @throws ParseException
	 */
	public Vector<WarenLog> getWarenLog(String bezeichnung, int daysInPast)throws IOException,ParseException{
		return meineWaren.getWarenLog(bezeichnung, daysInPast);
	}
	
	/**
	 * Gibt die PersonenVerwaltung zurück
	 * @return meinePersonen
	 */
	public PersonenVerwaltung getMeinePersonenVerwaltung(){
		return this.meinePersonen;
	}
	
	/**
	 * Gibt die WarenVerwaltung zurpck
	 * @return meineWaren
	 */
	public WarenVerwaltung getMeineWarenVerwaltung(){
		return this.meineWaren;
	}
	
	/**
	 * Gibt die Rechnung die beim Kauf entsteht zurück
	 * @return rechnung
	 */
	public Rechnung getRechnung(){
		return this.rechnung;
	}
	/**
	 * Hiermit lïässt sich die Rechnung für einen Kauf setzen
	 * @param r die Rechnung
	 */
	public void setRechnung(Rechnung r){
		this.rechnung = r;
	}
}
