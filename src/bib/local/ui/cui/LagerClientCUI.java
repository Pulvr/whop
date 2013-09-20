package bib.local.ui.cui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import bib.local.domain.LagerVerwaltung;
import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.PersonExistiertNichtException;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertNichtException;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Rechnung;
import bib.local.valueobjects.Ware;

/**
 * Klasse f�r sehr einfache Benutzungsschnittstelle f�r eina
 * Lager. Die Benutzungsschnittstelle basiert auf Ein-
 * und Ausgaben auf der Kommandozeile, daher der Name CUI
 * (Command line User Interface).
 * 
 */
public class LagerClientCUI {

	private LagerVerwaltung lag;
	private BufferedReader in;
	
	// Eingeloggten User festlegen
	private Person user = new Person();
		
	// Ist jemand eingeloggt und ist es ein Mitarbeiter?
	private boolean eingelogged = false;
	private boolean mitarbeiterAngemeldet = false;
	
	public LagerClientCUI(String datei) throws IOException {
		// die lag-Verwaltung erledigt die Aufgaben, 
		// die nichts mit Ein-/Ausgabe zu tun haben
		lag = new LagerVerwaltung(datei);

		// Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
		in = new BufferedReader(new InputStreamReader(System.in));
		
	}
	
	/* (non-Javadoc)
	 * 
	 * Interne (private) Methode zur Ausgabe des Men�s.
	 */
	private void gibMenueAus() {
		if (!eingelogged) System.out.print("\nBefehle:\n \n  Einloggen: 'i'\n");
		else System.out.print("\nBefehle:\n \n  Ausloggen: 'u'\n");
		
		//wird nur ausgegeben wenn ein Mitarbeiter angemeldet ist, 
		//man kann trotzdem drauf zugreifen nur wird man dann gebeten sich einzuloggen
		if (mitarbeiterAngemeldet){
			System.out.print("         \n  Person einfuegen: 'p'");
			System.out.print("		   \n  Person l�schen: 'x'");
			System.out.print("         \n  Personen ausgeben:  'l'");
			System.out.print("         \n  Personen speichern:  'b'");

			System.out.print("		   \n  Ware EINFUEGEN: 'e'");
			System.out.print("		   \n  Ware L�SCHEN: 'y'");
		}
		System.out.print("	       \n  Waren SORTIEREN : 't'");
		if (mitarbeiterAngemeldet) System.out.print("         \n  WarenBestand �ndern:  'c'");
		System.out.print("         \n  Waren AUSGEBEN:  'a'");
		System.out.print("         \n  Waren SUCHEN:  'f'");
		if (mitarbeiterAngemeldet) System.out.print("         \n  Waren SICHERN:  's'");
		System.out.print("		   \n  Waren in den Korb LEGEN: 'j'");
		System.out.print("		   \n  Waren aus Korb ENTFERNEN: 'z'");
		System.out.print("         \n  Warenkorb LEEREN 'h'");
		System.out.print("		   \n  Warenkorb ANZEIGEN 'o'");
		System.out.print("		   \n  Waren KAUFEN 'k'");
		if (mitarbeiterAngemeldet) System.out.print("		   \n  WarenLog ausgeben 'd'");
		System.out.println("         \n\n  Beenden:        'q'\n");
		
		System.out.print("> "); // Prompt
		System.out.flush(); // ohne NL ausgeben
	}

	/**
	 * Die Eingabe wird verarbeitet
	 * @param line die eingabe
	 * @throws IOException
	 */
	
