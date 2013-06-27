
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
import javax.swing.WindowConstants;

import bib.local.domain.LagerVerwaltung;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.valueobjects.Ware;

/**
 * Klasse für sehr einfache graphische Benutzungsschnittstelle (GUI)
 * für die Bibliothek. 
 * 
 * @author teschke
 * @version 7 (Swing-GUI)
 */
public class SwingBibClientGUI extends JFrame {

	private LagerVerwaltung lag;
	
	private JTextField titleField;
	private JTextField numberField;
	private JButton searchButton;
	private JTextField searchField;
//	private JList bookList;
	private JTable bookTable;
	
	public SwingBibClientGUI(String datei) throws IOException {
		super("Bibliothek");
		
//		// Code für Umschaltung des Look-and-Feels:
//		// (Einfach mal ausprobieren!)
//		try {
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//			SwingUtilities.updateComponentTreeUI(this);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}

		// die Bib-Verwaltung erledigt die Aufgaben, 
		// die nichts mit Ein-/Ausgabe zu tun haben
		lag = new LagerVerwaltung(datei);

		initialize();
	}

	
	private void initialize() {
		setSize(new Dimension(600, 400));
		setLayout(new BorderLayout());

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// North
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1, 5));
		northPanel.add(new JLabel());	// Abstandshalter
		northPanel.add(new JLabel("Suche: "));
		searchField = new JTextField();
		searchField.setToolTipText("Hier bitte Suchbegriff eingeben.");
		northPanel.add(searchField);
		northPanel.add(new JLabel());	// Abstandshalter
		searchButton = new JButton("Suchen");
		searchButton.addActionListener(new SearchListener());
		northPanel.add(searchButton);
		
		northPanel.setBorder(BorderFactory.createTitledBorder("Suche"));

		// West
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new GridLayout(10, 1));	// 10 Zeilen, um ein längeres Panel zu erreichen
		westPanel.add(new JLabel("Nummer: "));
		numberField = new JTextField();
		westPanel.add(numberField);
		westPanel.add(new JLabel("Titel: "));
		titleField = new JTextField();
		westPanel.add(titleField);
		westPanel.add(new JLabel("Bestand: "));
		numberField = new JTextField();
		westPanel.add(numberField);
		westPanel.add(new JLabel("Preis: "));
		numberField = new JTextField();
		westPanel.add(numberField);
		
		
		
		westPanel.add(new JLabel());		// Abstandshalter
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
		westPanel.add(addButton);	
		
		westPanel.setBorder(BorderFactory.createTitledBorder("Einfügen"));

		// Center
//		// Code für den Einsatz einer JList
//		bookList = new JList();
//		updateList(bib.gibAlleBuecher());
		// Code für den Einsatz einer JTable
		bookTable = new JTable(new BooksTableModel(lag.gibAlleWaren()));

		JScrollPane scrollPane = new JScrollPane(bookTable);
		
		// Inhalt des Frames
		add(northPanel, BorderLayout.NORTH);
		add(westPanel, BorderLayout.WEST);
		add(scrollPane, BorderLayout.CENTER);

		// Menu aufbauen
		initMenu();
		
		setVisible(true);
		// auskommentiert, weil ich die Größe des Fensters oben explizit gesetzt habe
//		pack();
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
		BooksTableModel btm = (BooksTableModel) bookTable.getModel();
		btm.updateDataVector(buecher);
	}
	

	/**
	 * Die main-Methode...
	 */
	public static void main(String[] args) {
		try {
			SwingBibClientGUI gui = new SwingBibClientGUI("LAG");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class SearchListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource().equals(searchButton)) {
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
