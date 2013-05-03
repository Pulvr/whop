package bib.local.domain;


import java.util.Vector;

import bib.local.persistence.FilePersistenceManager;
import bib.local.persistence.PersistenceManager;
import bib.local.valueobjects.Person;



/**
 * Klasse zur Verwaltung von Personen (Kunde und Mitarbeiter)
 * @author Florian
 *
 */

public class PersonenVerwaltung {
	
	private Vector<Person> personenListe = new Vector<Person>();
	
	private PersistenceManager pm = new FilePersistenceManager();

}
