package bib.local.domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;

import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.persistence.FilePersistenceManager;
import bib.local.persistence.PersistenceManager;
import bib.local.valueobjects.Ware;

/**
 * Klasse zur Verwaltung von Waren.
 * 
 * @author teschke
 * @version 3 (Verwaltung der Waren in Vector mit Generics)
 */
public class WarenVerwaltung {

	// Verwaltung des Warenbestands in einem Vector
	private List<Ware> warenBestand = new Vector<Ware>();

	// Persistenz-Schnittstelle, die f�r die Details des Dateizugriffs verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();
	
	
	/**
	 * Methode zum Einlesen von Warendaten aus einer Datei.
	 * 
	 * @param datei Datei, die einzulesenden Warenbestand enth�lt
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException {
		// PersistenzManager f�r Lesevorg�nge �ffnen
		pm.openForReading(datei);

		Ware eineWare;
		do {
			// Ware-Objekt einlesen
			eineWare = pm.ladeWare();
			if (eineWare != null) {
				// Ware in Liste einf�gen
				try {
					einfuegen(eineWare);
				} catch (WareExistiertBereitsException e1) {
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
				}
			}
		} while (eineWare != null);

		// Persistenz-Schnittstelle wieder schlie�en
		pm.close();
	}
//
	/**
	 * Methode zum Schreiben der Warendaten in eine Datei.
	 * 
	 * @param datei Datei, in die der Warenbestand geschrieben werden soll
	 * @throws IOException
	 */
	public void schreibeDaten(String datei) throws IOException  {
		// PersistenzManager f�r Schreibvorg�nge �ffnen
		pm.openForWriting(datei);

		if (!warenBestand.isEmpty()) {
			Iterator<Ware> iter = warenBestand.iterator();
			while (iter.hasNext()) {
				Ware b = iter.next();
				pm.speichereWare(b);				
			}
		}			
		
		// Persistenz-Schnittstelle wieder schlie�en
		pm.close();
	}
	
	/**
	 * Methode, die eine Ware an das Ende der Warenliste einf�gt.
	 * 
	 * @param eineWare die einzuf�gende Ware
	 * @throws WareExistiertBereitsException wenn die Ware bereits existiert
	 */
	public void einfuegen(Ware eineWare) throws WareExistiertBereitsException {
		if (!warenBestand.contains(eineWare))
			warenBestand.add(eineWare);
		else
			throw new WareExistiertBereitsException(eineWare, " - in 'einfuegen()'");
	}

	/**
	 * Methode, die anhand einer Bezeichnung nach Waren sucht. Es wird eine Liste von Waren
	 * zur�ckgegeben, die alle Waren mit exakt �bereinstimmender Bezeichnung enth�lt.
	 * 
	 * @param bezeichnung Bezeichnung der gesuchten Ware
	 * @return Liste der Waren mit gesuchter Bezeichnung (evtl. leer)
	 */
	public List<Ware> sucheWaren(String bezeichnung) {
		List<Ware> ergebnis = new Vector<Ware>();
		
		Iterator<Ware> iter = warenBestand.iterator();
		while (iter.hasNext()) {
			Ware ware = iter.next();
			if ( ware.getBezeichnung().equals(bezeichnung)) {
				ergebnis.add(ware);
			}
		}
		
		return ergebnis;
	}
	
	
	//Methode zum entfernen von waren, der Eintrag in vector wird komplett entfernt
	/*public void entfernen(Ware eineWare){
		if (warenBestand.contains(eineWare))
			warenBestand.remove(eineWare);
	}
	*/
	
	
	/**
	 * enum ist ein spezieller Datentyp, der einer Variable erlaubt einen 
	 * gewissen Wert anzunehemen
	 *
	 */
	public void artikelSortieren(Sortierung sortieren){
		switch (sortieren){
		case Bezeichnung:
			Collections.sort(warenBestand, new Comparator<Ware>(){
				public int compare(Ware w1, Ware w2){
					return w1.getBezeichnung().compareTo(w2.getBezeichnung());
				}
			});
			break;
			
		case Nummer:
			Collections.sort(warenBestand, new Comparator<Ware>(){
				public int compare(Ware w1, Ware w2){
					return w1.getNummer() - w2.getNummer();
				}
			});
			break;
		}
		
	}
	
	public static enum Sortierung{
		Bezeichnung,
		Nummer
	}
	
	
	
	// TODO: Weitere Methoden, z.B. Entfernen von Waren aus dem Bestand
	// ...

	/**
	 * Methode, die den Warenbestand als Vector zur�ckgibt.
	 * 
	 * @return Liste aller Waren im Warenbestand
	 */
	public List<Ware> getWarenBestand() {
		// Achtung: hier w�re es sinnvoller / sicherer, eine Kopie des Vectors 
		// mit Kopien der Buch-Objekte zur�ckzugeben
		return warenBestand;
	}
}
