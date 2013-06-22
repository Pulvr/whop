package bib.local.domain;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.persistence.FilePersistenceManager;
import bib.local.persistence.PersistenceManager;
import bib.local.valueobjects.Ware;

/**
 * Klasse zur Verwaltung der Waren.
 * 
 * @version 3 (Verwaltung der Waren in Vector mit Generics)
 */
public class WarenVerwaltung {

	// Verwaltung des Warenbestands in einem Vector
	private List<Ware> warenBestand = new Vector<Ware>();

	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs verantwortlich ist
	private PersistenceManager pm = new FilePersistenceManager();
	
	private HashMap<String, Ware> warenObjekte = new HashMap<String, Ware>();

	
	/**
	 * Methode zum Einlesen von Warendaten aus einer Datei.
	 * 
	 * @param datei Datei, die einzulesenden Warenbestand enthält
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException {
		// PersistenzManager für Lesevorgänge öffnen
		pm.openForReading(datei);

		Ware eineWare;
		do {
			// Ware-Objekt einlesen
			eineWare = pm.ladeWare();
			if (eineWare != null) {
				// Ware in Liste einfügen
				try {
					wareEinfuegen(eineWare);
				} catch (WareExistiertBereitsException e1) {
					// Kann hier eigentlich nicht auftreten,
					// daher auch keine Fehlerbehandlung...
				}
			}
		} while (eineWare != null);

		// Persistenz-Schnittstelle wieder schließen
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
		// PersistenzManager für Schreibvorgänge öffnen
		pm.openForWriting(datei);

		if (!warenBestand.isEmpty()) {
			Iterator<Ware> iter = warenBestand.iterator();
			while (iter.hasNext()) {
				Ware w = iter.next();
				pm.speichereWare(w);				
			}
		}			
		
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	/**
	 * Methode, die eine Ware an das Ende der Warenliste einfügt.
	 * 
	 * @param eineWare die einzufügende Ware
	 * @throws WareExistiertBereitsException wenn die Ware bereits existiert
	 */
	public void wareEinfuegen(Ware eineWare) throws WareExistiertBereitsException {
		if (!warenBestand.contains(eineWare)/*&&!warenBestand.contains(eineWare.getNummer())*/){
			warenBestand.add(eineWare);
			warenObjekte.put(eineWare.getBezeichnung(), eineWare);
		}
		else
			throw new WareExistiertBereitsException(eineWare, " - in 'einfuegen()'");
	}

	/**
	 * Methode, die anhand einer Bezeichnung nach Waren sucht. Es wird eine Liste von Waren
	 * zurückgegeben, die alle Waren mit exakt übereinstimmender Bezeichnung enthält.
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
	
	
	//Methode zum entfernen von waren, der Eintrag in vector wird komplett entfernt und ist deswegen unbenutzt
	/*public void entfernen(Ware eineWare){
		if (warenBestand.contains(eineWare))
			warenBestand.remove(eineWare);
	}
	*/
	
	/**
	 * Methode zum sortieren der Waren nach Bezeichnung oder Nummer , die methode ist erweiterbar um noch nach bestand o.ä.
	 * zu sortieren
	 * 
	 * @param sortieren nach was soll sortiert werden
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
		

		case Bestand:
		Collections.sort(warenBestand, new Comparator<Ware>(){
			public int compare(Ware w1, Ware w2){
				return w1.getBestand() - w2.getBestand();
			}
		});
		break;
		
		//Sortieren nach Preis geht noch nicht ganz wegen int typecast
		case Preis:
			Collections.sort(warenBestand, new Comparator<Ware>(){
				public int compare(Ware w1, Ware w2){
					return (int) (w1.getPreis() - w2.getPreis());
				}
			});
			break;
		
		}
	}

	/**
	 * Ein enum was zum sortieren der Waren benötigt wird. 
	 * Enum ist ein spezieller Datentyp der den Wert von verschiedenen Konstanten annehmen kann
	 * 
	 */
	public static enum Sortierung{
		Bezeichnung,
		Nummer,
		Bestand,
		Preis
	}
	
	
	/**
	 * Methode, die den Warenbestand als Vector zurückgibt.
	 * 
	 * @return Liste aller Waren im Warenbestand
	 */
	public List<Ware> getWarenBestand() {
		// Achtung: hier wäre es sinnvoller / sicherer, eine Kopie des Vectors 
		// mit Kopien der Waren-Objekte zurückzugeben
		List<Ware> warenBestandKopie = new Vector<Ware>(warenBestand);
		return warenBestandKopie;
	}
	
	public HashMap<String, Ware> getWarenObjekte(){
		return this.warenObjekte;
	}
}
