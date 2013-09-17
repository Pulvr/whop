
package bib.local.ui.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import bib.local.domain.LagerVerwaltung;
import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.WareExistiertBereitsException;
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
  
  private JTextField titelFeld;
  private JTextField nummernFeld;
  private JTextField bestandsFeld;
  private JTextField preisFeld;
  private JButton addButton;
  private JButton warenkorbButton;
  private JButton suchButton;
  private JButton loginButton;
  private JButton logoutButton;
  //private JButton userButton;
  private JTextField suchFeld;
  private JTextField kundenNummerInput;
  private JPasswordField passwortInput;
  private JTable warenTable;
  private TableRowSorter<TableModel> sorter ;
  private JMenuItem logoutItem;
  
  private boolean eingelogged = false;
  private boolean mitarbeiterBerechtigung = false;

  private static Person user ;
  
  public SwingLagClientGUI(String datei) throws IOException {
    super("ESHOP");
    
    // Windows-Fenster und -Buttondesign:
    try {
       UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      SwingUtilities.updateComponentTreeUI(this);
    } catch (ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException e) {
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
    setSize(new Dimension(800, 450));
    setLayout(new BorderLayout());

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
    // PANEL OBEN
    // PANEL OBEN
    // PANEL OBEN
    
    JPanel panelOben = new JPanel();
    panelOben.setLayout(new GridLayout(1, 5));
    
    // Warensuch-Eingabefeld und Suchen-Button
    suchFeld = new JTextField();
    suchFeld.setToolTipText("Hier bitte Suchbegriff eingeben.");  // Ausgabe, wenn Maus über Eingabefeld bleibt
    suchFeld.setPreferredSize(new Dimension(100,13));
    suchButton = new JButton("Suchen");
    suchButton.addActionListener(new SearchListener());
    panelOben.add(new JLabel()); // Abstandhalter
    panelOben.add(new JLabel("Suchbegriff: "));
    panelOben.add(suchFeld);
    panelOben.add(new JLabel()); // Abstandhalter
    panelOben.add(suchButton);
    panelOben.setBorder(BorderFactory.createTitledBorder("Warensuche")); // Umrandung mit dem Titel "Suche"
    
    //PANEL UNTEN
    //PANEL UNTEN
    //PANEL UNTEN
    
    JPanel panelUnten = new JPanel();
    panelOben.setLayout(new GridLayout(1, 5));
    
    warenkorbButton = new JButton("Warenkorb");
    try{
    	Image img = ImageIO.read(getClass().getResource("/resources/warenkorb.png"));
    	warenkorbButton.setIcon(new ImageIcon(img));
    }catch(IOException e){
    	e.getMessage();
    }
    warenkorbButton.addActionListener(new WarenkorbListener());
   
    panelUnten.add(warenkorbButton);
    panelUnten.add(new JLabel());
    panelUnten.add(new JLabel());
    panelUnten.add(new JLabel());
    panelUnten.add(new JLabel());
    panelUnten.add(new JLabel("test"));
    panelUnten.setBorder(BorderFactory.createTitledBorder("Warenkorb"));
    
    // PANEL RECHTS
    // PANEL RECHTS
    // PANEL RECHTS
    
    JPanel panelRechts = new JPanel();
    panelRechts.setLayout(new GridLayout(10, 1));
    
    // Kundennummer-Eingabefeld
    kundenNummerInput = new JTextField();
    kundenNummerInput.setToolTipText("Hier bitte Kundennummer eingeben.");
    panelRechts.add(new JLabel("Kundennummer: "));
    panelRechts.add(kundenNummerInput);
    
    // Passwort-Eingabe mit Keyadapter damit man im passwort Feld enter drücken kann zum einloggen
    passwortInput = new JPasswordField();
    passwortInput.addKeyListener(new KeyAdapter(){
    	public void keyPressed(KeyEvent e){
    		int key = e.getKeyCode();
    		if (key == KeyEvent.VK_ENTER){
    			passwortInput.addActionListener(new LoginListener());
    		}
    	}
    });
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
    logoutButton = new JButton("Ausloggen");
    logoutButton.addActionListener(new LogoutListener());
    //wird erst wieder sichtbar wenn sich eine Person einloggt
    logoutButton.setVisible(false);
    panelRechts.add(logoutButton);


    panelRechts.setBorder(BorderFactory.createTitledBorder("User"));

    // PANEL LINKS
    // PANEL LINKS
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
    
    panelLinks.add(new JLabel());   // Abstandshalter
    
    addButton = new JButton("Einfügen");
    addButton.addActionListener(new AddListener());
    addButton.setPreferredSize(new Dimension(83,12));
    //addButton.setMaximumSize(new Dimension(10,10));
    panelLinks.add(addButton);  
    
    panelLinks.setBorder(BorderFactory.createTitledBorder("Einfügen"));
    
    
    // PANEL MITTE
    // PANEL MITTE
    // PANEL MITTE
    
    warenTable = new JTable(new WarenTableModel(lag.gibAlleWaren()));
    // ein TableRowsorter damit der Inhalt der Tabelle korrekt sortiert werden kann
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
    
    //ein Mouselistener um auf Doppelklicks zu reagieren
    warenTable.addMouseListener(new MouseAdapter(){
    	public void mouseClicked(MouseEvent e){
    		if (e.getClickCount()==2){
    			JTable target = (JTable)e.getSource();
    			int row = target.getSelectedRow();
    			int column = target.getSelectedColumn();
    			java.util.HashMap<String,Ware> waren = lag.getMeineWarenVerwaltung().getWarenObjekte();
    			if(getEingelogged()==false/*waren.containsKey(warenTable.getValueAt(row, column))*/){
    				JOptionPane.showMessageDialog(null, "Loggen sie sich zuerst ein um Waren in den Korb zu legen");
    			}else{
    				Ware w = waren.get(warenTable.getValueAt(row, column));
    				if(w.getBestand()>= 1)
    				try {
						lag.inWarenKorbLegen(1, w, user);
						warenkorbButton.setBackground(Color.RED);
//						if (warenkorbButton.getBackground().equals(Color.RED)){
//							warenkorbButton.setBackground(Color.ORANGE);
//							warenkorbButton.setBackground(Color.RED);
//						}	
					} catch (BestellteMengeNegativException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    			}
    			
    		}
    	}
    });
    
    JScrollPane panelMitte = new JScrollPane(warenTable);
    
    
    
    
    // Inhalt des Frames
    add(panelOben, BorderLayout.NORTH);
    add(panelLinks, BorderLayout.WEST);
    add(panelMitte, BorderLayout.CENTER);
    add(panelRechts, BorderLayout.EAST);
    add(panelUnten, BorderLayout.SOUTH);
    this.setResizable(false);
    //this.setMinimumSize(new Dimension(700,350));
   
    // Menu aufbauen
    initMenu();
    
    setVisible(true);
  }
  

  private void initMenu() {
    JMenu fileMenu = new FileMenu();
    JMenu userMenu = new UserMenu();
    JMenu helpMenu = new HelpMenu();
    
    JMenuBar bar = new JMenuBar();
    bar.add(fileMenu);
    bar.add(userMenu);
    bar.add(helpMenu);
    
    setJMenuBar(bar);
  }
  
  private void updateList(java.util.List<Ware> waren) {
//    // Code für den Einsatz einer JList
//    DefaultListModel lm = new DefaultListModel();
//    
//    bookList.removeAll();
//    Iterator<Buch> it = buecher.iterator();
//    while (it.hasNext()) {
//      String buch = it.next().toString();
//      lm.addElement(buch);
//    }
//    
//    bookList.setModel(lm);

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
  
  class AddListener implements ActionListener{
	  public void actionPerformed(ActionEvent ae) {
	        String nummernString = nummernFeld.getText();
	        String titel = titelFeld.getText();
	        String bestandsString = bestandsFeld.getText();
	        String preisString = preisFeld.getText();
	        
	        if(nummernString.equals("")||titel.equals("")||bestandsString.equals("")||preisString.equals("")){
	          JOptionPane.showMessageDialog(null, "Alle Felder müssen ausgefüllt sein!");
	        }else if (getEingelogged() == true){
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
	            JOptionPane.showMessageDialog(null, "Nummer, Bestand und Preis müssen aus Zahlen bestehen");
	            e.printStackTrace();
	          }
	        }else {
	        	JOptionPane.showMessageDialog(null, "Bitte loggen sie sich zuerst ein");
	        }
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
          suchFeld.setText("");
        }
        updateList(result);
      }
    }
  }
  
  class LoginListener implements ActionListener{
    public void actionPerformed(ActionEvent ae){
      if(ae.getSource().equals(loginButton)||ae.getSource().equals(passwortInput)){
        
        String nummer = kundenNummerInput.getText();
        int kNummer = Integer.parseInt(nummer);
        String passwort = passwortInput.getText();
        java.util.HashMap<Integer,Person> result = lag.getMeinePersonenVerwaltung().getPersonenObjekte();
        
        if (nummer.equals("")||passwort.equals("")){
          JOptionPane.showMessageDialog(null, "Es darf kein Feld leer sein!");
        }else if (result.containsKey(kNummer)&&result.get(kNummer).getPassword().equals(passwort)){
        	kundenNummerInput.setText("");
        	passwortInput.setText("");
        	setEingelogged(true);
			user = lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(kNummer);
        	if(user.getMitarbeiterberechtigung()) mitarbeiterBerechtigung = true;
        	logoutItem.setVisible(true);
        	logoutButton.setVisible(true);
        	loginButton.setVisible(false);
        	JOptionPane.showMessageDialog(null, "Sie haben sich erfolgreich Eingeloggt!");
        }else if (result.containsKey(kNummer)&&!result.get(kNummer).getPassword().equals(passwort)){
        	JOptionPane.showMessageDialog(null, "Inkorrektes Passwort, bitte überprüfen sie ihre Angabe");
        }else {
        	JOptionPane.showMessageDialog(null, "Es existiert kein Kunde mit dieser Nummer, bitte überprüfen sie ihre Angabe");
        }
      }
    }
  }
  
  class LogoutListener implements ActionListener{
	  public void actionPerformed(ActionEvent ae){
		  if(ae.getSource().equals(logoutButton)||ae.getSource().equals(logoutItem)){
			  if(getEingelogged()==true){
				  int ok = JOptionPane.showConfirmDialog(null, "Sind sie sicher das sie sich ausloggen wollen?", "Ausloggen", JOptionPane.YES_NO_OPTION);
				  if (ok == JOptionPane.YES_OPTION){
					  setEingelogged(false);
					  user = null;
					  mitarbeiterBerechtigung = false;
					  logoutItem.setVisible(false);
					  logoutButton.setVisible(false);
					  loginButton.setVisible(true);
				  }
			  }
		  }
	  }
  }
  
  class WarenkorbListener implements ActionListener{
	  public void actionPerformed(ActionEvent ae){
		  if(ae.getSource().equals(warenkorbButton)){
			  if(user==null){
				  JOptionPane.showMessageDialog(null, "Sie sind nicht eingeloggt");
				  
			  }else if(user.getWarenkorb().isEmpty()){
				  JOptionPane.showMessageDialog(null, "ihr Warenkorb ist leer");
			  
			  }else{
				  if(warenkorbButton.getBackground().equals(Color.RED)){
					  warenkorbButton.setBackground(null);
				  }
				  JOptionPane.showMessageDialog(null, user.getWarenkorb(),"Inhalt ihres Warenkorbs", JOptionPane.PLAIN_MESSAGE);
			  }
		  }
	  }
  }
  
  class FileMenu extends JMenu implements ActionListener {
    public FileMenu() {
      super("Datei");
      
      JMenuItem saveItem = new JMenuItem("Speichern");
      add(saveItem);
      saveItem.addActionListener(this);
      addSeparator();
      JMenuItem quitItem = new JMenuItem("Schließen");
      add(quitItem);
      quitItem.addActionListener(this);
      
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
      if (ae.getActionCommand().equals("Speichern")) {
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

  class UserMenu extends JMenu implements ActionListener {
	  public UserMenu() {
		  super("Benutzer");
		  
		  JMenuItem actUserItem = new JMenuItem("Eingeloggter User");
		  add(actUserItem);
		  actUserItem.addActionListener(this);
		  JMenuItem addUser = new JMenuItem("User hinzufügen");
		  add(addUser);
		  addUser.addActionListener(this);
		  JMenuItem allUsers = new JMenuItem("Alle User ausgeben");
		  add(allUsers);
		  allUsers.addActionListener(this);
		  addSeparator();
		  logoutItem = new JMenuItem("Ausloggen");
		  logoutItem.setVisible(false);
		  add(logoutItem);
		  logoutItem.addActionListener(new LogoutListener());
	  }
	  public void actionPerformed (ActionEvent ae){
		  if (ae.getActionCommand().equals("Eingeloggter User")){
			  if (user==null){
				  JOptionPane.showMessageDialog(null,"Sie sind nicht eingeloggt");
			  }else{
			  String mitarbeiter = null;
			  if (mitarbeiterBerechtigung == true){
				  mitarbeiter = "Ja ";
			  }else{
				  mitarbeiter = "Nein";
			  }
			  JOptionPane.showMessageDialog(null, "Eingeloggter User : "+ user.getUsername() +"\n Mitarbeiter : " +mitarbeiter);
			  }
		  }else if (ae.getActionCommand().equals("User hinzufügen")){
			  if (getEingelogged()==true){
				  JTextField xField = new JTextField(5);
			      JTextField yField = new JTextField(5);

			      JPanel myPanel = new JPanel();
			      myPanel.add(new JLabel("x:"));
			      myPanel.add(xField);
			      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			      myPanel.add(new JLabel("y:"));
			      myPanel.add(yField);

			      int result = JOptionPane.showConfirmDialog(null, myPanel, 
			               "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
			      if (result == JOptionPane.OK_OPTION) {
			         System.out.println("x value: " + xField.getText());
			         System.out.println("y value: " + yField.getText());
			      }
			  }else{
				  JOptionPane.showMessageDialog(null, "Bitte loggen sie sich zuerst ein");
			  }
		  }else if (ae.getActionCommand().equals("Alle User ausgeben")){
			  
			  JTable personenTable = new JTable(new PersonenTableModel(lag.gibAllePersonen()));
			  personenTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			  personenTable.getColumnModel().getColumn(0).setMinWidth(100);
			  personenTable.getColumnModel().getColumn(1).setMinWidth(100);
			  personenTable.getColumnModel().getColumn(2).setMinWidth(100);
			  personenTable.getColumnModel().getColumn(3).setMinWidth(130);
			  personenTable.getColumnModel().getColumn(4).setMinWidth(130);
			  personenTable.getColumnModel().getColumn(5).setMinWidth(50);
			  personenTable.getColumnModel().getColumn(6).setMinWidth(100);
			  
			  JScrollPane personenPanel = new JScrollPane(personenTable);
			  
			  JOptionPane.showMessageDialog(null, personenPanel,"Alle Personen", JOptionPane.PLAIN_MESSAGE);
		  }
	  }
  }

  class HelpMenu extends JMenu implements ActionListener{
    public HelpMenu() {
      super("Hilfe");
      
      JMenuItem helpItem = new JMenuItem("Hilfe");
      add(helpItem);
      helpItem.addActionListener(this);
    }
    
    public void actionPerformed (ActionEvent ae){
    	if(ae.getActionCommand().equals("Hilfe")){
    		JOptionPane.showMessageDialog(null, "Doppelklicken sie auf den Titel der Ware die sie einkaufen möchten um diese in den Warenkorb zu legen");
    	}
    }
  }
  
  public boolean getEingelogged(){
	  return eingelogged;
  }
  
  public void setEingelogged(boolean log){
	  this.eingelogged=log;
  }
}
