
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.PersonExistiertNichtException;
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
  private JButton kaufenButton;
  private JButton suchButton;
  private JButton loginButton;
  private JButton logoutButton;
  private JLabel userLabel;
  private JTextField suchFeld;
  private JTextField userNameInput;
  private JPasswordField passwortInput;
  private JTable warenTable;
  private TableRowSorter<TableModel> sorter;
  private JMenuItem logoutItem;
  private JMenuItem addUserItem;
  private JMenuItem delUserItem;
  private JMenuItem allUsersItem;
  
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
    setSize(new Dimension(800, 480));
    setLayout(new BorderLayout());
    
    //Bevor das Programm geschlossen wird, fragt das Programm ob die Änderungen gespeichert werden sollen
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowAdapter(){
    	public void windowClosing(WindowEvent we){
    		int result = JOptionPane.showConfirmDialog(null, "Möchten sie speichern bevor sie beenden?", "Speichern?", JOptionPane.YES_NO_CANCEL_OPTION);
    		if (result == JOptionPane.YES_OPTION){
    			try{
    				lag.schreibePersonen();
    				lag.schreibeWaren();
    				dispose();
    			}catch (IOException e){
    				System.out.println(e.getMessage());
    			}
    		}else if (result == JOptionPane.NO_OPTION){
    			dispose();
    		}else {
    			//do Nothing
    		}
    		
    	}
    });
    
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
    
    //WarenkorbButton mit passendem warenkorb als Bild
    warenkorbButton = new JButton("Warenkorb");
    try{
    	Image img = ImageIO.read(getClass().getResource("/resources/warenkorb.png"));
    	warenkorbButton.setIcon(new ImageIcon(img));
    }catch(IOException e){
    	e.getMessage();
    }
    warenkorbButton.addActionListener(new WarenkorbListener());
   
    kaufenButton = new JButton ("KAUFEN");
    kaufenButton.setPreferredSize(new Dimension(115,33));
    
    panelUnten.add(warenkorbButton);
    panelUnten.add(new JLabel());
    panelUnten.add(new JLabel());
    panelUnten.add(kaufenButton);
    panelUnten.setBorder(BorderFactory.createTitledBorder("Warenkorb"));
    
    // PANEL RECHTS
    // PANEL RECHTS
    // PANEL RECHTS
    
    JPanel panelRechts = new JPanel();
    panelRechts.setLayout(new GridLayout(11, 1));
    
    // UserName-Eingabefeld
    userNameInput = new JTextField();
    userNameInput.setToolTipText("Hier bitte UserNamen eingeben.");
    panelRechts.add(new JLabel("UserName : "));
    panelRechts.add(userNameInput);
    
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
    panelRechts.add(new JLabel("Eingeloggt als:")); // Abstandhalter
    userLabel = new JLabel(" nicht eingeloggt ");
    userLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
    panelRechts.add(userLabel);
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
    panelLinks.setLayout(new GridLayout(11, 1));
    
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
    //addButton.setPreferredSize(new Dimension(83,12));
    panelLinks.add(addButton);  
    
    panelLinks.setBorder(BorderFactory.createTitledBorder("Einfügen"));
    
    
    // PANEL MITTE
    // PANEL MITTE
    // PANEL MITTE
    
    warenTable = new JTable(new WarenTableModel(lag.gibAlleWaren()));
    // ein TableRowsorter damit der Inhalt der Tabelle korrekt sortiert werden kann
    tableSorter(warenTable);
    warenTable.setRowSorter(sorter);
    
    //ein Mouselistener um auf Doppelklicks zu reagieren
    warenTable.addMouseListener(new MouseAdapter(){
    	public void mouseClicked(MouseEvent e){
    		if (e.getClickCount()==2){
    			JTable target = (JTable)e.getSource();
    			int row = target.getSelectedRow();
    			int column = target.getSelectedColumn();
    			java.util.HashMap<String,Ware> waren = lag.getMeineWarenVerwaltung().getWarenObjekte();
    			if(getEingelogged()==false){
    				JOptionPane.showMessageDialog(null, "Loggen Sie sich bitte zuerst ein!");
    			}else{
    				if(column==1){
    				//getValueAt(row,1) die 1 damit er immer die bezeichnung nimmt und keinen Nullpointer wirft
    				Ware w = waren.get(warenTable.getValueAt(row, 1));
    				if(w.getBestand()>= 1)
    				try {
						lag.inWarenKorbLegen(1, w, user);
						warenkorbButton.setBackground(Color.RED);
	
    				} catch (BestellteMengeNegativException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    				}else if(column==2){
    					
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
  /**
   * Nimmt die waranTabe und macht Spalten sortierbar
   * @param table
   */
  public void tableSorter(JTable table){
	  sorter = new TableRowSorter<TableModel>(table.getModel());
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
    // Code für den Einsatz einer JTable
    WarenTableModel btm = (WarenTableModel) warenTable.getModel();
    btm.updateDataVector(waren);
    //Da das sortieren sonst nicht mehr funktionieren würde hier noch einmal den sorter definieren
    tableSorter(warenTable);
    warenTable.setRowSorter(sorter);
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
	        }else if (getEingelogged() == true && getMitarbeiterBerechtigung() == true){
	          try {
	            int nummer = Integer.parseInt(nummernString);
	            int bestand = Integer.parseInt(bestandsString);
	            
	            //wenn der String ein Komma findet wird es mit einem Punkt ersetzt da sonst eine NumberformatException geworfen wird
	            String realPreisString = preisString.replace(",",".");
	            
	            float preis = Float.valueOf(realPreisString);
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
	        }else if (getEingelogged()==true &&getMitarbeiterBerechtigung()==false){
	        	JOptionPane.showMessageDialog(null, "Ihnen fehlen die nötigen Berechtigungen für diese Aktion");
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
        
        String userName = userNameInput.getText();
        
        String passwort = passwortInput.getText();
        java.util.HashMap<String,Person> result = lag.getMeinePersonenVerwaltung().getPersonenObjekte();
        
        if (passwort.equals("")||userName.equals("")){
          JOptionPane.showMessageDialog(null, "es darf kein Feld leer sein!");
        }else if (result.containsKey(userName)&&result.get(userName).getPassword().equals(passwort)){
        	userNameInput.setText("");
        	passwortInput.setText("");
        	setEingelogged(true);
			user = lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName);
        	if(user.getMitarbeiterberechtigung()) mitarbeiterBerechtigung = true;
        	if(getMitarbeiterBerechtigung()==true){
        		allUsersItem.setVisible(true);
        		addUserItem.setVisible(true);
        		delUserItem.setVisible(true);
        	}
        	logoutItem.setVisible(true);
        	logoutButton.setVisible(true);
        	loginButton.setVisible(false);
        	userLabel.setText(" "+user.getUsername());
        	JOptionPane.showMessageDialog(null, "Sie haben sich erfolgreich Eingeloggt!\nHerzlich willkommen "+ user.getUsername()+" !");
        }else if (result.containsKey(userName)&&!result.get(userName).getPassword().equals(passwort)){
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
				  int ok = JOptionPane.showConfirmDialog(null, "Sind sie sicher das sie sich ausloggen möchten?", "Ausloggen", JOptionPane.YES_NO_OPTION);
				  if (ok == JOptionPane.YES_OPTION){
					  setEingelogged(false);
					  user = null;
					  mitarbeiterBerechtigung = false;
					  userLabel.setText(" nicht eingeloggt");
					  addUserItem.setVisible(false);
		        	  delUserItem.setVisible(false);
					  allUsersItem.setVisible(false);
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
      
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
      if (ae.getActionCommand().equals("Speichern")) {
        try {
          lag.schreibeWaren();
          lag.schreibePersonen();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } 
    }
  }

  class UserMenu extends JMenu implements ActionListener {
	  public UserMenu() {
		  super("Benutzer");
		  
		  JMenuItem actUserItem = new JMenuItem("Eingeloggter User");
		  add(actUserItem);
		  actUserItem.addActionListener(this);
		  
		  addUserItem = new JMenuItem("User hinzufügen");
		  addUserItem.setVisible(false);
		  add(addUserItem);
		  addUserItem.addActionListener(this);
		  
		  delUserItem = new JMenuItem ("User entfernen");
		  delUserItem.setVisible(false);
		  add(delUserItem);
		  delUserItem.addActionListener(this);
		  
		  allUsersItem = new JMenuItem("Alle User ausgeben");
		  allUsersItem.setVisible(false);
		  add(allUsersItem);
		  allUsersItem.addActionListener(this);
		  
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
			  if (getMitarbeiterBerechtigung() == true){
				  mitarbeiter = "Ja ";
			  }else{
				  mitarbeiter = "Nein";
			  }
			  JOptionPane.showMessageDialog(null, "Eingeloggter User : "+ user.getUsername() +"\nMitarbeiter : " +mitarbeiter);
			  }
		  }else if (ae.getActionCommand().equals("User hinzufügen")){
			  if (getEingelogged()== true&&getMitarbeiterBerechtigung()==true){
				  JTextField nrField = new JTextField();
			      JTextField nameField = new JTextField();
			      JTextField strField = new JTextField();
			      JTextField plzField = new JTextField();
			      JTextField ortField = new JTextField();
			      JTextField eMailField = new JTextField();
			      JTextField userNameField = new JTextField();
			      JPasswordField passwordField = new JPasswordField();
			      
			     
			      JComboBox<String> herrfrauCombo = new JComboBox<String>();
				  herrfrauCombo.addItem("Herr");
				  herrfrauCombo.addItem("Frau");
			      
			      
			      JPanel myPanel = new JPanel();
			      myPanel.setLayout(new GridLayout(10,2));
			      myPanel.add(new JLabel("Nummer:"));
			      myPanel.add(nrField);
			      myPanel.add(herrfrauCombo);
			      myPanel.add(new JLabel()); // Platzhalter
			      myPanel.add(new JLabel("Name:"));
			      myPanel.add(nameField);
			      myPanel.add(new JLabel("Straße :"));
			      myPanel.add(strField);
			      myPanel.add(new JLabel("Postleitzahl:"));
			      myPanel.add(plzField);
			      myPanel.add(new JLabel("Ort:"));
			      myPanel.add(ortField);
			      myPanel.add(new JLabel("E-Mail:"));
			      myPanel.add(eMailField);
			      myPanel.add(new JLabel("UserName:"));
			      myPanel.add(userNameField);
			      myPanel.add(new JLabel("Passwort:"));
			      myPanel.add(passwordField);

			      int result = JOptionPane.showConfirmDialog(null, myPanel,"Geben sie die Daten der Person an", JOptionPane.OK_CANCEL_OPTION);
			      if (result == JOptionPane.OK_OPTION) {
			    	  String nummernString = nrField.getText();
			    	  int nr = Integer.parseInt(nummernString);
			    	  
			    	  String anrede = (String) herrfrauCombo.getSelectedItem();
			    	  
			          try {
						lag.fuegePersonEin(nr, nameField.getText(), anrede, strField.getText(), plzField.getText(), ortField.getText(), 
								eMailField.getText(), userNameField.getText(), passwordField.getText(), false);
						JOptionPane.showMessageDialog(null, "Person erfolgreich eingefügt!");
			          } catch (PersonExistiertBereitsException e) {
			        	  JOptionPane.showMessageDialog(null, "Diese Person existiert bereits!");
			        	  e.printStackTrace();
			          }
			      }
			  }else{
				  JOptionPane.showMessageDialog(null, "Bitte loggen sie sich zuerst ein");
			  }
			  
			  
		  }else if (ae.getActionCommand().equals("User entfernen")){
			  //Personen werden mit der personEntfernen Methode gelöscht
			  //Dies geschieht mit der Hilfe eines JOptionPane.showInputDialog
			  if (getEingelogged()== true&&getMitarbeiterBerechtigung()==true){
				  String userName = JOptionPane.showInputDialog(null, "Geben sie den UserNamen der zu entfernenden Person ein",JOptionPane.OK_CANCEL_OPTION);
				  try {
					  lag.personEntfernen(lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName));
					  JOptionPane.showMessageDialog(null, "Erfolgreich gelöscht");
				  } catch (PersonExistiertNichtException e) {
					  JOptionPane.showMessageDialog(null, "Diese Person existiert nicht !");
				  }
			  }else{
				  JOptionPane.showMessageDialog(null, "Bitte loggen sie sich zuerst ein");
			  }
			  
			  
		  }else if (ae.getActionCommand().equals("Alle User ausgeben")){
			  //Alle user werden in einer JTable ausgegeben die sortierbar ist
			  if (getEingelogged()== true&&getMitarbeiterBerechtigung()==true){
			  JTable personenTable = new JTable(new PersonenTableModel(lag.gibAllePersonen()));
			  //Notwendig da der JOpionPane sonst viel zu klein wäre
			  personenTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			  personenTable.getColumnModel().getColumn(0).setMinWidth(100);
			  personenTable.getColumnModel().getColumn(1).setMinWidth(100);
			  personenTable.getColumnModel().getColumn(2).setMinWidth(100);
			  personenTable.getColumnModel().getColumn(3).setMinWidth(130);
			  personenTable.getColumnModel().getColumn(4).setMinWidth(130);
			  personenTable.getColumnModel().getColumn(5).setMinWidth(50);
			  personenTable.getColumnModel().getColumn(6).setMinWidth(100);
			  personenTable.setPreferredScrollableViewportSize(new Dimension(700,200));
		
			  TableRowSorter<TableModel> personenSorter = new TableRowSorter<TableModel>(personenTable.getModel());
			  personenSorter.setComparator(0,new Comparator<String>() {
			      public int compare (String s1, String s2)
			      {
			        int i1 = Integer.parseInt(s1), i2 = Integer.parseInt(s2);
			        return i1 - i2;
			      }
			    });
			  personenTable.setRowSorter(personenSorter);
			  
			  JScrollPane personenPanel = new JScrollPane(personenTable);
			  
			  JOptionPane.showMessageDialog(null, personenPanel,"Alle Personen", JOptionPane.PLAIN_MESSAGE);
			  }else{
				  JOptionPane.showMessageDialog(null, "Bitte loggen sie sich zuerst ein");
			  }
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
  public boolean getMitarbeiterBerechtigung(){
	  return mitarbeiterBerechtigung;
  }
  
  public void setEingelogged(boolean log){
	  this.eingelogged=log;
  }
}
