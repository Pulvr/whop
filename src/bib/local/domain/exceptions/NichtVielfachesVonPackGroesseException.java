package bib.local.domain.exceptions;
/**
 * Klasse die eine Exception wirft falls die angeforderte Menge nicht der Packungsgröße einer massengutware entspricht
 * Beim hinzufügen und entfernen von Waren im Korb
 * @author Florian
 *
 */
public class NichtVielfachesVonPackGroesseException extends Exception {
	
	public NichtVielfachesVonPackGroesseException(){
		super("Die bestellte Menge ist entspricht nicht den verfügbaren Packungsgrößen.");
	}

}
