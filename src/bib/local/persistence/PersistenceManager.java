package bib.local.persistence;
//
import java.io.IOException;

import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 * @author Maik
 *
 * Allgemeine Schnittstelle für den Zugriff auf ein Speichermedium
 * (z.B. Datei oder Datenbank) zum Ablegen von beispielsweise
 * Waren- oder Kundendaten.
 * 
 * Das Interface muss von Klassen implementiert werden, die eine
 * Persistenz-Schnittstelle realisieren wollen.
 */
public interface PersistenceManager {

	public void openForReading(String datenquelle) throws IOException;
	
	public void openForWriting(String datenquelle) throws IOException;
	
	public boolean close() throws IOException;

	/**
	 * Methode zum Einlesen der Warendaten aus einer externen Datenquelle.
	 * 
	 * @return Waren-Objekt, wenn Einlesen erfolgreich, false null
	 */
	public Ware ladeWare() throws IOException, ClassNotFoundException;

	/**
	 * Methode zum Schreiben der Warendaten in eine externe Datenquelle.
	 * 
	 * @param w Waren-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereWare(Ware w) throws IOException;

	
	/**
	 * Methode zum Einlesen der Personendaten aus einer externen Datenquelle.
	 * 
	 * @return Waren-Objekt, wenn Einlesen erfolgreich, false null
	 */
	public Person ladePerson() throws IOException, ClassNotFoundException;
	
	/**
	 * Methode zum Schreiben der Personendaten in eine externe Datenquelle.
	 * 
	 * @param w Waren-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */

	public boolean speicherePerson(Person p) throws IOException;
	
	
}