/**
 * Warenkorb klasse , arbeitet mit einer hashmap 
 */
package bib.local.valueobjects;

import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
	private Map<Ware, Integer> warenListe;
	
	/**
	 * Erzeugt einen neuen Warenkorb
	 */
	public Warenkorb(){
		warenListe = new HashMap<Ware, Integer>();
	}
	/**
	 * Methode zum einfügen einer Ware in den warenkorb
	 * 
	 * @param eineWare Warenobject das eingefügt werden soll
	 * @param anzahl wie häufig sich die Ware im Korb befindet
	 */
	public void add (Ware eineWare, int anzahl){
		if(warenListe.containsKey(eineWare)){
			Integer aktAnzahl = warenListe.get(eineWare);
			aktAnzahl += anzahl;
			warenListe.put(eineWare, aktAnzahl);
		}else{
		warenListe.put(eineWare, anzahl);
		}
		eineWare.setBestand(eineWare.getBestand() - anzahl);
	}
}
