package bib.local.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import bib.local.valueobjects.Kunde;
import bib.local.valueobjects.Mitarbeiter;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 *
 * Realisierung einer Schnittstelle zur persistenten Speicherung von
 * Daten in Dateien.
 * @see bib.local.persistence.PersistenceManager
 */
public class FilePersistenceManager implements PersistenceManager {

	private BufferedReader reader = null;
	private PrintWriter writer = null;
	
	public void openForReading(String datei) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(datei));
	}

	public void openForWriting(String datei) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(datei)));
	}

	public boolean close() {
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
	public Ware ladeWare() throws IOException {
		// Titel einlesen
		String bezeichnung = liesZeile();
		if (bezeichnung == null) {
			// keine Daten mehr vorhanden
			return null;
		}
		
		// Nummer einlesen ...
		String nummerString = liesZeile();
		// ... und von String in int konvertieren
		int nummer = Integer.parseInt(nummerString);
		
		//Bestand einlesen ...
		String bestandString = liesZeile();
		// ... und von String in int konvertieren
		int bestand = Integer.parseInt(bestandString);
		
		//Preis einlesen ...
		String preisString = liesZeile();
		// ... und von String in float konvertieren
		float preis = Float.parseFloat(preisString);
		
		// neues Buch-Objekt mit eingelesenen Daten anlegen und zurückgeben
		return new Ware(bezeichnung, nummer, bestand, preis);
	}

	/**
	 * Methode zum Schreiben der Warendaten in eine externe Datenquelle.
	 * Das Verfügbarkeitsattribut wird in der Datenquelle (Datei) als "t" oder "f"
	 * codiert abgelegt.
	 * 
	 * @param w Waren-Objekt, das gespeichert werden soll
	 * @return true, wenn Schreibvorgang erfolgreich,sonst false
	 */
	public boolean speichereWare(Ware w) throws IOException {
		// Bezeichnung und Nummer schreiben
		schreibeZeile(w.getBezeichnung());
		schreibeZeile(Integer.valueOf(w.getNummer()).toString());
		schreibeZeile(Integer.valueOf(w.getBestand()).toString());
		schreibeZeile(Float.valueOf(w.getPreis()).toString());
		return true;
	}

	
	/**
	 * Methode zum einlesen von Personen aus einer externen Datei
	 */
	public Person ladePerson() throws IOException {
		// Name einlesen
				String name = liesZeile();
				if (name == null) {
					// keine Daten mehr vorhanden
					return null;
				}
				// Daten der Person einlesen ...
				String nummernString = liesZeile();
				int nummer = Integer.parseInt(nummernString);
				String anrede = liesZeile();
				String strasse = liesZeile();
				String plz = liesZeile();
				String wohnort = liesZeile();
				String email = liesZeile();
				String username = liesZeile();
				String password = liesZeile();
				//String berechtigung = liesZeile();
				/*if (berechtigung.equals("Kunde")){
					String umsatzString = liesZeile();
					int umsatz = Integer.parseInt(umsatzString);
					
					return new Kunde(nummer, name ,anrede ,strasse ,plz ,wohnort,email, username, password, umsatz);
					
				}else if(berechtigung.equals("Mitarbeiter")){
					String gehaltString = liesZeile();
					Float gehalt = Float.parseFloat(gehaltString);
					
					return new Kunde(nummer,name ,anrede ,strasse ,plz ,wohnort,email, username, password, gehalt);
					
				}*/
				return new Person(nummer,name ,anrede ,strasse ,plz ,wohnort,email, username, password);
				
				
	}
	
	/**
	 * Methode zum speichern von Personen in einer externen Datei
	 * 
	 * @param p Person die gespeichert wird
	 */
	public boolean speicherePerson(Person p) throws IOException {
		schreibeZeile(p.getName());
		schreibeZeile(Integer.valueOf(p.getNummer()).toString());
		schreibeZeile(p.getAnrede());
		schreibeZeile(p.getStrasse());
		schreibeZeile(p.getPlz());
		schreibeZeile(p.getWohnort());
		schreibeZeile(p.getEmail());
		schreibeZeile(p.getUsername());
		schreibeZeile(p.getPassword());
		if(p instanceof Kunde){
			Kunde k = (Kunde) p;
			schreibeZeile(new Float(k.getUmsatz()).toString());
            return true;
		}else if(p instanceof Mitarbeiter){
			Mitarbeiter m= (Mitarbeiter) p;
			schreibeZeile(new Float(m.getGehalt()).toString());
			return true;
		}
		return true;
	}

	
	
	/*
	 * Private Hilfsmethoden
	 */
	
	private String liesZeile() throws IOException {
		if (reader != null)
			return reader.readLine();
		else
			return "";
	}

	private void schreibeZeile(String daten) {
		if (writer != null)
			writer.println(daten);
	}
}
