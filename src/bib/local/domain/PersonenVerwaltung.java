package bib.local.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.PersonExistiertNichtException;
import bib.local.persistence.FilePersistenceManager;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 * Klasse zur Verwaltung von Personen (noch ohne Mitarbeiter und Kunden).
 * 
 * @author Maik
 */
public class PersonenVerwaltung {

	// Verwaltung der Personen in einem Vector
	private List<Person> personen = new Vector<Person>();
	
	//Ebenso werden die Personen in einer Hashmap verwaltet dessen Key-Value Paar aus der KundenNummer
	//(Integer) und der Person selbst besteht
	private HashMap<String, Person> personenObjekte = new HashMap<String, Person>();

	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs
	// verantwortlich ist
	private FilePersistenceManager pm = new FilePersistenceManager();

	/**
	 * Methode zum Einlesen von Personendaten aus einer Datei.
	 * 
	 * @param datei Datei, die einzulesende Kundeninformationen enthält
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException  {
		// PersistenzManager für Lesevorgänge öffnen
		pm.openForReading(datei);
		
		try{
			Person einePerson;
			do {
				// Personen-Objekt einlesen
				einePerson = pm.ladePerson();
				if (einePerson != null) {
					//Person in Liste einfügen
					try {
						personEinfuegen(einePerson);
					} catch (PersonExistiertBereitsException e1) {
						// Kann hier eigentlich nicht auftreten,
						// daher auch keine Fehlerbehandlung...
					}
				}
			} while (einePerson != null);
		}catch (ClassNotFoundException e){
			System.out.println(e.getMessage());
		}
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	/**
	 * Methode zum einfügen von Personen
	 * 
	 * @param einePerson
	 * @throws PersonExistiertBereitsException
	 */
	public void personEinfuegen(Person einePerson) throws PersonExistiertBereitsException{
		if (!personen.contains(einePerson)){
			personen.add(einePerson);
			personenObjekte.put(einePerson.getUsername(), einePerson);
		} else {
			throw new PersonExistiertBereitsException(einePerson, " - in 'einfuegen()'");
		}
	}
	/**
	 * Methode zum löschen einer Person aus der Liste
	 * @param einePerson
	 * @throws PersonExistiertNichtException
	 */
	public void personEntfernen(Person einePerson) throws PersonExistiertNichtException{
		if(personen.contains(einePerson)){
			personen.remove(einePerson);
		}else{
			throw new PersonExistiertNichtException();
		}
	}
	/**
	 * Schreibe die Daten in eine Datei
	 * 
	 * @param datei
	 * @throws IOException
	 */
	public void schreibeDaten(String datei) throws IOException  {
		// PersistenzManager für Schreibvorgänge öffnen
		pm.openForWriting(datei);

		if (!personen.isEmpty()) {
			Iterator<Person> iter = personen.iterator();
			while (iter.hasNext()) {
				Person p = iter.next();
				pm.speicherePerson(p);	
			}
		}			
		
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	/**
	 * Methode die die Personen als Vector zurück gibt
	 * @return Kopie des Vectors
	 */
	public List<Person> getPersonen() {
		List<Person> personenKopie = new Vector<Person>(personen);
		return personenKopie;
	}
	
	/**
	 * Methode die die Personen als HashMap zurück gibt
	 * @return Kopie der HashMap
	 */
	public HashMap<String, Person> getPersonenObjekte(){
		HashMap<String, Person> personenObjekteKopie = new HashMap<String, Person>(personenObjekte);
		return personenObjekteKopie;
	}
	
	/**
	 * Methode die Waren in den Warenkorb legt, aber nur wenn die verlangte Menge > 0 und der
	 * Bestand der Anfrage standhalten kann
	 * 
	 * @param menge wieviele von der angegeben Ware?
	 * @param ware welche ware?
	 * @param p welche Person?
	 * @throws BestellteMengeNegativException
	 */
	public void inWarenkorbLegen(int menge, Ware ware, Person p) throws BestellteMengeNegativException{
		if((menge > 0) && (ware.getBestand() >= menge)){
			p.inWarenKorbLegen(ware, menge);
		} else if (menge < 0) {
			throw new BestellteMengeNegativException();
		}
	}
	/**
	 * Von der Funktion her wie inWarenkorbLegen() nur werden die Waren entfernt
	 * @param menge
	 * @param ware
	 * @param p
	 * @throws BestellteMengeNegativException
	 */
	public void entferneAusWarenkorb(int menge, Ware ware, Person p) throws BestellteMengeNegativException{
		if((menge > 0) ){
			p.entferneAusWarenkorb(ware, menge);
		} else if (menge < 0){
			throw new BestellteMengeNegativException();
		}
		
	}
	
	/**
	 * Methode zum kaufen des Inhalts des Warenkorbs
	 * @param p Person desssen Korb gekauft werden soll
	 * @param warenkorb warenkorb dieser Person
	 */
	public void warenkorbKaufen(Person p, Vector<Ware> warenkorb){
		p.warenkorbKaufen(warenkorb);
		p.warenkorbLeeren();
	}
	/**
	 * Warenkorb einer Person komplett leeren
	 * @param p
	 */
	public void warenkorbLeeren( Person p){
		p.warenkorbLeeren();
	}

}