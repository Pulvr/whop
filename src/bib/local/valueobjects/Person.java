package bib.local.valueobjects;

public class Person {
	private int nummer;
	private String anrede="";
	private String name="";
	private String strasse = "";
	private String plz = "";
	private String wohnort = "";

	public Person(int nr, String name, String anrede, String strasse, String plz, String ort){
		this.nummer = nr;
		this.name = name;
		this.anrede = anrede;
		this.strasse = strasse;
		this.plz = plz;
		this.wohnort = ort;
		
	}
	
	public int getNummer(){
		return nummer;
	}

}
