/**
 * Personenklasse , Superklasse von Kunde und Mitarbeiter
 */

package bib.local.valueobjects;

import java.util.HashMap;
import java.util.Vector;

public class Person {
	private int nummer;
	private String anrede="";
	private String name="";
	private String strasse = "";
	private String plz = "";
	private String wohnort = "";
	private String email = "";
	private String username = "";
	private String password = "";
	private Vector<Ware> warenkorb;
	
	public Person (){
		
	}
	//Konstruktor
	public Person(int nr, String name, String anr, String strasse, String plz, String ort ,String email, String usr, String pw){
		this.nummer = nr;
		this.name = name;
		this.anrede = anr;
		this.strasse = strasse;
		this.plz = plz;
		this.wohnort = ort;
		this.email = email;
		this.username = usr;
		this.password = pw;
	}
	
	
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
	
	/**
	 * Standard-Methode  von Object überschrieben
	 * dient der einfachereren ausgabe der attribute der Person
	 */
	public String toString() {
		return ("User: " + username + "\n" + anrede + " " + name + " / E-Mail: " + email + "\nAdresse:\n" + strasse + "\t" + plz + "\t" + wohnort + "\n\n");
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
	
	public String getStrasse(){
		return strasse;
	}
	
	public String getPlz(){
		return plz;
	}
	
	public String getWohnort(){
		return wohnort;
	}
	
	public String getEmail(){
		return email;
	}

	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public Vector<Ware> getWarenkorb(){
		return this.warenkorb;
	}
	
	public void inWarenKorbLegen(Ware w, int menge){
		for(int i = 0; i < menge; i++)
			this.warenkorb.add(w);
	}
}
