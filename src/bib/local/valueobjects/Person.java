package bib.local.valueobjects;

public class Person {
	private int nummer;
	private String anrede="";
	private String name="";
	private String strasse = "";
	private String plz = "";
	private String wohnort = "";
	
	
	//Konstruktor
	public Person(int nr, String name, String anrede, String strasse, String plz, String ort){
		this.nummer = nr;
		this.name = name;
		this.anrede = anrede;
		this.strasse = strasse;
		this.plz = plz;
		this.wohnort = ort;
	}
	
	@Override
	/**
	 * Standard-Methode von Object überschrieben.
	 * Methode dient Vergleich von zwei Personen-Objekten anhand ihrer Werte,
	 * d.h. nummer und name.
	 * 
	 * @see java.lang.Object#equals()
	 */
	public boolean equals(Object anderePerson){
		if (anderePerson instanceof Person)
			return (( nummer == ((Person) anderePerson).nummer)
					&& name.equals(((Person) anderePerson).name));
		else
			return false;
		
	}
	
	
	//Accessor-Methoden
	public int getNummer(){
		return nummer;
	}
	
	public String getAnrede(){
		return anrede;
	}
	
	public String getName(){
		return name;
	}
	
	public String getStraße(){
		return strasse;
	}
	
	public String getPlz(){
		return plz;
	}
	
	public String getWohnort(){
		return wohnort;
	}

}
