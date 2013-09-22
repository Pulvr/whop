package bib.local.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.NichtVielfachesVonPackGroesseException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.PersonExistiertNichtException;
import bib.local.persistence.FilePersistenceManager;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 * Klasse zur Verwaltung von Personen, hat methoden zum laden und schreiben der Personen
 * einf�gen, warenkorbMethoden usw
 * 
 * @author Florian
 * 
 */
public class PersonenVerwaltung {

	// Verwaltung der Personen in einem Vector
	private List<Person> personen = new Vector<Person>();
	
	//Ebenso werden die Personen in einer Hashmap verwaltet dessen Key-Value Paar aus der UserNamen
	//(String) und der Person selbst besteht
	private HashMap<String, Person> personenObjekte = new HashMap<String, Person>();

	// Persistenz-Schnittstelle, die f�r die Details des Dateizugriffs
	// verantwortlich ist
	private FilePersistenceManager pm = new FilePersistenceManager();

	/**
	 * Methode zum Einlesen von Personendaten aus einer Datei.
	 * Einlesen ist serialisiertf
	 * 
	 * @param datei Datei, die einzulesende Kundeninformationen enth�lt
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException  {
		// PersistenzManager f�r Lesevorg�nge �ffnen
		pm.openForReading(datei);
		
		try{
			Person einePerson;
			do {
				// Personen-Objekt einlesen
				einePerson = pm.ladePerson();
				if (einePerson != null) {
					//Person in Liste einf�gen
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
		// Persistenz-Schnittstelle wieder schlie�en
		pm.close();
	}
	/**
	 * Methode zum einf�gen von Personen
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
	 * Methode zum l�schen einer Person aus der Liste
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
	 * Methode die das mitarbeiterattribut einer Person auf true setzt damit sie mitarbeiterberechtigung hat
	 * @param einePerson
	 * @throws PersonExistiertNichtException
	 */
	public void personBef�rdern(Person einePerson) throws PersonExistiertNichtException{
		if(personen.contains(einePerson)){
			einePerson.setMitarbeiterberechtigung(true);
		}else 
			throw new PersonExistiertNichtException();
	}
	
	/**
	 * Schreibe die Daten in eine Datei
	 * @param datei
	 * @throws IOException
	 */
	public void schreibeDaten(String datei) throws IOException  {
		// PersistenzManager f�r Schreibvorg�nge �ffnen
		pm.openForWriting(datei);

		if (!personen.isEmpty()) {
			Iterator<Person> iter = personen.iterator();
			while (iter.hasNext()) {
				Person p = iter.next();
				pm.speicherePerson(p);	
			}
		}			
		
		// Persistenz-Schnittstelle wieder schlie�en
		pm.close();
	}
	
	/**
	 * Methode die die Personen als Vector zur�ck gibt
	 * @return Kopie des Vectors
	 */
	public List<Person> getPersonen() {
		List<Person> personenKopie = new Vector<Person>(personen);
		return personenKopie;
	}
	
	/**
	 * Methode die die Personen als HashMap zur�ck gibt
	 * @return Kopie der HashMap
	 */
	public HashMap<String, Person> getPersonenObjekte(){
		HashMap<String, Person> personenObjekteKopie = new HashMap<String, Person>(personenObjekte);
		return personenObjekteKopie;
	}
	
	/**
	 * Methode die Waren in den Warenkorb legt, aber nur wenn die verlangte Menge > 0 und der
	 * Bestand der Anfrage standhalten kann 
	 * wenn NichtVielfachesVonPackGroesseException wird die ware nicht in den korb gelegt
	 * 
	 * @param menge wieviele von der angegeben Ware?
	 * @param ware welche ware?
	 * @param p welche Person?
	 * @throws BestellteMengeNegativException
	 * @throws NichtVielfachesVonPackGroesseException
	 */
	public void inWarenkorbLegen(int menge, Ware ware, Person p) throws BestellteMengeNegativException, NichtVielfachesVonPackGroesseException {
	    if (!ware.checkBestellmengeGueltig(menge)) {
	        throw new NichtVielfachesVonPackGroesseException();
	    } else if ((menge > 0) && (ware.getBestand() >= menge)) {
			p.inWarenKorbLegen(ware, menge);
		} else if (menge < 0) {
			throw new BestellteMengeNegativException();
		}
	}
	/**
	 * entfernt die angegebene Ware mit der angegebenen menge, sollte die angegebene menge die tats�chlich im warenkorb vorhandene menge
	 * �bersteigen, wird diese ware komplett entfernt anstatt in den minusbereich zu gehen
	 * 
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