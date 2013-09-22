

package bib.local.valueobjects;

import java.io.Serializable;
import java.util.Vector;

/**
 * Personenklasse zum darstellen von Personen, manche mit Mitarbeiterberechtigung
 * Jede Person hat einen eigenen Warenkorb um einzukaufen
 *
 * @author Florian
 *
 */
public class Person implements Serializable{
	
	private static final long serialVersionUID = -4136988681207212891L;
	private int nummer;
	private String anrede="";
	private String name="";
	private String strasse = "";
	private String plz = "";
	private String wohnort = "";
	private String email = "";
	private String username = "";
	private String password = "";
	private boolean mitarbeiter;
	private Vector<Ware> warenkorb = new Vector<Ware>();
	
	//Default Konstruktor
	public Person (){
		
	}
	
	//Konstruktor
	public Person(int nr, String name, String anr, String strasse, String plz, String ort ,String email, String usr, String pw, boolean ma){
		this.nummer = nr;
		this.name = name;
		this.anrede = anr;
		this.strasse = strasse;
		this.plz = plz;
		this.wohnort = ort;
		this.email = email;
		this.username = usr;
		this.password = pw;
		this.mitarbeiter = ma;
	}
	
	
	/**
	 * Standard-Methode von Object überschrieben.
	 * Methode dient Vergleich von zwei Personen-Objekten anhand ihrer Werte,
	 * d.h. nummer und name.
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
	 * dient der einfachereren Ausgabe der attribute der Person
	 * Natürlich sollte im normalfall das Passwort nicht mit ausgegeben werden, da
	 * wir aber sonst den wert nicht erkennen können haben wir ihn uns mit ausgeben lassen
	 */
	public String toString() {
		return ("User: " + username + " Nummer : "+nummer +" Passwort : " +password+"\n"
					+ anrede + " " + name + " / E-Mail: " + email + "\nAdresse:\n" + strasse + "\t" + plz + "\t" + wohnort + "\nMitarbeiter : "+mitarbeiter+"\n\n");
	}
	
	/**
	 * Methode zum kaufen der Waren
	 * @param warenkorb
	 */
	public void warenkorbKaufen(Vector<Ware> warenkorb){
		for (int i = 0; i < warenkorb.size(); i++) {
		    int einheiten = 1;
		    if (warenkorb.elementAt(i) instanceof MassengutWare) {
		        MassengutWare mw = (MassengutWare) warenkorb.elementAt(i);
		        einheiten = mw.getPackungsGroesse();
		    }
			warenkorb.elementAt(i).setBestand(warenkorb.elementAt(i).getBestand() - einheiten);
		}
		this.warenkorbLeeren();
	}
	
	/**
	 * methode zum hinzufügen von Waren in den Warenkorb
	 * @param w
	 * @param menge
	 */
	public void inWarenKorbLegen(Ware w, int menge) {
	    int einheiten = 1;
	    if (w instanceof MassengutWare) {
	        MassengutWare mw = (MassengutWare) w;
	        einheiten = mw.getPackungsGroesse();
	    }
	    // Sollte die Menge kein vielfaches der Packungsgröße sein,
	    // wird Math.floor() verwendet um sicherzustellen,
	    // dass dem Kunden nicht eine Packung zu viel aufgebucht wird.
        for(int i = 0; i < Math.floor(menge / einheiten); i++)
			this.warenkorb.add(w);
	}
	
	/**
	 * Methode zum entfernen einzelner odere mehrer Waren 
	 * @param w
	 * @param menge
	 */
	public void entferneAusWarenkorb(Ware w, int menge){
		for(int i = 0; i < menge; i++)
			this.warenkorb.remove(w);
	}
	/**
	 * Methode zum kompletten leeren des Warenkorbs der Person
	 */
	public void warenkorbLeeren(){
		this.warenkorb.removeAllElements();
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
	
	public void setMitarbeiterberechtigung(boolean recht){
		this.mitarbeiter=recht;
	}
	
	public boolean getMitarbeiterberechtigung(){
		return this.mitarbeiter;
	}
	
}