	private void verarbeiteEingabe(String line) throws IOException {
		
		//Die ganze eingabe ist mit einem try catch -NumberFormatException umgeben
		try{
			
			// EINLOGGEN:
			if (line.equals("i")){
				if(!eingelogged) {
					System.out.print("Bitte geben Sie ihren UserNamen ein > \n");
					String userName = this.liesEingabe();
					
					while(!lag.getMeinePersonenVerwaltung().getPersonenObjekte().containsKey(userName)){
						System.out.print("Es existiert kein User mit diesem Namen. Bitte versuchen Sie es erneut > \n");
						userName = liesEingabe();
					}
					System.out.print("\nBitte geben Sie nun Ihr entsprechendes Passwort ein > \n");
					String passwort = liesEingabe();
					while(!lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName).getPassword().equals(passwort)){
						System.out.print("Das eingegebene Passwort war nicht korrekt. Bitte versuchen Sie es erneut > \n");
						passwort = liesEingabe();
					}
					
					einloggen(userName);
				}
				else System.out.print("Sie sind bereits als " + user.getUsername() + " eingeloggt.");
			}
			// AUSLOGGEN:
			if (line.equals("u") && eingelogged){
				if(nachfragen("dich ausloggen m�chtest")){
					user = null;
					System.out.print("Erfolgreich ausgeloggt.");
					eingelogged = false;
					mitarbeiterAngemeldet = false;
				}
			}
			// WARE EINFÜGEN:
			if (line.equals("e")) { 
				
				// Liest die Eigenschaften der neuen Ware nacheinander ein
				einloggenAbfrage();
				System.out.print("Warenbezeichnung  > ");
				String bezeichnung = liesEingabe();
				//WARENNUMMER DARF NICHT DOPPELT SEIN

				System.out.print("Warennummer > ");
				String nummernString = liesEingabe();
				int nummer = Integer.parseInt(nummernString);
				
				while (nummer<0){
					System.out.println("Die WarenNummer darf nicht negativ sein!");
					nummernString = liesEingabe();
					nummer = Integer.parseInt(nummernString);
				}
				
				System.out.print("Bestand > ");
				String bestandsString = liesEingabe();
				int bestand = Integer.parseInt(bestandsString);
				while (bestand<0){
					System.out.println("Der Bestand darf nicht negativ sein!");
					bestandsString = liesEingabe();
					bestand = Integer.parseInt(bestandsString);
				}
					
				System.out.print("Preis > ");
				String preisString = liesEingabe();
				float preis = Float.parseFloat(preisString);
				while (preis<0){
					System.out.println("Der Preis darf nicht negativ sein!");
					preisString = liesEingabe();
					preis = Float.parseFloat(preisString);
				}
	
				boolean ok = false;	
				try {
					lag.fuegeWareEin(bezeichnung, nummer, bestand, preis);
					ok = true;
				} catch (WareExistiertBereitsException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
		
				if (ok)
					System.out.println("Einfügen ok");
				else
					System.out.println("Fehler beim Einfügen");
			
				
			//WARE AUS DEM BESTAND LÖSCHEN
			}else if (line.equals("y")){
				try {
					einloggenAbfrage();
					System.out.print("Gib die exakte Bezeichnung der Ware ein, die gelöscht werden soll >");
					String bezeichnung = liesEingabe();
					
					lag.entferneWare(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung));
					System.out.println("Der Eintrag im Warenbestand wurde gelöscht");
				} catch (WareExistiertNichtException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
		
			//WARENBESTAND ÄNDERN
			}else if(line.equals("c")){
				
				try {
					einloggenAbfrage();
					System.out.print("Gib die exakte Bezeichnung der Ware ein, dessen Bestand geändert werden soll >");
					String bezeichnung = liesEingabe();
					
					System.out.print("neuer Bestand > ");
					String bstd = liesEingabe();
					int neuerBestand = Integer.parseInt(bstd);
						
					lag.aendereBestand(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung), neuerBestand);
					
					System.out.println("Der Bestand für '"+lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBezeichnung()+ "' wurde ge�ndert");
				} catch (WareExistiertNichtException e) {
						// TODO Auto-generated catch block
						System.err.println(e.getMessage());
						e.printStackTrace();
				}
			
			//PERSON EINF�GEN	
			} else if (line.equals("p")) {
				einloggenAbfrage();
				System.out.print("Nummer > ");
				String nummernString = liesEingabe();
				int personenNummer = Integer.parseInt(nummernString);
				while (personenNummer<0){
					System.out.println("Die PersonenNummer darf nicht negativ sein!");
					nummernString = liesEingabe();
					personenNummer = Integer.parseInt(nummernString);
				}
				System.out.print("Anrede > ");
				String anr = liesEingabe();
				System.out.print("ganzer Name > ");
				String name = liesEingabe();
				System.out.print("Straße > ");
				String strasse = liesEingabe();
				System.out.print("PLZ > ");
				String plz = liesEingabe();
				System.out.print("Ort > ");
				String ort = liesEingabe();
				System.out.print("E-Mail > ");
				String email = liesEingabe();
				System.out.print("Username > ");
				String usr = liesEingabe();
				System.out.print("Passwort > ");
				String pw = liesEingabe();
				System.out.print("Ist diese Person ein Mitarbeiter ? (j/n) > ");
				String ma = liesEingabe();
				boolean ok = false;
				
				while(!ma.equals("j")&&!ma.equals("n")){
						System.out.println("Bitte geben sie 'j' oder 'n' an > ");
						ma = liesEingabe();
					}
				if(ma.equals("j")){
					try{
						lag.fuegePersonEin(personenNummer, name, anr, strasse, plz, ort, email, usr, pw, true);
						ok = true;
					}catch (PersonExistiertBereitsException e){
						System.err.println(e.getMessage());
						e.printStackTrace();
					}
					if (ok)
						System.out.println("Einfügen ok");
					else
						System.out.println("Fehler beim Einfügen");
				
				}else if(ma.equals("n")){
					try{
						lag.fuegePersonEin(personenNummer, name, anr, strasse, plz, ort, email, usr, pw, false);
						ok = true;
					}catch (PersonExistiertBereitsException e){
						System.err.println(e.getMessage());
						e.printStackTrace();
					}
					if (ok)
						System.out.println("Einfügen ok");
					else
						System.out.println("Fehler beim Einfügen");
				}
			
			//PERSON LÖSCHEN
			}else if (line.equals("x")){
				try {
					einloggenAbfrage();
					System.out.print("Gib den UserNamen der Person ein, die gelöscht werden soll >");
					String userName = liesEingabe();
					
					lag.personEntfernen(lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName));
					System.out.println("Die Person wurde entfernt");
				}catch(PersonExistiertNichtException e){
					System.err.println(e.getMessage());
				}
			}
			
