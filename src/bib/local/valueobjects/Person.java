/**
 * Personenklasse , Superklasse von Kunde und Mitarbeiter
 */

package bib.local.valueobjects;

import java.io.Serializable;
import java.util.Vector;

public class Person implements Serializable{
	/**
	 * 
	 */
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
	
	/*public void warenkorbAusgeben(Vector<Ware> warenkorb){
		if(!warenkorb.isEmpty()){
			int anzahl = 1;
			int i = 0;
			Ware ware1 = warenkorb.elementAt(i);
			warenkorb.remove(i);
			if(warenkorb.contains(ware1))
			
		}
	}
	
	
	/*public void schreibeWarenkorb(Vector<Ware> warenkorb) {
		// PersistenzManager für Schreibvorgänge öffnen
		if (!warenkorb.isEmpty()) {
			int anzahl = 1;
			Iterator<Ware> iter = warenkorb.iterator();
			while (iter.hasNext()) {
				if(iter.equals(iter.next())){
					anzahl++;
				} else {
					System.out.println("<-- Artikel: " + iter.next().getBezeichnung() + " - Menge: " + anzahl + " -->");
					anzahl = 1;
				}
			}
		}			
		
		// Persistenz-Schnittstelle wieder schließen
		//pm.close();
	}
	
	/*public void warenkorbAusgeben(Vector<Ware> w){
		int i = 0;
		int anzahl = 1;
		//int u = 0;
		while(w.elementAt(i).equals(w.elementAt(i+1)) && (i+1) <= w.capacity()){
			anzahl++;
			i++;
			//u++;
		}
		System.out.println("<-- Artikel: " + w.elementAt(i).getBezeichnung() + " - Menge: " + anzahl + " -->");
		anzahl = 1;
		i++;
		//u++;
	}*/
	
	/**
	 * Methode zum kaufen der Waren
	 * @param warenkorb
	 */
	public void warenkorbKaufen(Vector<Ware> warenkorb){
		for(int i = 0; i < warenkorb.size(); i++){
			warenkorb.elementAt(i).setBestand(warenkorb.elementAt(i).getBestand()-1);
		}
		this.warenkorbLeeren();
	}
	
	/**
	 * methode zum hinzufügen von Waren in den Warenkorb
	 * @param w
	 * @param menge
	 */
	public void inWarenKorbLegen(Ware w, int menge){
		for(int i = 0; i < menge; i++)
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
	 * Methode zum kompletten leeren des Warenkorbs
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
	
	public boolean getMitarbeiterberechtigung(){
		return this.mitarbeiter;
	}
	
}