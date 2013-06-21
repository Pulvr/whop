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
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Ware;

/**
 * Klasse für sehr einfache Benutzungsschnittstelle für eina
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

	/* (non-Javadoc)
	 * 
	 * Interne (private) Methode zur Ausgabe des Menüs.
	 */
	private void gibMenueAus() {
		System.out.print("\nBefehle: \n  Ware einfuegen: 'e'");
	  //System.out.print("	       \n  Ware entfernen : 'l'");
		System.out.print("	       \n  Waren sortieren : 't'");
		System.out.print("         \n  Waren ausgeben:  'a'");
		System.out.print("         \n  Person einfuegen: 'p'");
		System.out.print("         \n  Personen ausgeben:  'l'");
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

	
	
	// Vergleicht eingegebenen Buchstaben mit Menüpunkten und führt den gewünschten Befehl aus
	
	private void verarbeiteEingabe(String line) throws IOException {
		
		
		
		
		// WARE EINFÜGEN:
		try{
			if (line.equals("e")) { 
				
				// Liest die Eigenschaften der neuen Ware nacheinander ein
				
				System.out.print("Warennummer > ");
					String nummer = liesEingabe();
				int bNr = Integer.parseInt(nummer);
				
				System.out.print("Warenbezeichnung  > ");
					String bezeichnung = liesEingabe();
					
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
					System.out.println("Einfügen ok");
				else
					System.out.println("Fehler beim Einfügen");
			} else if (line.equals("p")) {
				System.out.print("Nummer > ");
				String nr = liesEingabe();
				int pNr = Integer.parseInt(nr);
				System.out.print("Name > ");
				String name = liesEingabe();
				System.out.print("Anrede > ");
				String anr = liesEingabe();
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
				boolean ok = false;
				try{
					lag.fuegePersonEin(pNr, name, anr, strasse, plz, ort, email, usr, pw);
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
			else if (line.equals("o")){
				System.out.println("\nGib deine Kundennummer an.");
				String knummerString = liesEingabe();
				int knummer = Integer.parseInt(knummerString);
				Vector<Ware> warenkorb = lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(knummer).getWarenkorb();
				lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(knummer).warenkorbAusgeben(warenkorb);
			}
			else if (line.equals("a")) {
				List<Ware> listeW = lag.gibAlleWaren();
				gibWarenlisteAus(listeW);
	
			}
			else if (line.equals("l")) {
				List<Person> listeP = lag.gibAllePersonen();
				gibPersonenAus(listeP);
			}
			else if (line.equals("t")){
				System.out.println("Nach was soll sortiert werden?");
				System.out.println("b = WarenBezeichnung");
				System.out.println("n = WarenNummer");
				String antwort = liesEingabe();
				if(antwort.equals("b")){
					lag.sortiereDieWaren("b");
					System.out.println("Waren wurden nach Bezeichnung sortiert");
				}else if(antwort.equals("n")){
					lag.sortiereDieWaren("n");
					System.out.println("Waren wurden nach Nummer sortiert");
				}
				
			}
			else if (line.equals("f")) {
				System.out.print("Warenbezeichnung  > ");
				String bezeichnung = liesEingabe();
				List<Ware> liste = lag.sucheNachBezeichnung(bezeichnung);
				gibWarenlisteAus(liste);
				
			}
			else if (line.equals("s")) {
				lag.schreibeWaren();
			}
			else if (line.equals("b")) {
				lag.schreibePersonen();
			}
			// INKORBLEGEN INKORBLEGEN INKORBLEGEN
			else if (line.equals("j")){
				
					System.out.println("\nGib die exakte Bezeichnung des Artikels ein, den du kaufen möchtest.");
					String bezeichnung = liesEingabe();
					System.out.println("\nZu bestellende Anzahl?");
					String mengenString = liesEingabe();
					int menge = Integer.parseInt(mengenString);
					System.out.println("\nGib deine Kundennummer an.");
					String knummerString = liesEingabe();
					int knummer = Integer.parseInt(knummerString);
				
				if(lag.getMeinePersonenVerwaltung().getPersonenObjekte().containsKey(knummer) && 
						lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
					if (lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() >= menge){
						try {
							lag.inWarenKorbLegen(menge, lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung), 
									lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(knummer));
							//lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).setBestand(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() - menge);
							System.out.println("Ihr Warenkorb beinhaltet:\n" + 
									lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(knummer).getWarenkorb());
						} catch (BestellteMengeNegativException e) {
							// TODO Auto-generated catch block
							System.err.print(e.getMessage());
						}
					}
				} else if(!lag.getMeinePersonenVerwaltung().getPersonenObjekte().containsKey(knummer)) {
					System.err.println("Die Person existiert nicht.");
				} else if(!lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
					System.err.println("Die Ware existiert nicht.");
				} else if(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() < menge){
					System.err.println("Die angeforderte Menge übersteigt den Bestand des von Ihnen gewünschten Artikels.");
				}
			
			//ENTFERNEN ENTFERNEN ENTFERNEN
			} else if(line.equals("z")){
				
				System.out.println("\nGib die exakte Bezeichnung des Artikels ein, den du aus dem Korb entfernen möchtest.");
				String bezeichnung = liesEingabe();
				System.out.println("\nZu entfernende Anzahl?");
				String mengenString = liesEingabe();
				int menge = Integer.parseInt(mengenString);
				System.out.println("\nGib deine Kundennummer an.");
				String knummerString = liesEingabe();
				int knummer = Integer.parseInt(knummerString);
				
				if(lag.getMeinePersonenVerwaltung().getPersonenObjekte().containsKey(knummer) && 
						lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
					if (lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() >= menge){
						try {
							lag.entferneAusWarenkorb(menge, lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung), 
									lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(knummer));
							//lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).setBestand(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() - menge);
							System.out.println("Ihr Warenkorb beinhaltet:\n" + 
									lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(knummer).getWarenkorb());
						} catch (BestellteMengeNegativException e) {
							// TODO Auto-generated catch block
							System.err.print(e.getMessage());
						}
					}
				} else if(!lag.getMeinePersonenVerwaltung().getPersonenObjekte().containsKey(knummer)) {
					System.err.println("Die Person existiert nicht.");
				} else if(!lag.getMeineWarenVerwaltung().getWarenObjekte().containsKey(bezeichnung)){
					System.err.println("Die Ware existiert nicht.");
				} else if(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung).getBestand() < menge){
					System.err.println("Die angeforderte Menge übersteigt den Bestand des von Ihnen gewünschten Artikels.");
				}
			//LEEREN LEEREN LEEREN	
			} else if(line.equals("h")){
				System.out.println("Der Korb wessen users soll geleert werden?");
				String knummernString = liesEingabe();
				int knummer = Integer.parseInt(knummernString);
				lag.warenkorbLeeren(lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(knummer));
				System.out.println("Der Warenkorb wurde geleert");
			}
		} catch (NumberFormatException e){
				System.err.println(e.getMessage());
		}
		
	/*	else if (line.equals("l")){
			System.out.print("Warennummer > ");
			String nummer = liesEingabe();
			int Nr = Integer.parseInt(nummer);
			System.out.print("Warenbezeichnung  > ");
			String bezeichnung = liesEingabe();
			System.out.print("Bestand > ");
			String bstd = liesEingabe();
			int bestand = Integer.parseInt(bstd);
			List<Ware> liste = lag.sucheNachBezeichnung(bezeichnung);
			
			if(!liste.isEmpty()){
				System.out.print(bezeichnung +" mit der Nummer"+ Nr +" wurde gefunden , löschen? (y/n)");
				String answ = liesEingabe();
				if (answ.equals("y")){
					lag.entferneWare(bezeichnung, Nr, bestand);
					System.out.println("gelöscht");
					
					
				}else {
					System.out.println("dann nicht...");
						
				}
			}
			if(liste.isEmpty())
		     System.out.println(bezeichnung + " wurde nicht gefunden");
		}*/
		
	}
	/*
	private void login (){
		lag.getMeinePersonenVerwaltung().getPersonenObjekte().containsKey(key)
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