			// WARENKORB AUSGEBEN LASSEN
			else if (line.equals("o")){
				einloggenAbfrage();
				if(!user.getWarenkorb().isEmpty()){
					System.out.println("Ihr Warenkorb beinhaltet: \n" + user.getWarenkorb());
				} else System.err.println("\nIhr Warenkorb enthält bislang noch keine Artikel.");
			}
			
			//GIB ALLE WAREN AUS
			else if (line.equals("a")) {
				List<Ware> listeW = lag.gibAlleWaren();
				gibWarenlisteAus(listeW);
			}
			
			//GIB ALLE PERSONEN AUS
			else if (line.equals("l") && mitarbeiterAngemeldet) {
				einloggenAbfrage();
				List<Person> listeP = lag.gibAllePersonen();
				gibPersonenAus(listeP);
			}
			
			//SORTIERE DIE WAREN
			else if (line.equals("t")){
				einloggenAbfrage();
				System.out.println("Nach was soll sortiert werden?");
				System.out.println("b = WarenBezeichnung");
				System.out.println("n = WarenNummer");
				System.out.println("e = WarenBestand");
//				System.out.println("p = WarenPreis");
				String antwort = liesEingabe();
				if(antwort.equals("b")){
					lag.sortiereDieWaren("b");
					List<Ware> listeW = lag.gibAlleWaren();
					gibWarenlisteAus(listeW);
					System.out.println("Waren wurden nach Bezeichnung sortiert");
				}else if(antwort.equals("n")){
					lag.sortiereDieWaren("n");
					List<Ware> listeW = lag.gibAlleWaren();
					gibWarenlisteAus(listeW);
					System.out.println("Waren wurden nach Nummer sortiert");
				}else if(antwort.equals("e")){
					lag.sortiereDieWaren("e");
					List<Ware> listeW = lag.gibAlleWaren();
					gibWarenlisteAus(listeW);
					System.out.println("Waren wurden nach Bestand sortiert");
					//Sortieren nach Preis funktioniert noch nicht ganz, da compare() eine int merhode ist und nicht mit float werten umgehen kann
					//somit musste der float wert in einen int wert getypecastet werden dies fährt aber zu Ungenauigkeiten
				} /* else if(antwort.equals("p")){
					lag.sortiereDieWaren("p");
					List<Ware> listeW = lag.gibAlleWaren();
					gibWarenlisteAus(listeW);
					System.out.println("Waren wurden nach Preis sortiert");
				}*/
				
			//SUCHE NACH WAREN
			}
			else if (line.equals("f")) {
				System.out.print("Warenbezeichnung  > ");
				String bezeichnung = liesEingabe();
				List<Ware> liste = lag.sucheNachBezeichnung(bezeichnung);
				gibWarenlisteAus(liste);
				
			//SPEICHERE WAREN
			}
			else if (line.equals("s")) {
				lag.schreibeWaren();
				System.out.println("Erfolgreich gesichert");
			//SPEICHERE PERSONEN
			}
			else if (line.equals("b")) {
				lag.schreibePersonen();
				System.out.println("Erfolgreich gesichert");
			}
			//IN KORB LEGEN 
			else if (line.equals("j")){
				einloggenAbfrage();
				System.out.println("\nGib die exakte Bezeichnung der Ware ein, die in den Korb soll > ");
				String bezeichnung = liesEingabe();
				System.out.println("\nZu bestellende Anzahl? > ");
				String mengenString = liesEingabe();
				int menge = Integer.parseInt(mengenString);
				
			if(lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
				if (lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() >= menge){
					try {
						lag.inWarenKorbLegen(menge, lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung), user);
						System.out.println("\nIhr Warenkorb beinhaltet:\n" + user.getWarenkorb());
					} catch (BestellteMengeNegativException e) {
						// TODO Auto-generated catch block
						System.err.print(e.getMessage());
					}
				}else if(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() < menge){
				 System.err.println("Die angeforderte Menge übersteigt den Bestand des von Ihnen gewünschten Artikels.");
				}
			} else if(!lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
				System.err.println("Die Ware existiert nicht.");
			} 
			
