
package bib.local.ui.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import bib.local.domain.LagerVerwaltung;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.ui.cui.LagerClientCUI;
import bib.local.valueobjects.Ware;

/**
 * Klasse für sehr einfache graphische Benutzungsschnittstelle (GUI)
 * für den eshop. 
 *
 * @version 7 (Swing-GUI)
 */
public class SwingLagClientGUI extends JFrame {

	private LagerVerwaltung lag;
	private LagerClientCUI cui;
	
	private JTextField titleField;
	private JTextField numberField;
	private JButton button;
	private JTextField searchField;
//	private JList bookList;
	private JTable warenTable;
	
	public SwingLagClientGUI(String datei) throws IOException {
		super("ESHOP");
		
		// Windows-Fenster und -Buttondesign:
		try {
			 UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Lagerverwaltung, um die Funktionen, die nichts ausgeben
		// weiterhin zu benutzen
		lag = new LagerVerwaltung(datei);
		
		// Fenster starten
		initialize();
	}

	
	private void initialize() {
		
		// Größe des Fensters festlegen
		setSize(new Dimension(800, 400));
		setLayout(new BorderLayout());

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
		
		// PANEL OBEN
		
		JPanel panelOben = new JPanel();
		panelOben.setLayout(new GridLayout(1, 5));
		
		// Warensuch-Eingabefeld und Suchen-Button
		searchField = new JTextField();
		searchField.setToolTipText("Hier bitte Suchbegriff eingeben.");	// Ausgabe, wenn Maus über Eingabefeld bleibt
		button = new JButton("Suchen");
		button.addActionListener(new SearchListener());
		panelOben.add(new JLabel()); // Abstandhalter
		panelOben.add(new JLabel("Suchbegriff: "));
		panelOben.add(searchField);
		panelOben.add(new JLabel()); // Abstandhalter
		panelOben.add(button);
		panelOben.setBorder(BorderFactory.createTitledBorder("Artikelsuche")); // Umrandung mit dem Titel "Suche"
		
		
		
		// PANEL RECHTS
		
		JPanel panelRechts = new JPanel();
		panelRechts.setLayout(new GridLayout(9, 1));
		
		// Kundennummer-Eingabefeld
		JTextField kundenNummerInput = new JTextField();
		kundenNummerInput.setToolTipText("Hier bitte Kundennummer eingeben.");
		panelRechts.add(new JLabel("Kundennummer: "));
		panelRechts.add(kundenNummerInput);
		
		// Passwort-Eingabe
		JTextField passwortInput = new JTextField();
		panelRechts.add(new JLabel("Passwort: "));
		panelRechts.add(passwortInput);
		
		panelRechts.add(new JLabel()); // Abstandhalter
		
		// Einloggen-Button
		JButton loginButton = new JButton("Einloggen");
		loginButton.addActionListener(new SearchListener()); 
		panelRechts.add(loginButton);
		
		panelRechts.add(new JLabel()); // Abstandhalter
		panelRechts.add(new JLabel()); // Abstandhalter
		
		// Ausloggen-Button
		JButton logoutButton = new JButton("Ausloggen");
		logoutButton.addActionListener(new SearchListener());
		panelRechts.add(logoutButton);

		panelRechts.setBorder(BorderFactory.createTitledBorder("User"));

		
		
		// PANEL LINKS
		
		JPanel panelLinks = new JPanel();
		panelLinks.setLayout(new GridLayout(10, 1));
		
		// Eingabe-Felder für Nummer, Titel, Bestand und Preis der Ware
		panelLinks.add(new JLabel("Nummer: "));
		numberField = new JTextField();
		panelLinks.add(numberField);
		panelLinks.add(new JLabel("Titel: "));
		titleField = new JTextField();
		panelLinks.add(titleField);
		panelLinks.add(new JLabel("Bestand: "));
		numberField = new JTextField();
		panelLinks.add(numberField);
		panelLinks.add(new JLabel("Preis: "));
		numberField = new JTextField();
		panelLinks.add(numberField);
		
		panelLinks.add(new JLabel());		// Abstandshalter
		
		JButton addButton = new JButton("Einfügen");
		addButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent ae) {
				int number = Integer.parseInt(numberField.getText());
				String title = titleField.getText();
				int bestand = Integer.parseInt(numberField.getText());
				float preis = Float.parseFloat(numberField.getText());
				try {
					lag.fuegeWareEin(title, number, bestand, preis);
					updateList(lag.gibAlleWaren());
				} catch (WareExistiertBereitsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panelLinks.add(addButton);	
		
		panelLinks.setBorder(BorderFactory.createTitledBorder("Einfügen"));

		
		
		// PANEL MITTE
		
		warenTable = new JTable(new WarenTableModel(lag.gibAlleWaren()));
		JScrollPane panelMitte = new JScrollPane(warenTable);
		
		
		
		// Inhalt des Frames
		add(panelOben, BorderLayout.NORTH);
		add(panelLinks, BorderLayout.WEST);
		add(panelMitte, BorderLayout.CENTER);
		add(panelRechts, BorderLayout.EAST);

		// Menu aufbauen
		initMenu();
		
		setVisible(true);
	}

	private void initMenu() {
		JMenu fileMenu = new FileMenu();
		JMenu helpMenu = new HelpMenu();
		
		JMenuBar bar = new JMenuBar();
		bar.add(fileMenu);
		bar.add(helpMenu);
		
		setJMenuBar(bar);
	}
	
	private void updateList(java.util.List<Ware> buecher) {
//		// Code für den Einsatz einer JList
//		DefaultListModel lm = new DefaultListModel();
//		
//		bookList.removeAll();
//		Iterator<Buch> it = buecher.iterator();
//		while (it.hasNext()) {
//			String buch = it.next().toString();
//			lm.addElement(buch);
//		}
//		
//		bookList.setModel(lm);

		// Code für den Einsatz einer JTable
		WarenTableModel btm = (WarenTableModel) warenTable.getModel();
		btm.updateDataVector(buecher);
	}
	

	/**
	 * Die main-Methode...
	 */
	public static void main(String[] args) {
		try {
			SwingLagClientGUI gui = new SwingLagClientGUI("LAG");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class SearchListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(button)) {
				java.util.List<Ware> result;
				String titel = searchField.getText();
				if (titel.equals("")) {
					result = lag.gibAlleWaren();
				} else {
					result = lag.sucheNachBezeichnung(titel);
				}
				updateList(result);
			}
		}
	}
	
	class FileMenu extends JMenu implements ActionListener {
		public FileMenu() {
			super("File");
			
			JMenuItem saveItem = new JMenuItem("Save");
			add(saveItem);
			saveItem.addActionListener(this);
			addSeparator();
			JMenuItem quitItem = new JMenuItem("Quit");
			add(quitItem);
			quitItem.addActionListener(this);
			
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getActionCommand().equals("Save")) {
				try {
					lag.schreibeWaren();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				setVisible(false);
				dispose();
				System.exit(0);				
			}
		}
	}


	class HelpMenu extends JMenu {
		public HelpMenu() {
			super("Help");			
		}
	}
}
