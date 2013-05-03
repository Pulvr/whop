package bib.local.valueobjects;

public class Mitarbeiter extends Person{
	
	private float gehalt;
	
	public Mitarbeiter (int nr, String name, String anrede, String strasse, String plz, String ort, float gehalt){
		super(nr, name, anrede, strasse, plz, ort);
		this.gehalt=gehalt;
		
	}
	
	public float getGehalt() {
		return gehalt;
	}
	
	public void setGehalt(float gehalt){
		this.gehalt = gehalt;
	}
}
