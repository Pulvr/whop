package bib.local.valueobjects;

/**
 * Klasse zur Repraesentation einzelner Kunden.
 * 
 * Die Klasse wird derzeit noch nicht verwendet, weil die Bibliotheks-
 * Anwendung bisland nur Waren verwaltet.
 * 
 * @author teschke
 */
public class Kunde extends Person{

	

	
	private float umsatz = 0.0f;
	

    public Kunde(int nr, String name, String anrede, String strasse, String plz, String ort, String email,String usr, String pw,float um) {
		super(nr ,name ,anrede ,strasse ,plz ,ort ,email ,usr ,pw );
		this.umsatz=um;
	}
    
    public void setUmsatz(float umsatz) {
		this.umsatz = umsatz;
	}
    
	public float getUmsatz() {
		return umsatz;
	}
	// Weitere Dienste der Kunden-Objekte
}
