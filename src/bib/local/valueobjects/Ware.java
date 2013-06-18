package bib.local.valueobjects;

/**
 * Klasse zur Repräsentation einzelner Waren.
 */
public class Ware {

	// Attribute zur Beschreibung einer Ware:
	private String bezeichnung;
	private int nummer; 
	private int bestand;
	private int bestellteMenge;
	
	//
	//public Ware(String bezeichnung, int nr, int bestand) {
	//	this(bezeichnung, nr ,bestand, true);
	//}

	public Ware(String bezeichnung, int nr,int bstd) {
		nummer = nr;
		this.bezeichnung = bezeichnung;
		this.bestand= bstd;
	}
	
	// --- Dienste der Waren-Objekte ---

	/**
	 * Standard-Methode von Object überschrieben.
	 * Methode wird immer automatisch aufgerufen, wenn ein Waren-Objekt als String
	 * benutzt wird (z.B. in println(Ware);)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		//String verfuegbarkeit = verfuegbar ? "verfuegbar" : "ausverkauft";
		return ("Nr: " + nummer + " / Bezeichnung: " + bezeichnung + " / Bestand : " + bestand + " / ");
	}

	/**
	 * Standard-Methode von Object überschrieben.
	 * Methode dient Vergleich von zwei Waren-Objekten anhand ihrer Werte,
	 * d.h. bezeichnung und Nummer.
	 * 
	 * @see java.lang.Object#equals()
	 */
	public boolean equals(Object andereWare) {
		if (andereWare instanceof Ware) 
			return ((this.nummer == ((Ware) andereWare).nummer) 
					&& (this.bezeichnung.equals(((Ware) andereWare).bezeichnung)));
		else
			return false;
	}

	
	/*
	 * Ab hier Accessor-Methoden
	 */
	
	public int getNummer() {
		return this.nummer;
	}
	
	public int getBestand() {
		return this.bestand;
	}
	
	public void setBestand(int bestand){
		this.bestand = bestand;
	}
	
	public void setBestellteMenge(int menge){
		this.bestellteMenge = menge;
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}
	
	public int getBestellteMenge(){
		return this.bestellteMenge;
	}
	
}