			//ENTFERNEN 
			} else if(line.equals("z")){
				einloggenAbfrage();
				System.out.println("\nGib die exakte Bezeichnung der Ware ein, die du aus dem Korb entfernen möchtest > ");
				String bezeichnung = liesEingabe();
				System.out.println("\nZu entfernende Anzahl? > ");
				String mengenString = liesEingabe();
				int menge = Integer.parseInt(mengenString);
				
				if(lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
						try {
							lag.entferneAusWarenkorb(menge, lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung), user);
							if(user.getWarenkorb().isEmpty()){
								System.out.println("Ihr Warenkorb ist leer");
							}else System.out.println("\nIhr Warenkorb beinhaltet:\n" + user.getWarenkorb());
						} catch (BestellteMengeNegativException e) {
							System.err.print(e.getMessage());
						}
				}else if(!lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
				 System.err.println("Die Ware existiert nicht.");
				}
			//WARENKORB KAUFEN	
			}else if(line.equals("k")){
				if(user.getWarenkorb().isEmpty()){
					System.err.println("\nIhr Warenkorb enthält bislang noch keine Artikel.");
				}else {
					lag.setRechnung(new Rechnung(user));
					System.out.println(lag.getRechnung().toString());
					lag.warenkorbKaufen(user, user.getWarenkorb());
				}
			//WARENLOG AUSGEBEN
			}else if (line.equals("d")){
				
				try{
					einloggenAbfrage();
					System.out.print("Warenbezeichnung  > ");
					String bezeichnung = liesEingabe();
					
					System.out.print("Wie viele Tage soll der Log zurueckliegen? > ");
						String daysInPastString = liesEingabe();
						int daysInPast = Integer.parseInt(daysInPastString);
						
					for(int i = 0; i < lag.getWarenLog(bezeichnung, daysInPast).size(); i++){
						System.out.println(lag.getWarenLog(bezeichnung, daysInPast).elementAt(i));
					}
				} catch(IOException | ParseException e){
					e.printStackTrace();
				}
				
			//LEEREN
			}else if(line.equals("h")){
				einloggenAbfrage();
				if (!user.getWarenkorb().isEmpty()){
					if (nachfragen("deinen Warenkorb leeren willst")){
						lag.warenkorbLeeren(user);
						System.out.println("Der Warenkorb wurde geleert.");
					}
				}else System.out.println("Ihr Warenkorb enthält keine Artikel.");
			//Automatisches Speichern vorm schließen
			}else if (line.equals("q")){
				System.out.println("wollen sie vor dem beenden speichern (j/n)?");
				String yesOrNo = liesEingabe();
				while(!yesOrNo.equals("j")&&!yesOrNo.equals("n")){
						System.out.println("Bitte geben sie 'j' oder 'n' an > ");
						yesOrNo = liesEingabe();
					}
				if(yesOrNo.equals("j")){
					lag.schreibePersonen();
					lag.schreibeWaren();
					System.out.println("Erfolgreich gespeichert, bis zum nächsten mal!");
				}else{
					System.out.println("Erfolgreich beendet ohne zu speichern");
				}
			}
		}catch (NumberFormatException e){
			System.err.println(e.getMessage());
			e.getStackTrace();
		}	
	}
	
	/* (non-Javadoc)
	 * 
	 * Interne (private) Methode zum Ausgeben von Warenlisten.
	 *
	 */
	private void gibWarenlisteAus(List<Ware> waren) {
		if (waren.isEmpty()) {
			System.out.println("Liste ist leer.");
		} else {
			Iterator<Ware> iter = waren.iterator();
			while (iter.hasNext()) {
				Ware ware = iter.next();
				System.out.println(ware.toString());
			}
		}
	}
	/* (non-Javadoc)
	 * 
	 * Interne (private) Methode zum Ausgeben aller angemeldeter Personen.
	 *
	 */
	private void gibPersonenAus(List<Person> personen) {
		if (personen.isEmpty()) {
			System.out.println("Liste ist leer.");
		} else {
			Iterator<Person> iter = personen.iterator();
			while (iter.hasNext()) {
				Person person = iter.next();
				System.out.println(person.toString());
			}
		}
	}
	
	private void einloggenAbfrage() throws IOException{
		if(!eingelogged){
			System.out.println("\nBitte loggen Sie sich zunächst ein!\n");
			System.out.println("Geben sie ihren UserNamen an > \n");
			String userName = this.liesEingabe();
			
			while(!lag.getMeinePersonenVerwaltung().getPersonenObjekte().containsKey(userName)){
				System.out.print("Es existiert kein User mit diesem Namen. Bitte versuchen Sie es erneut > \n");
				userName = liesEingabe();
			}
			System.out.print("\nBitte geben Sie nun Ihr entsprechendes Passwort ein > \n");
			String passwort = liesEingabe();
			while(!lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName).getPassword().equals(passwort)){
				System.out.print("Das eingegebene Passwort war nicht korrekt. Bitte versuchen Sie es erneut > \n");
				passwort = liesEingabe();
			}
			
			einloggen(userName);
		}
	}
	
	private boolean nachfragen(String zusatz) throws IOException{
		System.out.print("Bist du sicher, dass du " + zusatz + "?");
		System.out.print("         \n  ja: 'j'");
		System.out.println("         \n  nein:  'n'");
		System.out.print(" > ");
		String antwort = liesEingabe();
		if(antwort.equals("j")) return true;
		else return false;
	}
	
	private void einloggen(String userName){
		
		this.eingelogged = true;
		this.user = lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName);
		if(this.user.getMitarbeiterberechtigung()) this.mitarbeiterAngemeldet = true;
		System.out.print("\nErfolgreich eingeloggt!");
		System.out.print("\nWilkommen, " + lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName).getUsername() + "!\n");
	}
	
