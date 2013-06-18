package bib.local.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.persistence.FilePersistenceManager;
import bib.local.persistence.PersistenceManager;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 * Klasse zur Verwaltung von Personen (Mitarbeiter und Kunden).
 * 
 * @author Maik Eisermann
 */
public class PersonenVerwaltung {

	// Verwaltung der Personen in einem Vector
	private List<Person> personen = new Vector<Person>();
	
	private HashMap<Integer, Person> personenObjekte = new HashMap<Integer, Person>();

	// Persistenz-Schnittstelle, die f�r die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	/**
	 * Methode zum Einlesen von Personendaten aus einer Datei.
	 * 
	 * @param datei Datei, die einzulesende Kundeninformationen enth�lt
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException {
		// PersistenzManager f�r Lesevorg�nge �ffnen
		pm.openForReading(datei);

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
		if (!personen.contains(einePerson))
			{personen.add(einePerson);
			personenObjekte.put(einePerson.getNummer(), einePerson);}
		else
			throw new PersonExistiertBereitsException(einePerson, " - in 'einfuegen()'");
	}
	/**
	 * Schreibe die Daten in eine Datei
	 * 
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
	
	public List<Person> getPersonen() {
		// Achtung: hier w�re es sinnvoller / sicherer, eine Kopie des Vectors 
		// mit Kopien der Personen-Objekte zur�ckzugeben
		return personen;
	}
	
	public HashMap<Integer, Person> getPersonenObjekte(){
		return this.personenObjekte;
	}
	
	public void inWarenkorbLegen(int menge, Ware ware, Person p) throws BestellteMengeNegativException{
		if((menge > 0) && (ware.getBestand() >= menge)){
			p.inWarenKorbLegen(ware, menge);
		} else if (menge < 0) {
			throw new BestellteMengeNegativException();
		}
	}
}