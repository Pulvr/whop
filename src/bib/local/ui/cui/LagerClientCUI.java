package bib.local.ui.cui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import bib.local.domain.LagerVerwaltung;
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
		System.out.print("Befehle: \n  Ware einfuegen: 'e'");
	  //System.out.print("	       \n  Ware entfernen : 'l'");
		System.out.print("	       \n  Waren sortieren : 't'");
		System.out.print("         \n  Waren ausgeben:  'a'");
		System.out.print("         \n  Person einfuegen: 'p'");
		System.out.print("         \n  Personen ausgeben:  'l'");
		System.out.print("         \n  Personen speichern:  'b'");
		System.out.print("         \n  Waren suchen:  'f'");
		System.out.print("         \n  Daten sichern:  's'");
		System.out.println("         \n  Beenden:        'q'");
		System.out.print("> "); // Prompt
		System.out.flush(); // ohne NL ausgeben
	}

	/* (non-Javadoc)
	 * 
	 * Interne (private) Methode zum Einlesen von Benutzereingaben.
	 */
	private String liesEingabe() throws IOException {
		// einlesen von Konsole
		return in.readLine();
	}

	/* (non-Javadoc)
	 * 
	 * Interne (private) Methode zur Verarbeitung von Eingaben
	 * und Ausgabe von Ergebnissen.
	 */
	private void verarbeiteEingabe(String line) throws IOException {
		
		// Eingabe bearbeiten:
		if (line.equals("e")) { 
			// lese die notwendigen Parameter, einzeln pro Zeile
			System.out.print("Warennummer > ");
			String nummer = liesEingabe();
			int bNr = Integer.parseInt(nummer);
			System.out.print("Warenbezeichnung  > ");
			String bezeichnung = liesEingabe();
			System.out.print("Bestand > ");
			String bstd = liesEingabe();
			int bestand = Integer.parseInt(bstd);

			boolean ok = false;
			try {
				lag.fuegeWareEin(bezeichnung, bNr, bestand);
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
			}else if(antwort.equals("n")){
				lag.sortiereDieWaren("n");	
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (!input.equals("q"));
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
