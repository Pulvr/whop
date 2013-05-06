package bib.local.valueobjects;
//
public class Mitarbeiter extends Person{
	
	//Mitarbeiter hat extra-Attribut gehalt
	private float gehalt;
	
	//Konstruktor
	public Mitarbeiter (int nr, String name, String anrede, String strasse, String plz, String ort, String email, String usr, String pw, float gehalt){
		super(nr, name, anrede, strasse, plz, ort, email, usr, pw);
		this.gehalt=gehalt;
		
	}
	
	
	//Accessor-Methoden
	public float getGehalt() {
		return gehalt;
	}
	
	public void setGehalt(float gehalt){
		this.gehalt = gehalt;
	}
}
