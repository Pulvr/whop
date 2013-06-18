package bib.local.domain.exceptions;

public class BestellteMengeNegativException extends Exception {
	
	public BestellteMengeNegativException(){
		super("Die bestellte Menge ist negativ.");
	}

}
