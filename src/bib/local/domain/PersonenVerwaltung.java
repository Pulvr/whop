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

	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs
	// verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();

	/**
	 * Methode zum Einlesen von Personendaten aus einer Datei.
	 * 
	 * @param datei
	 *            Datei, die einzulesende Kundeninformationen enthält
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException {
		// PersistenzManager für Lesevorgänge öffnen
		pm.openForReading(datei);

		Person einePerson;
		do {
			// Ware-Objekt einlesen
			einePerson = pm.ladePerson();
			if (einePerson != null) {
				// Ware in Liste einfügen
				//try {
					einfuegen(einePerson);
				//} catch (WareExistiertBereitsException e1) {
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
				//}
			}
		} while (einePerson != null);

		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}

	private void einfuegen(Person einePerson) {
		// TODO Auto-generated method stub

	}
	
	public void schreibeDaten(String datei) throws IOException  {
		// PersistenzManager für Schreibvorgänge öffnen
		pm.openForWriting(datei);

		if (!personen.isEmpty()) {
			Iterator<Person> iter = personen.iterator();
			while (iter.hasNext()) {
				Person b = iter.next();
				pm.speicherePerson(b);				
			}
		}			
		
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	public List<Person> getPersonen() {
		// Achtung: hier wäre es sinnvoller / sicherer, eine Kopie des Vectors 
		// mit Kopien der Buch-Objekte zurückzugeben
		return personen;
	}
}