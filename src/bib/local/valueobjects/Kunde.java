package bib.local.valueobjects;


/**
 * Klasse zur Repraesentation einzelner Kunden.
 * 
 * Die Klasse wird derzeit noch nicht verwendet, weil die Bibliotheks-
 * Anwendung bisland nur Waren verwaltet.
 * 
 * @author teschke
 */
public class Kunde {

	private int kundenNr;

	private String name;
	private float umsatz = 0.0f;
	private String strasse = "";
	private String plz = "";
	private String wohnort = "";

    public Kunde(int nr, String name) {
		kundenNr = nr;
		this.name = name;
	}
    
	// Methoden zum Setzen und Lesen der Kunden-Eigenschaften,
	// z.B. getStrasse() und setStrasse()
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getUmsatz() {
		return umsatz;
	}

	public void setUmsatz(float umsatz) {
		this.umsatz = umsatz;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getWohnort() {
		return wohnort;
	}

	public void setWohnort(String wohnort) {
		this.wohnort = wohnort;
	}

	public int getKundenNr() {
		return kundenNr;
	}

	// Weitere Dienste der Kunden-Objekte
}
