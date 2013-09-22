package bib.local.domain.exceptions;
/**
 * Klasse die eine Exception wirft falls die angeforderte Menge negativ ist.
 * Beim hinzufügen und entfernen von Waren im Korb
 * @author Florian
 *
 */
public class BestellteMengeNegativException extends Exception {
	
	public BestellteMengeNegativException(){
		super("Die Menge darf nicht negativ sein!");
	}

}
