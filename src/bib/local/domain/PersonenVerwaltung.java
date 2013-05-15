package bib.local.domain;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import bib.local.domain.exceptions.WareExistiertBereitsException;
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

	// Persistenz-Schnittstelle, die f�r die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	/**
	 * Methode zum Einlesen von Personendaten aus einer Datei.
	 * 
	 * @param datei
	 *            Datei, die einzulesende Kundeninformationen enth�lt
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException {
		// PersistenzManager f�r Lesevorg�nge �ffnen
		pm.openForReading(datei);

		Person einePerson;
		do {
			// Ware-Objekt einlesen
			einePerson = pm.ladePerson();
			if (einePerson != null) {
				// Ware in Liste einf�gen
				//try {
					einfuegen(einePerson);
				//} catch (WareExistiertBereitsException e1) {
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
				//}
			}
		} while (einePerson != null);

		// Persistenz-Schnittstelle wieder schlie�en
		pm.close();
	}

	private void einfuegen(Person einePerson) {
		// TODO Auto-generated method stub

	}
	
	public void schreibeDaten(String datei) throws IOException  {
		// PersistenzManager f�r Schreibvorg�nge �ffnen
		pm.openForWriting(datei);

		if (!personen.isEmpty()) {
			Iterator<Person> iter = personen.iterator();
			while (iter.hasNext()) {
				Person b = iter.next();
				pm.speicherePerson(b);				
			}
		}			
		
		// Persistenz-Schnittstelle wieder schlie�en
		pm.close();
	}
	
	public List<Person> getPersonen() {
		// Achtung: hier w�re es sinnvoller / sicherer, eine Kopie des Vectors 
		// mit Kopien der Buch-Objekte zur�ckzugeben
		return personen;
	}
}