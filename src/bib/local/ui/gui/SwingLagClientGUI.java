
package bib.local.ui.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import bib.local.domain.LagerVerwaltung;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.ui.cui.LagerClientCUI;
import bib.local.valueobjects.Person;
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
	
	private JTextField titelFeld;
	private JTextField nummernFeld;
	private JTextField bestandsFeld;
	private JTextField preisFeld;
	private JButton suchButton;
	private JButton loginButton;
	private JTextField suchFeld;
	private JTextField kundenNummerInput;
	private JPasswordField passwortInput;
//	private JList bookList;
	private JTable warenTable;
	private TableRowSorter<TableModel> sorter ;
	
	// Eingeloggten User festlegen
	public Person user = new Person();
		
	// Ist jemand eingeloggt und ist es ein Mitarbeiter?
	public boolean eingeloggt = false;
	public boolean mitarbeiterAngemeldet = false;
	
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
		suchFeld = new JTextField();
		suchFeld.setToolTipText("Hier bitte Suchbegriff eingeben.");	// Ausgabe, wenn Maus über Eingabefeld bleibt
		suchFeld.setPreferredSize(new Dimension(100,13));
		suchButton = new JButton("Suchen");
		suchButton.addActionListener(new SearchListener());
		panelOben.add(new JLabel()); // Abstandhalter
		panelOben.add(new JLabel("Suchbegriff: "));
		panelOben.add(suchFeld);
		panelOben.add(new JLabel()); // Abstandhalter
		panelOben.add(suchButton);
		panelOben.setBorder(BorderFactory.createTitledBorder("Warensuche")); // Umrandung mit dem Titel "Suche"
		
		
		
		// PANEL RECHTS
		
		JPanel panelRechts = new JPanel();
		panelRechts.setLayout(new GridLayout(10, 1));
		
		// Kundennummer-Eingabefeld
		kundenNummerInput = new JTextField();
		kundenNummerInput.setToolTipText("Hier bitte Kundennummer eingeben.");
		panelRechts.add(new JLabel("Kundennummer: "));
		panelRechts.add(kundenNummerInput);
		
		// Passwort-Eingabe
		passwortInput = new JPasswordField();
		panelRechts.add(new JLabel("Passwort: "));
		panelRechts.add(passwortInput);
		
		panelRechts.add(new JLabel()); // Abstandhalter
		
		// Einloggen-Button
		loginButton = new JButton("Einloggen");
		loginButton.addActionListener(new LoginListener()); 
		panelRechts.add(loginButton);
		
		panelRechts.add(new JLabel()); // Abstandhalter
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
		nummernFeld = new JTextField();
		panelLinks.add(nummernFeld);
		panelLinks.add(new JLabel("Titel: "));
		titelFeld = new JTextField();
		panelLinks.add(titelFeld);
		panelLinks.add(new JLabel("Bestand: "));
		bestandsFeld = new JTextField();
		panelLinks.add(bestandsFeld);
		panelLinks.add(new JLabel("Preis: "));
		preisFeld = new JTextField();
		panelLinks.add(preisFeld);
		
		panelLinks.add(new JLabel());		// Abstandshalter
		
		JButton addButton = new JButton("Einfügen");
		addButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent ae) {
				String nummernString = nummernFeld.getText();
				String titel = titelFeld.getText();
				String bestandsString = bestandsFeld.getText();
				String preisString = preisFeld.getText();
				
				if(nummernString.equals("")||titel.equals("")||bestandsString.equals("")||preisString.equals("")){
					JOptionPane.showMessageDialog(null, "Die Felder dürfen nicht leer sein!");
				}else{
					try {
						int nummer = Integer.parseInt(nummernString);
						int bestand = Integer.parseInt(bestandsString);
						float preis = Float.parseFloat(preisString);
						lag.fuegeWareEin(titel, nummer, bestand, preis);
						updateList(lag.gibAlleWaren());
						nummernFeld.setText("");
						titelFeld.setText("");
						bestandsFeld.setText("");
						preisFeld.setText("");
						
					} catch (WareExistiertBereitsException|NumberFormatException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Nummer, Bestand und Preis müssen aus Zahlen bestehen");
						e.printStackTrace();
					}
				}
			}
		});
		addButton.setPreferredSize(new Dimension(83,12));
		addButton.setMaximumSize(new Dimension(10,10));
		panelLinks.add(addButton);	
		
		panelLinks.setBorder(BorderFactory.createTitledBorder("Einfügen"));

		
		
		// PANEL MITTE
		
		warenTable = new JTable(new WarenTableModel(lag.gibAlleWaren()));
		sorter = new TableRowSorter<TableModel>(warenTable.getModel());
		
		sorter.setComparator(0,new Comparator<String>() {
			public int compare (String s1, String s2)
			{
				int i1 = Integer.parseInt(s1), i2 = Integer.parseInt(s2);
				return i1 - i2;
			}
		});
		sorter.setComparator(2,new Comparator<String>() {
			public int compare (String s1, String s2)
			{
				int i1 = Integer.parseInt(s1), i2 = Integer.parseInt(s2);
				return i1 - i2;
			}
		});
		sorter.setComparator(3,new Comparator<String>() {
			public int compare (String s1, String s2)
			{
				try {
					DecimalFormat df = new DecimalFormat("#,##0.00");
                    Number f1 = df.parse(s1);
                    Number f2 = df.parse(s2);
                    if(f1.floatValue() < f2.floatValue() || f1.floatValue() > f2.floatValue() ){
                        return f1.floatValue() < f2.floatValue() ? -1 : 1;
                    }
                    return 0;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
			}
		});
		
		warenTable.setRowSorter(sorter);
		//warenTable.setRowSelectionAllowed(false);
		warenTable.setEnabled(false);
		
		//warenTable.setAutoCreateRowSorter(true);
		JScrollPane panelMitte = new JScrollPane(warenTable);
		
		
		
		// Inhalt des Frames
		add(panelOben, BorderLayout.NORTH);
		add(panelLinks, BorderLayout.WEST);
		add(panelMitte, BorderLayout.CENTER);
		add(panelRechts, BorderLayout.EAST);
		this.setResizable(false);
		//this.setMinimumSize(new Dimension(700,350));
		
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
	
	private void updateList(java.util.List<Ware> waren) {
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
		btm.updateDataVector(waren);
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
			if (ae.getSource().equals(suchButton)) {
				java.util.List<Ware> result;
				String titel = suchFeld.getText();
				if (titel.equals("")) {
					result = lag.gibAlleWaren();
				} else {
					result = lag.sucheNachBezeichnung(titel);
				}
				updateList(result);
			}
		}
	}
	
	class LoginListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			if(ae.getSource().equals(loginButton)){
				
				String nummer = kundenNummerInput.getText();
				String passwort = passwortInput.getSelectedText();
				
				if (nummer.equals("")||passwort.equals("")){
					JOptionPane.showMessageDialog(null, "Die Felder dürfen nicht leer sein!");
				}else{
					
					int kNummer = Integer.parseInt(nummer);
					eingeloggt = true;
					user = lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(kNummer);
					if(cui.user.getMitarbeiterberechtigung()) mitarbeiterAngemeldet = true;
					JOptionPane.showMessageDialog(null, "erfolgreich eingeloggt!");
				
				}
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