//	public boolean getEingelogged(){
//		return this.eingelogged;
//	}
//	
//	public void setEingelogged(boolean log){
//		this.eingelogged=log;
//	}
//	
//	public Person getPerson(){
//		return this.user;
//	}
//	
//	public void setUser (Person p){
//		this.user = p;
//	}
	
	private void enterZumFortfahren() throws IOException{
		System.out.println("\n		-> Zum Fortfahren bitte die Enter-Taste drücken.");
		String input = this.liesEingabe();
		System.out.println("\n\n");
		input = null;
	}


	/**
	 * Methode zur Ausführung der Hauptschleife:
	 * - Menü ausgeben
	 * - Eingabe des Benutzers einlesen
	 * - Eingabe verarbeiten und Ergebnis ausgeben
	 * (EVA-Prinzip: Eingabe-Verarbeitung-Ausgabe)
	 */
	public void run() {
		// Variable für Eingaben von der Konsole
		String input = ""; 
	
		// Hauptschleife der Benutzungsschnittstelle
		do {
			gibMenueAus();
			try {
				input = liesEingabe();
				verarbeiteEingabe(input);
				enterZumFortfahren();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!input.equals("q"));
	}
	
	// Liest Eingaben des Nutzers ein
	
		private String liesEingabe() throws IOException {
			// einlesen von Konsole
			return in.readLine();
		}
	
	/**
	 * Die main-Methode...
	 */
	public static void main(String[] args) {
		LagerClientCUI cui;
		try {
			cui = new LagerClientCUI("LAG");
			cui.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
