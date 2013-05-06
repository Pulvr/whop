package bib.local.domain.exceptions;
//
import bib.local.valueobjects.Ware;

/**
 * Exception zur Signalisierung, dass ein ware bereits existiert (z.B. bei einem Einfügevorgang).
 * 
 * @author teschke
 */
public class WareExistiertBereitsException extends Exception {

	/**
	 * Konstruktor
	 * 
	 * @param ware Das bereits existierende ware
	 * @param zusatzMsg zusätzlicher Text für die Fehlermeldung
	 */
	public WareExistiertBereitsException(Ware ware, String zusatzMsg) {
		super("Ware mit Bezeichnung " + ware.getBezeichnung() + " und Nummer " + ware.getNummer() 
				+ " existiert bereits" + zusatzMsg);
	}
}
