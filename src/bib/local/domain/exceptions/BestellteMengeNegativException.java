package bib.local.domain.exceptions;
/**
 * Klasse die eine Exception wirft falls die Bestellte Menge negativ ist.
 * @author Maik
 *
 */
public class BestellteMengeNegativException extends Exception {
	
	public BestellteMengeNegativException(){
		super("Die bestellte Menge ist negativ.");
	}

}
