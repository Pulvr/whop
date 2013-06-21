package bib.local.domain.exceptions;
/**
 * Klasse die eine Exception wirft falls die angeforderte Menge negativ ist.
 * Beim hinzuf�gen und entfernen von Waren im Korb
 * @author Maik
 *
 */
public class BestellteMengeNegativException extends Exception {
	
	public BestellteMengeNegativException(){
		super("Die bestellte Menge ist negativ.");
	}

}
