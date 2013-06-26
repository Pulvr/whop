package bib.local.ui.cui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import bib.local.domain.LagerVerwaltung;
import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertNichtException;
import bib.local.domain.exceptions.WarenkorbLeerException;
import bib.local.valueobjects.Person;
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
	
	
	public LagerClientCUI(String datei) throws IOException {
		// die lag-Verwaltung erledigt die Aufgaben, 
		// die nichts mit Ein-/Ausgabe zu tun haben
		lag = new LagerVerwaltung(datei);

		// Stream-Objekt fuer Texteingabe ueber Konsolenfenster erzeugen
		in = new BufferedReader(new InputStreamReader(System.in));
		
	}
	
	// Eingeloggten User festlegen
	public Person User = new Person();
	
	// Ist jemand eingeloggt und ist es ein Mitarbeiter?
	public boolean eingeloggt = false;
	public boolean mitarbeiterAngemeldet = false;


	/* (non-Javadoc)
	 * 
	 * Interne (private) Methode zur Ausgabe des Men�s.
	 */
	private void gibMenueAus() {
		if (!eingeloggt) System.out.print("\nBefehle:\n \n  Einloggen: 'i'\n");
		else System.out.print("\nBefehle:\n \n  Ausloggen: 'u'\n");
		if (mitarbeiterAngemeldet) System.out.print("		   \n  Ware einfuegen: 'e'");
		System.out.print("	       \n  Waren sortieren : 't'");
		if (mitarbeiterAngemeldet) System.out.print("         \n  WarenBestand �ndern:  'c'");
		System.out.print("         \n  Waren ausgeben:  'a'");
		System.out.print("         \n  Person einfuegen: 'p'");
		if (mitarbeiterAngemeldet) System.out.print("         \n  Personen ausgeben:  'l'");
		System.out.print("         \n  Personen speichern:  'b'");
		System.out.print("         \n  Waren suchen:  'f'");
		System.out.print("         \n  Daten sichern:  's'");
		System.out.print("		   \n  Waren in den Korb legen 'j'");
		System.out.print("		   \n  Waren aus Korb entfernen 'z'");
		System.out.print("         \n  Warenkorb leeren 'h'");
		System.out.print("		   \n  Warenkorb anzeigen lassen 'o'");
		System.out.println("         \n\n  Beenden:        'q'\n");
		
		System.out.print("> "); // Prompt
		System.out.flush(); // ohne NL ausgeben
	}

	// Vergleicht eingegebenen Buchstaben mit Men�punkten und f�hrt den gew�nschten Befehl aus
	
	private void verarbeiteEingabe(String line) throws IOException {
		
		try{
			
			// EINLOGGEN:
			if (line.equals("i")){
				if(!eingeloggt) einloggen();
				else System.out.print("Sie sind bereits als " + User.getUsername() + " eingeloggt.");
			}
			// AUSLOGGEN:
			if (line.equals("u") && eingeloggt){
				if(nachfragen("dich ausloggen m�chtest")){
					User = null;
					System.out.print("Erfolgreich ausgeloggt.");
					eingeloggt = false;
					mitarbeiterAngemeldet = false;
				}
			}
			// WARE EINF�GEN:
			if (line.equals("e")) { 
				
				// Liest die Eigenschaften der neuen Ware nacheinander ein
				
				System.out.print("Warenbezeichnung  > ");
				String bezeichnung = liesEingabe();
				
				System.out.print("Warennummer > ");
					String nummer = liesEingabe();
					int bNr = Integer.parseInt(nummer);
				
				System.out.print("Bestand > ");
					String bstd = liesEingabe();
					int bestand = Integer.parseInt(bstd);
					
				System.out.print("Preis > ");
					String preisString = liesEingabe();
					float preis = Float.parseFloat(preisString);
	
				boolean ok = false;	
				try {
					lag.fuegeWareEin(bezeichnung, bNr, bestand, preis);
					ok = true;
					} catch (WareExistiertBereitsException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
		
				if (ok)
					System.out.println("Einf�gen ok");
				else
					System.out.println("Fehler beim Einf�gen");
			
				
			//WARENBESTAND �NDERN
			}else if(line.equals("c")){
				try {
					einloggenAbfrage();
					System.out.print("Gib die exakte Bezeichnung des Artikels ein, dessen Bestand ge�ndert werden soll >");
					String bezeichnung = liesEingabe();
					
					System.out.print("neuer Bestand > ");
					String bstd = liesEingabe();
					int neuerBestand = Integer.parseInt(bstd);
						
					lag.aendereBestand(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung), neuerBestand);
					
					System.out.println("Der Bestand f�r '"+lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBezeichnung()+ "' wurde ge�ndert");
				} catch (WareExistiertNichtException e) {
						// TODO Auto-generated catch block
						System.err.println(e.getMessage());
						e.printStackTrace();
				}
			
			//PERSON EINF�GEN	
			} else if (line.equals("p")) {
				System.out.print("Nummer > ");
				String nr = liesEingabe();
				int pNr = Integer.parseInt(nr);
				System.out.print("Name > ");
				String name = liesEingabe();
				System.out.print("Anrede > ");
				String anr = liesEingabe();
				System.out.print("Stra�e > ");
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
				boolean ok = false;
				try{
					lag.fuegePersonEin(pNr, name, anr, strasse, plz, ort, email, usr, pw, false);
					ok = true;
				}catch (PersonExistiertBereitsException e){
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
				if (ok)
					System.out.println("Einf�gen ok");
				else
					System.out.println("Fehler beim Einf�gen");
				
				
			}
			
			// WARENKORB AUSGEBEN LASSEN
			else if (line.equals("o")){
				einloggenAbfrage();
				if(!User.getWarenkorb().isEmpty()){
					System.out.println("Ihr Warenkorb beinhaltet: \n" + User.getWarenkorb());
				} else System.out.println("\nIhr Warenkorb enth�lt bislang noch keine Artikel.");
			}
			
			//GIB ALLE WAREN AUS
			else if (line.equals("a")) {
				List<Ware> listeW = lag.gibAlleWaren();
				gibWarenlisteAus(listeW);
			}
			
			//GIB ALLE PERSONEN AUS
			else if (line.equals("l") && mitarbeiterAngemeldet) {
				List<Person> listeP = lag.gibAllePersonen();
				gibPersonenAus(listeP);
			}
			
			//SORTIERE DIE WAREN
			else if (line.equals("t")){
				System.out.println("Nach was soll sortiert werden?");
				System.out.println("b = WarenBezeichnung");
				System.out.println("n = WarenNummer");
				System.out.println("e = WarenBestand");
				System.out.println("p = WarenPreis");
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
					//somit musste der float wert in einen int wert getypecastet werden dies f�hrt aber zu Ungenauigkeiten
				}else if(antwort.equals("p")){
					lag.sortiereDieWaren("p");
					List<Ware> listeW = lag.gibAlleWaren();
					gibWarenlisteAus(listeW);
					System.out.println("Waren wurden nach Preis sortiert");
				}
				
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
				
			//SPEICHERE PERSONEN
			}
			else if (line.equals("b")) {
				lag.schreibePersonen();
			}
			// IN KORB LEGEN 
			else if (line.equals("j")){
				if(!eingeloggt) einloggenAbfrage();
					System.out.println("\nGib die exakte Bezeichnung des Artikels ein, den du kaufen m�chtest > ");
					String bezeichnung = liesEingabe();
					System.out.println("\nZu bestellende Anzahl? > ");
					String mengenString = liesEingabe();
					int menge = Integer.parseInt(mengenString);
				
				if(lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
					if (lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() >= menge){
						try {
							lag.inWarenKorbLegen(menge, lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung), User);
							System.out.println("\nIhr Warenkorb beinhaltet:\n" + 
									User.getWarenkorb());
						} catch (BestellteMengeNegativException e) {
							// TODO Auto-generated catch block
							System.err.print(e.getMessage());
						}
					}else if(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() < menge){
					 System.err.println("Die angeforderte Menge �bersteigt den Bestand des von Ihnen gew�nschten Artikels.");
					}
				} else if(!lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
					System.err.println("Die Ware existiert nicht.");
				} 
			
			//ENTFERNEN 
			} else if(line.equals("z")){
				einloggenAbfrage();
				System.out.println("\nGib die exakte Bezeichnung des Artikels ein, den du aus dem Korb entfernen m�chtest > ");
				String bezeichnung = liesEingabe();
				System.out.println("\nZu entfernende Anzahl? > ");
				String mengenString = liesEingabe();
				int menge = Integer.parseInt(mengenString);
				
				if(lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
					if (lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() >= menge){
						try {
							lag.entferneAusWarenkorb(menge, lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung), User);
							System.out.println("\nIhr Warenkorb beinhaltet:\n" + 
									User.getWarenkorb());
						} catch (BestellteMengeNegativException e) {
							// TODO Auto-generated catch block
							System.err.print(e.getMessage());
						}
					}
				}else if(!lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
				 System.err.println("Die Ware existiert nicht.");
				} 
			//LEEREN 
			} else if(line.equals("h")){
				einloggenAbfrage();
				if (!User.getWarenkorb().isEmpty()){
					if (nachfragen("deinen Warenkorb leeren willst")){
						lag.warenkorbLeeren(User);
						System.out.println("Der Warenkorb wurde geleert.");
					}
				}
				else System.out.println("Ihr Warenkorb enth�lt bislang keinerlei Artikel.");
			}
		} catch (NumberFormatException e){
				System.err.println(e.getMessage());
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
				System.out.println(ware.warenListe());
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
		if(!eingeloggt){
			System.out.println("\nBitte loggen Sie sich zun�chst ein!\n");
			this.einloggen();
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
	
	private void einloggen() throws IOException{
		// Angemeldete User geben ihre Kundennummer und ihr Passwort an, um sich einzuloggen
		
		System.out.print("Bitte geben Sie ihre Kundennummer ein > \n");
		String knummer = this.liesEingabe();
		int nummer = Integer.parseInt(knummer);
		while(!lag.getMeinePersonenVerwaltung().getPersonenObjekte().containsKey(nummer)){
			System.out.print("Es existiert kein User mit dieser Nummer. Bitte versuchen Sie es erneut > \n");
			knummer = liesEingabe();
			nummer = Integer.parseInt(knummer);
		}
		System.out.print("\nBitte geben Sie nun Ihr entsprechendes Passwort ein > \n");
		String passwort = liesEingabe();
		while(!lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(nummer).getPassword().equals(passwort)){
			System.out.print("Das eingegebene Passwort war nicht korrekt. Bitte versuchen Sie es erneut > \n");
			passwort = liesEingabe();
		}
		this.eingeloggt = true;
		this.User = lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(nummer);
		if(this.User.getMitarbeiterberechtigung()) this.mitarbeiterAngemeldet = true;
		System.out.print("\nErfolgreich eingeloggt!");
		System.out.print("\nWilkommen, " + lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(nummer).getUsername() + "!\n");
	}
	
	private void enterZumFortfahren() throws IOException{
		System.out.println("\n		-> Zum Fortfahren bitte die Enter-Taste dr�cken.");
		String input = this.liesEingabe();
		System.out.println("\n\n");
		input = null;
	}


	/**
	 * Methode zur Ausf�hrung der Hauptschleife:
	 * - Men� ausgeben
	 * - Eingabe des Benutzers einlesen
	 * - Eingabe verarbeiten und Ergebnis ausgeben
	 * (EVA-Prinzip: Eingabe-Verarbeitung-Ausgabe)
	 */
	public void run() {
		// Variable f�r Eingaben von der Konsole
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
