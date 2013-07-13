package bib.local.persistence;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 *
 * Realisierung einer Schnittstelle zur persistenten Speicherung von
 * Daten in Dateien.
 * @see bib.local.persistence.PersistenceManager
 */
public class FilePersistenceManager implements PersistenceManager {

	private ObjectInputStream reader = null;
	private ObjectOutputStream writer = null;
	
	public void openForReading(String datei) throws FileNotFoundException ,IOException{
		reader = new ObjectInputStream(new FileInputStream(datei));
	}

	public void openForWriting(String datei) throws IOException {
		writer = new ObjectOutputStream(new FileOutputStream(datei));
	}

	public boolean close() throws IOException{
		
		if (writer != null)
			writer.close();
		
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return false;
			}
		}

		return true;
	}

	/**
	 * Methode zum Einlesen der Warendaten aus einer externen Datenquelle.
	 * Das Verfügbarkeitsattribut ist in der Datenquelle (Datei) als "t" oder "f"
	 * codiert abgelegt.
	 * 
	 * @return Waren-Objekt, wenn Einlesen erfolgreich, false null
	 */
	public Ware ladeWare() throws IOException,ClassNotFoundException {
		// Titel einlesen
		
		try{
			Ware w = (Ware)reader.readObject();
		
			
			// neues Waren-Objekt mit eingelesenen Daten anlegen und zurückgeben
			return w;
		} catch (EOFException exc){
			return null;
		}
	}

	/**
	 * Methode zum Schreiben der Warendaten in eine externe Datenquelle.
	 * 
	 * @param w Waren-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich,sonst false
	 */
	public boolean speichereWare(Ware w) throws IOException {
		try{
			// Bezeichnung, Nummer, Bestand und Preis schreiben
			writer.writeObject(w);
			writer.flush();
			return true;
		}catch(IOException e){
			return false;
		}
	}

	
	/**
	 * Methode zum einlesen von Personen aus einer externen Datei
	 */
	public Person ladePerson() throws IOException ,ClassNotFoundException{
		// Name einlesen
		try{
			Person p = (Person) reader.readObject();
			return p;
		}catch(EOFException e){
			return null;
		}
	}
	
	/**
	 * Methode zum speichern von Personen in einer externen Datei
	 * 
	 * @param p Person die gespeichert wird
	 */
	public boolean speicherePerson(Person p) throws IOException {
		try{
		writer.writeObject(p);
		writer.flush();
		return true;
		}catch(IOException e){
			return false;
		}
	}
	
	/*
	 * Private Hilfsmethoden
	 */
//	private String liesZeile() throws IOException {
//		if (reader != null)
//			return reader.readLine();
//		else
//			return "";
//	}

//	private void schreibeZeile(String daten) {
//		if (writer != null)
//			writer.println(daten);
//	}
}
