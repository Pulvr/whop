package bib.local.domain;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertNichtException;
import bib.local.persistence.FilePersistenceManager;
import bib.local.persistence.LogPersistenceManager;
import bib.local.persistence.PersistenceManager;
import bib.local.valueobjects.Ware;
import bib.local.valueobjects.WarenLog;

/**
 * Klasse zur Verwaltung der Waren, liest daten ein, speichert und hat methoden zum waren verwalten
 * zb einfügen, löschen oder Bestand einer ware ändern
 *
 */
public class WarenVerwaltung {

	// Verwaltung des Warenbestands in einem Vector
	private List<Ware> warenBestand = new Vector<Ware>();

	// Persistenz-Schnittstelle, die für die Details des Dateizugriffs verantwortlich ist
	private FilePersistenceManager pm = new FilePersistenceManager();
	
	//Der Warenbestand wird ebenfalls in einer Hashmap verwaltet dessen Key-Value Paar aus Bezeichnung der Ware
	//(String) und der Ware selbst besteht
	private HashMap<String, Ware> warenObjekte = new HashMap<String, Ware>();
	
	//Für den Warenlog der den bestand von waren mithilfe einer textdatei einliest
	private LogPersistenceManager logP = new LogPersistenceManager();
	private File warenLog = new File("WAREN_LOG.txt");
	private SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");

	
	/**
	 * Methode zum Einlesen von Warendaten aus einer Datei.
	 * Einlesen ist serialisert
	 * 
	 * @param datei Datei, die einzulesenden Warenbestand enthält
	 * @throws IOException
	 */
	public void liesDaten(String datei) throws IOException{
		// PersistenzManager für Lesevorgänge öffnen
		pm.openForReading(datei);
		
		try{
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
		} catch(ClassNotFoundException e){
			e.getMessage();
		} finally {
		// Persistenz-Schnittstelle wieder schließen
			pm.close();
		}
	}

	/**
	 * Methode zum Schreiben der Warendaten in eine Datei.
	 * Ebenfalls wird hier der Warenlog geschrieben
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
				Date d = new Date();
				String text = this.ft.format(d) + "\n" + w.getBezeichnung() + "\n" + w.getNummer() + "\n" + w.getPreis() + "\n" + w.getBestand() + "\n";
				this.logP.writeLog(this.warenLog, text);
			}
		}			
		
		// Persistenz-Schnittstelle wieder schließen
		pm.close();
	}
	
	/**
	 * Methode, die eine Ware an das Ende der Warenliste einfügt.
	 * Ebenfalls wird hier der Warenbestand in einen Vector und eine Hashmap gepackt
	 * @param eineWare die einzufügende Ware
	 * @throws WareExistiertBereitsException wenn die Ware bereits existiert
	 */
	public void wareEinfuegen(Ware eineWare) throws WareExistiertBereitsException {
		if (!warenBestand.contains(eineWare)){
			warenBestand.add(eineWare);
			warenObjekte.put(eineWare.getBezeichnung(), eineWare);
		}
		else
			throw new WareExistiertBereitsException(eineWare, " - in 'einfuegen()'");
	}
	
	/**
	 * Methode zum löschen von Waren aus dem Bestand sodass sie keinen Eintrag mehr haben
	 * @param eineWare
	 */
	public void entferneWare(Ware eineWare)throws WareExistiertNichtException{
		if(warenBestand.contains(eineWare)){
			warenBestand.remove(eineWare);
		}else
			throw new WareExistiertNichtException();
	}
	
	/**
	 * Methode die den Bestand einer angegebenen Ware ändert 
	 * @param w name der Ware
	 * @param neuerBestand der Neue Bestand dieser Ware
	 * @throws WareExistiertNichtException
	 */
	public void aendereBestand(Ware w, int neuerBestand)throws WareExistiertNichtException, IOException{
		if(warenBestand.contains(w)){
			w.setBestand(neuerBestand);
		}
		else
			throw new WareExistiertNichtException();
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
	
	/**
	 * Methode zum sortieren der Waren nach Bezeichnung, Nummer oder Bestand
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
	}
	
	
	/**
	 * Methode, die den Warenbestand als Vector zurückgibt.
	 * @return Eine Kopie des WarenBestandes als Vector
	 */
	public List<Ware> getWarenBestand() {
		List<Ware> warenBestandKopie = new Vector<Ware>(warenBestand);
		return warenBestandKopie;
	}
	
	/**
	 * Methode, die den Warenbestand als Hashmap zurückgibt 
	 * @return Eine Kopie des Warenbestandes als Hashmap
	 */
	public HashMap<String, Ware> getWarenObjekte(){
		HashMap<String, Ware> warenObjekteKopie = new HashMap<String, Ware>(warenObjekte);
		return warenObjekteKopie;
	}
	/**
	 * Methode die den Warenlog zurück gibt
	 * @param bezeichnung für welche Ware
	 * @param daysInPast wieviele Tage soll der Logg zurück liegen
	 * @return warenLog
	 * @throws ParseException
	 * @throws IOException
	 */
	public Vector<WarenLog> getWarenLog(String bezeichnung, int daysInPast) throws ParseException, IOException{
		return this.logP.readLog(this.warenLog, bezeichnung, daysInPast);
	}
}
