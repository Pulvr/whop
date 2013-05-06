package bib.local.domain.exceptions;
//
import bib.local.valueobjects.Ware;

/**
 * Exception zur Signalisierung, dass ein ware bereits existiert (z.B. bei einem Einf�gevorgang).
 * 
 * @author teschke
 */
public class WareExistiertBereitsException extends Exception {

	/**
	 * Konstruktor
	 * 
	 * @param ware Das bereits existierende ware
	 * @param zusatzMsg zus�tzlicher Text f�r die Fehlermeldung
	 */
	public WareExistiertBereitsException(Ware ware, String zusatzMsg) {
		super("Ware mit Bezeichnung " + ware.getBezeichnung() + " und Nummer " + ware.getNummer() 
				+ " existiert bereits" + zusatzMsg);
	}
}
