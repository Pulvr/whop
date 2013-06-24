package bib.local.domain.exceptions;
/**
 * Klasse die eine Exception wirft falls die angeforderte Menge negativ ist.
 * Beim hinzuf�gen und entfernen von Waren im Korb
 * @author Maik
 *
 */
public class WarenkorbLeerException extends Exception {
	
	public WarenkorbLeerException(){
		super("Ihr Warenkorb ist leer.");
	}

}
