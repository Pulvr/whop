package bib.local.persistence;

import java.io.IOException;

import bib.local.valueobjects.Ware;

/**
 * @author teschke
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
	
	public boolean close();

	/**
	 * Methode zum Einlesen der Warendaten aus einer externen Datenquelle.
	 * 
	 * @return Waren-Objekt, wenn Einlesen erfolgreich, false null
	 */
	public Ware ladeWare() throws IOException;

	/**
	 * Methode zum Schreiben der Warendaten in eine externe Datenquelle.
	 * 
	 * @param w Waren-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich, false sonst
	 */
	public boolean speichereWare(Ware w) throws IOException;

	/*
	 *  Wenn später mal eine Kundenverwaltung ergänzt wird:

	public Kunde ladeKunde() throws IOException;

	public boolean speichereKunde(Kunde k) throws IOException;
	
	*/
}