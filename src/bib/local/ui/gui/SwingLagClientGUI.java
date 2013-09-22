
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import bib.local.domain.LagerVerwaltung;
import bib.local.domain.exceptions.BestellteMengeNegativException;
import bib.local.domain.exceptions.NichtVielfachesVonPackGroesseException;
import bib.local.domain.exceptions.PersonExistiertBereitsException;
import bib.local.domain.exceptions.PersonExistiertNichtException;
import bib.local.domain.exceptions.WareExistiertBereitsException;
import bib.local.domain.exceptions.WareExistiertNichtException;
import bib.local.ui.gui.tableModels.PersonenTableModel;
import bib.local.ui.gui.tableModels.WarenTableModel;
import bib.local.valueobjects.Person;
import bib.local.valueobjects.Rechnung;
import bib.local.valueobjects.Ware;

/**
 * Der Eshop in einer gui version, sollte alles das können was die gui auch kann
 *
 * @version 7 (Swing-GUI)
 */
public class SwingLagClientGUI extends JFrame {
  //Die LagerVerwaltung und alle Attribute die global verfügbar sein müssen
  private LagerVerwaltung lag;
  
  private JPanel panelLinks;
  
  private JTextField titelFeld;
  private JTextField nummernFeld;
  private JTextField bestandsFeld;
  private JTextField preisFeld;
  private JTextField packungsGroesseFeld;
  
  private JButton addButton;
  private JButton warenkorbButton;
  private JButton kaufenButton;
  private JButton ausKorbEntfernenButton;
  private JButton warenkorbLeerenButton;
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
  private JMenuItem delUserItem;
  private JMenuItem upgradeItem;
  private JMenuItem allUsersItem;
  
  private JMenuItem bestandsItem;
  private JMenuItem delWareItem;
  private JMenuItem warenlogItem;
  
  private boolean eingelogged = false;
  private boolean mitarbeiterBerechtigung = false;

  private static Person user ;
  
  public SwingLagClientGUI(String datei) throws IOException {
    super("ESHOP");
    
    // Windows-Fenster und -Buttondesign:
    try {
       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
    		int result = JOptionPane.showConfirmDialog(null, "Vor dem Beenden muss gespeichert werden", "Speichern?", JOptionPane.OK_CANCEL_OPTION);
    		if (result == JOptionPane.OK_OPTION){
    			try{
    				lag.schreibePersonen();
    				lag.schreibeWaren();
    				dispose();
    			}catch (IOException e){
    				System.out.println(e.getMessage());
    			}
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
    
    //WarenkorbButton mit passendem Warenkorb als Bild
    warenkorbButton = new JButton("Warenkorb");
    try{
    	Image img = ImageIO.read(getClass().getResource("/resources/warenkorb.png"));
    	warenkorbButton.setIcon(new ImageIcon(img));
    }catch(IOException e){
    	e.getMessage();
    }
    warenkorbButton.setPreferredSize(new Dimension(155,33));
    warenkorbButton.addActionListener(new WarenkorbListener());
   
    //Kaufenbutton um die waren zu kaufen
    kaufenButton = new JButton ("KAUFEN");
    kaufenButton.setPreferredSize(new Dimension(155,33)); //damit er die gleiche größe hat wie der warenkorbButton
    kaufenButton.addActionListener(new KaufenListener());
    
    //einzelne oder mehrere Waren aus dem Korb entfernen
    ausKorbEntfernenButton = new JButton("Ware aus Korb entfernen");
    ausKorbEntfernenButton.setPreferredSize(new Dimension(155,33));
    ausKorbEntfernenButton.setVisible(false);
    ausKorbEntfernenButton.addActionListener(new AusKorbEntfernenListener());
    
    //Button um den ganzen Korb zu leeren
    warenkorbLeerenButton = new JButton("Warenkorb Leeren");
    warenkorbLeerenButton.setPreferredSize(new Dimension(155,33));
    warenkorbLeerenButton.setVisible(false);
    warenkorbLeerenButton.addActionListener(new KorbLeerenListener());
    
    panelUnten.add(warenkorbButton);
    panelUnten.add(kaufenButton);
    panelUnten.add(ausKorbEntfernenButton);
    panelUnten.add(warenkorbLeerenButton);
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
    
    panelLinks = new JPanel();
    panelLinks.setLayout(new GridLayout(12, 1));
    // Panel ausblenden bis der Benutzer sich eingeloggt hat
    panelLinks.setVisible(false);
    
    // Eingabe-Felder für Nummer, Titel, Bestand und Preis der Ware
    panelLinks.add(new JLabel("Nummer:"));
    nummernFeld = new JTextField();
    panelLinks.add(nummernFeld);
    panelLinks.add(new JLabel("Titel:"));
    titelFeld = new JTextField();
    panelLinks.add(titelFeld);
    panelLinks.add(new JLabel("Bestand:"));
    bestandsFeld = new JTextField();
    panelLinks.add(bestandsFeld);
    panelLinks.add(new JLabel("Preis:"));
    preisFeld = new JTextField();
    panelLinks.add(preisFeld);
    
    panelLinks.add(new JLabel("Pack.Größe:"));
    packungsGroesseFeld = new JTextField();
    packungsGroesseFeld.setText("1");
    panelLinks.add(packungsGroesseFeld);
    
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
    			//Wo wurde geklickt
    			JTable target = (JTable)e.getSource();
    			int row = target.getSelectedRow();
    			java.util.HashMap<String,Ware> waren = lag.getMeineWarenVerwaltung().getWarenObjekte();
    			if (getEingelogged() == false) {
    				JOptionPane.showMessageDialog(null, "Loggen Sie sich bitte zuerst ein!");
    			} else {
    				//getValueAt(row,1) die 1 damit er immer die bezeichnung nimmt und keinen Nullpointer wirft
    				Ware w = waren.get(warenTable.getValueAt(row, 1));
    				//InputDialog und parsen um zu bekommen wieviele
    				String mengenString = JOptionPane.showInputDialog("Wieviele von : \"" + w.getBezeichnung() + "\" wollen sie bestellen");
    				if (mengenString!=null){
    					int menge = 0;
    					try {
    						menge = Integer.parseInt(mengenString);
    					} catch (NumberFormatException ex) {
    						ex.printStackTrace();
    					}
    					
    					if(menge == 0) {
    						JOptionPane.showMessageDialog(null, "Bitte geben Sie eine gültige Menge an", "ERROR", JOptionPane.ERROR_MESSAGE);
    					} else if (w.getBestand() >= menge) {
    						try {
    							//in den korb des users legen
    							lag.inWarenKorbLegen(menge, w, user);
    							warenkorbButton.setBackground(Color.RED);
    							//nun werdn die korb leeren buttons sichtbar , da der korb inhalt hat
    							ausKorbEntfernenButton.setVisible(true);
    							warenkorbLeerenButton.setVisible(true);
    						} catch (NichtVielfachesVonPackGroesseException | BestellteMengeNegativException ex) {
    							JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
    						}
    					}else if (w.getBestand()<menge){
        					JOptionPane.showMessageDialog(null, "Ihre Anfrage übersteigt den vorhandenen Bestand", "ERROR", JOptionPane.ERROR_MESSAGE);
        				}
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
   
    // Menu aufbauen
    initMenu();
    
    setVisible(true);
  }
  /**
   * Nimmt die waranTabe und macht die Spalten sortierbar
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
  /**
   * Initialisiert die MenüLeiste
   */
  private void initMenu() {
    JMenu fileMenu = new FileMenu();
    JMenu userMenu = new UserMenu();
    JMenu wareMenu = new WarenMenu();
    JMenu helpMenu = new HelpMenu();
    
    JMenuBar bar = new JMenuBar();
    bar.add(fileMenu);
    bar.add(userMenu);
    bar.add(wareMenu);
    bar.add(helpMenu);
    
    setJMenuBar(bar);
  }
  /**
   * um die warentable zu aktualisieren
   * @param waren die waren damit die warentable die neuesten daten bekommt
   */
  private void updateList(java.util.List<Ware> waren) {
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
  
  /**
   * Der Addlistener fügt neue Waren hinzu, sollte die PaketGröße mehr als 1 betragen handelt es sich um einen massengutartikel
   * @author Florian
   *
   */
  class AddListener implements ActionListener{
	  public void actionPerformed(ActionEvent ae) {
	        String nummernString = nummernFeld.getText();
	        String titel = titelFeld.getText();
	        String bestandsString = bestandsFeld.getText();
	        String preisString = preisFeld.getText();
	        String packungsGroesseString = packungsGroesseFeld.getText();
	        
	        if(nummernString.equals("")||titel.equals("")||bestandsString.equals("")||preisString.equals("")){
	        	JOptionPane.showMessageDialog(null, "Alle Felder müssen ausgefüllt sein!");
	        }else if (getEingelogged() == true && getMitarbeiterBerechtigung() == true){
	        	try {
	        		//Falls iregendwo ein minus auftaucht wird es einfach mit nichts ersetzt
	        		String realNummernString = nummernString.replace("-", "");
	        		String realBestandsString = bestandsString.replace("-", "");
	        		String realpackungsGroesseString = packungsGroesseString.replace("-", "");
	        		int nummer = Integer.valueOf(realNummernString);
	        		int bestand = Integer.valueOf(realBestandsString);
	        		int packungsGroesse = Integer.valueOf(realpackungsGroesseString);
	        		
	        		//wenn der String ein Komma findet wird es mit einem Punkt ersetzt da sonst eine NumberformatException geworfen wird
	        		String realPreisString = preisString.replace(",",".");
	        		String finalPreisString = realPreisString.replace("-", "");
		            float preis = Float.valueOf(finalPreisString);
		            
		            if(checkWarenNummer(lag.gibAlleWaren(),nummer)==false){
		    			  JOptionPane.showMessageDialog(null, "Diese Nummer ist vergeben! ","Error",JOptionPane.ERROR_MESSAGE);
		    		}else{
		            lag.fuegeWareEin(titel, nummer, bestand, preis, packungsGroesse);
		            updateList(lag.gibAlleWaren());
		            nummernFeld.setText("");
		            titelFeld.setText("");
		            bestandsFeld.setText("");
		            preisFeld.setText("");
		            //wenn die packungsgröße 1 ist handelt es sich nicht um einen massengutware , 
		            //da mehr waren als massengutwaren eingefügt werden bietet es sich an den Text auf 1 zu setzen
		            packungsGroesseFeld.setText("1");
		    		}
	        	} catch (NumberFormatException e) {
	        		JOptionPane.showMessageDialog(null, "Nummer, Bestand ,PackungsGröße und Preis müssen aus Zahlen bestehen");
	        		e.printStackTrace();
	        	}catch (WareExistiertBereitsException e1){
	        		JOptionPane.showMessageDialog(null, e1.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
	        	}
	        }
	  }
  }
  /**
   * Der Searchlistener gibt die gesuchte Ware in einer List aus, 
   * sollte der suchenbutton ohne inhalt im textfeld gedrückt werden dann wird 
   * der aktuelle warenTable ausgegeben
   * @author Florian
   *
   */
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
  /**
   * Der LoginListener überprüft ob der User vorhanden ist, ob er ein Mitarbeiter ist
   * Viele Funktionen werden nur als Mitarbeiter zugänglich
   * @author Florian
   *
   */
  class LoginListener implements ActionListener {
	  public void actionPerformed(ActionEvent ae) {
		  if (ae.getSource().equals(loginButton) || ae.getSource().equals(passwortInput)) {
			  
			  String userName = userNameInput.getText();
			  // andere Methoden haben keine Wirkung gezeigt darum sind wir
			  // bei .getText() geblieben
			  String passwort = passwortInput.getText();
			  java.util.HashMap<String, Person> result = lag.getMeinePersonenVerwaltung().getPersonenObjekte();
			  
			  if (passwort.equals("") || userName.equals("")) {
				  JOptionPane.showMessageDialog(null, "es darf kein Feld leer sein!");
			  } else if (result.containsKey(userName) && result.get(userName).getPassword().equals(passwort)) {
				  userNameInput.setText("");
				  passwortInput.setText("");
				  setEingelogged(true);
				  user = lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName);
				  if (user.getMitarbeiterberechtigung())
					  mitarbeiterBerechtigung = true;
				  // Falls ein Mitarbeiter eingeloggt ist werden stehen mehr
				  // Optionen zur Verfügung
				  if (getMitarbeiterBerechtigung() == true) {
					  allUsersItem.setVisible(true);
					  delUserItem.setVisible(true);
					  upgradeItem.setVisible(true);
					  
					  bestandsItem.setVisible(true);
					  delWareItem.setVisible(true);
					  warenlogItem.setVisible(true);
					  
					// Einfügen-panel sichtbar machen sobald der Benutzer eingeloggt ist.
					  panelLinks.setVisible(true);
				  }
				  logoutItem.setVisible(true);
				  logoutButton.setVisible(true);
				  loginButton.setVisible(false);
				  
				  userLabel.setText(" " + user.getUsername());
				  JOptionPane.showMessageDialog(null, "Sie haben sich erfolgreich Eingeloggt!\nHerzlich willkommen " + user.getUsername() + " !");
			  } else if (result.containsKey(userName) && !result.get(userName).getPassword().equals(passwort)) {
				  JOptionPane.showMessageDialog(null, "Inkorrektes Passwort, bitte überprüfen sie ihre Angabe");
			  } else {
				  JOptionPane.showMessageDialog(null, "Es existiert kein Kunde mit dieser Nummer, bitte überprüfen sie ihre Angabe");
			  }
		  }
	  }
  }
  
  /**
   * Wenn sich ein User eingeloggt hat wird der LogoutButton sichtbar
   * Wenn sich dieser User dann ausloggt werden viele Funktionen nicht zugänglich
   * @author Florian
   *
   */
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
		        	  delUserItem.setVisible(false);
		        	  allUsersItem.setVisible(false);
		        	  upgradeItem.setVisible(false);
		        	  
		        	  bestandsItem.setVisible(false);
		        	  delWareItem.setVisible(false);
		        	  warenlogItem.setVisible(false);
		        	  
		        	  logoutItem.setVisible(false);
					  logoutButton.setVisible(false);
					  loginButton.setVisible(true);
					  panelLinks.setVisible(false);
				  }
			  }
		  }
	  }
  }
  /**
   * Zeigt den Inhalt des Warenkorbs an wenn der warenkorbButton gedrückt wird und gibt den Inhalt des Korbs aus
   * @author Florian
   *
   */
  class WarenkorbListener implements ActionListener{
	  public void actionPerformed(ActionEvent ae){
		  if(ae.getSource().equals(warenkorbButton)){
			  if(getEingelogged()==false){
				  JOptionPane.showMessageDialog(null, "Sie sind nicht eingeloggt");
				  
			  }else if(user.getWarenkorb().isEmpty()){
				  JOptionPane.showMessageDialog(null, "Ihr Warenkorb ist leer\n\nDoppelklicken sie auf die Ware die sie kaufen \nmöchten um sie in den Warenkorb zu legen");
			  
			  }else{
				  if(warenkorbButton.getBackground().equals(Color.RED)){
					  warenkorbButton.setBackground(null);
				  }
				  JOptionPane.showMessageDialog(null, user.getWarenkorb(),"Inhalt ihres Warenkorbs", JOptionPane.PLAIN_MESSAGE);
			  }
		  }
	  }
  }
  
  /**
   * Überprüft ob der Kaufen Button gedrückt wurde und verarbeitet die Anfrage entsprechend
   * @author Florian
   *
   */
  class KaufenListener implements ActionListener{
	  public void actionPerformed(ActionEvent ae){
		  if (ae.getSource().equals(kaufenButton)){
			  if(getEingelogged()==false){
				  JOptionPane.showMessageDialog(null, "Sie sind nicht eingeloggt");
			  }else if(user.getWarenkorb().isEmpty()){
				  JOptionPane.showMessageDialog(null, "Ihr Warenkorb ist leer\n\nDoppelklicken sie auf die Ware die sie kaufen \nmöchten um sie in den Warenkorb zu legen");
			  }else{
				  int result = JOptionPane.showConfirmDialog(null, "Sind sie sicher das sie diese Waren kaufen möchten?", "Meldung",  JOptionPane.YES_NO_OPTION);
				  if (result == JOptionPane.YES_OPTION ){
					  lag.setRechnung(new Rechnung(user));
					  JTextArea textArea = new JTextArea(lag.getRechnung().toString());
					  JScrollPane rechnungsScrollPane = new JScrollPane(textArea);  
					  textArea.setLineWrap(true);  
					  textArea.setWrapStyleWord(true); 
					  rechnungsScrollPane.setPreferredSize( new Dimension( 500, 500 ) );
					  JOptionPane.showMessageDialog(null,rechnungsScrollPane,"Ihr Einkauf",JOptionPane.PLAIN_MESSAGE);
					  lag.warenkorbKaufen(user, user.getWarenkorb());
					  warenkorbButton.setBackground(null); //Falls man direkt kauft ohne vorher in den Korb zu gucken wird der Hintergrund wieder auf null gesetzt
					  //Warentable wird nach dem Kauf aktualisiert
					  ausKorbEntfernenButton.setVisible(false);
					  warenkorbLeerenButton.setVisible(false);
					  updateList(lag.gibAlleWaren());
				  }
			  }
		  }
	  }
  }
  /**
   * Methode zum entfernen von Waren aus dem Korb
   * @author Florian
   *
   */
  class AusKorbEntfernenListener implements ActionListener{
	  public void actionPerformed(ActionEvent ae){
		  if(ae.getSource().equals(ausKorbEntfernenButton)){
			  
			  JTextField dieWare = new JTextField();
			  JTextField menge = new JTextField();
			  
			  JPanel ausKorbEntfernenPanel = new JPanel();
			  ausKorbEntfernenPanel.setLayout(new GridLayout(2,2));
			  ausKorbEntfernenPanel.add(new JLabel("Welche Ware ?"));
			  ausKorbEntfernenPanel.add(dieWare);
			  ausKorbEntfernenPanel.add(new JLabel("wieviele ?"));
			  ausKorbEntfernenPanel.add(menge);
			  
			  int result = JOptionPane.showConfirmDialog(null,ausKorbEntfernenPanel,"Ware aus Korb entfernen", JOptionPane.OK_CANCEL_OPTION);
			  if (result == JOptionPane.OK_OPTION){
				  if(dieWare.getText().equals("")||menge.getText().equals("")){
					  JOptionPane.showMessageDialog(null, "Es darf Kein Feld leer sein");
				  }else{
					  String mengenString = menge.getText();
					  int mengeZahl = Integer.parseInt(mengenString);
					  try {
						lag.entferneAusWarenkorb(mengeZahl, lag.getMeineWarenVerwaltung().getWarenObjekte().get(dieWare.getText()), user);
						if (user.getWarenkorb().isEmpty()){
							warenkorbLeerenButton.setVisible(false);
							ausKorbEntfernenButton.setVisible(false);
							warenkorbButton.setBackground(null);
						}
					  } catch (BestellteMengeNegativException e) {
						  JOptionPane.showMessageDialog(null, e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
						
					  }
					  updateList(lag.gibAlleWaren());
					  JOptionPane.showMessageDialog(null, "Erfolgreich aus Korb entfernt");
					
				  }
			  }
		  }
	  }
  }
  /**
   * Erscheint wenn Waren im Korb sind und leert den gesamten Korb auf nachfrage
   * @author Florian
   *
   */
  class KorbLeerenListener implements ActionListener{
	  public void actionPerformed(ActionEvent ae){
		  if(ae.getSource().equals(warenkorbLeerenButton)){
			 int result = JOptionPane.showConfirmDialog(null, "wollen sie ihren Warenkorb leeren?","Meldung",JOptionPane.OK_CANCEL_OPTION);
			 if (result == JOptionPane.OK_OPTION){
				 lag.warenkorbLeeren(user);
				 JOptionPane.showMessageDialog(null, "Erfolgreich geleert");
				 warenkorbLeerenButton.setVisible(false);
				 ausKorbEntfernenButton.setVisible(false);
				 warenkorbButton.setBackground(null);
			 }
		  }
	  }
  }
  
  /**
   * Das Filemenu erlaubt das manuelle Speichern der Personen und Waren
   * @author Florian
   *
   */
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

  /**
   * Das UserMenu hat Funktionen zum User hinzufügen ,User entfernen, alle user ausgeben und ausloggen
   * ausloggen greift auf den LogoutListener zu
   * Eingeloggter User zeig ebenfalls an ob ein Mitarbeiter eingeloggt ist oder nicht
   * @author Florian
   *
   */
  class UserMenu extends JMenu implements ActionListener {
	  public UserMenu() {
		  super("Benutzer");
		  
		  JMenuItem actUserItem = new JMenuItem("Eingeloggter User");
		  add(actUserItem);
		  actUserItem.addActionListener(this);
		  
		  JMenuItem	addUserItem = new JMenuItem("User hinzufügen");
		  add(addUserItem);
		  addUserItem.addActionListener(this);
		  
		  upgradeItem = new JMenuItem("User befördern!");
		  add(upgradeItem);
		  upgradeItem.setVisible(false);
		  upgradeItem.addActionListener(this);
		  
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
			  if (getEingelogged()==false){
				  JOptionPane.showMessageDialog(null,"Sie sind nicht eingeloggt, bitte loggen sie sich zuerst ein");
			  }else{
			  String mitarbeiter = null;
			  //auf Mitarbeiter überprüfen
			  if (getMitarbeiterBerechtigung() == true){
				  mitarbeiter = "Ja ";
			  }else{
				  mitarbeiter = "Nein";
			  }
			  JOptionPane.showMessageDialog(null, "Eingeloggter User : "+ user.getUsername() +"\nMitarbeiter : " +mitarbeiter);
			  }
		  }else if (ae.getActionCommand().equals("User hinzufügen")){
				  //Felder zur Eingabe der Daten
				  JTextField nrField = new JTextField();
			      JTextField nameField = new JTextField();
			      JTextField strField = new JTextField();
			      JTextField plzField = new JTextField();
			      JTextField ortField = new JTextField();
			      JTextField eMailField = new JTextField();
			      JTextField userNameField = new JTextField();
			      JPasswordField passwordField = new JPasswordField();
			      
			      //Da es sich nur um Herr oder Frau handeln kann eine ComboBox
			      JComboBox<String> herrfrauCombo = new JComboBox<String>();
				  herrfrauCombo.addItem("Herr");
				  herrfrauCombo.addItem("Frau");
			      
			      //Zum Panel hinzufügen
			      JPanel addPersonPanel = new JPanel();
			      addPersonPanel.setLayout(new GridLayout(10,2)); //Panel bekommt ein GridLayout
			      addPersonPanel.add(new JLabel("Nummer:"));
			      addPersonPanel.add(nrField);
			      addPersonPanel.add(herrfrauCombo);
			      addPersonPanel.add(new JLabel()); // Platzhalter
			      addPersonPanel.add(new JLabel("Name:"));
			      addPersonPanel.add(nameField);
			      addPersonPanel.add(new JLabel("Straße :"));
			      addPersonPanel.add(strField);
			      addPersonPanel.add(new JLabel("Postleitzahl:"));
			      addPersonPanel.add(plzField);
			      addPersonPanel.add(new JLabel("Ort:"));
			      addPersonPanel.add(ortField);
			      addPersonPanel.add(new JLabel("E-Mail:"));
			      addPersonPanel.add(eMailField);
			      addPersonPanel.add(new JLabel("UserName:"));
			      addPersonPanel.add(userNameField);
			      addPersonPanel.add(new JLabel("Passwort:"));
			      addPersonPanel.add(passwordField);
			      
			      //Der Panel wird in einem JoptionPane dargestellt
			      int result = JOptionPane.showConfirmDialog(null, addPersonPanel,"Geben sie die Daten der Person an", JOptionPane.OK_CANCEL_OPTION);
			      if (result == JOptionPane.OK_OPTION) {
			    	  //Sollte eins der Felder leer sein wird eine Exception geworfen also vorher checken
			    	  if(nrField.getText().equals("")||nameField.getText().equals("")||strField.getText().equals("")||plzField.getText().equals("")||
			    			  ortField.getText().equals("")||eMailField.getText().equals("")||userNameField.getText().equals("")||passwordField.getText().equals("")){
			    		  JOptionPane.showMessageDialog(null, "Keins der Felder darf leer sein");
			    	  }else{
			    		  try {
			    			  String nummernString = nrField.getText();
				    		  String realNummernString = nummernString.replace("-", "");
				    		  //auch hier wird ein minus mit nichts ersetzt
				    		  int nr = Integer.parseInt(realNummernString);
				    		  
				    		  if(checkPersonNummer(lag.gibAllePersonen(),nr)==false){
				    			  JOptionPane.showMessageDialog(null, "Diese Nummer ist vergeben! ","Error",JOptionPane.ERROR_MESSAGE);
				    		  }else{
				    		  
				    		  //Die Auswahl (Herr/Frau) wird im anreden String gespeichert
				    		  String anrede = (String) herrfrauCombo.getSelectedItem();
			    			  lag.fuegePersonEin(nr, nameField.getText(), anrede, strField.getText(), plzField.getText(), ortField.getText(), 
			    					  eMailField.getText(), userNameField.getText(), passwordField.getText(), false); 
			    			  //Am ende ein false damit man sich nicht als mitarbeiter hinzufügen kann
			    			  JOptionPane.showMessageDialog(null, "Person erfolgreich eingefügt!");
				    		  }
			    		  } catch (NumberFormatException e) {
			    			  JOptionPane.showMessageDialog(null, " Die PersonenNummer darf nicht aus Text bestehen","Error",JOptionPane.ERROR_MESSAGE);
			    		  } catch (PersonExistiertBereitsException e1){
			    			  JOptionPane.showMessageDialog(null, "Diese Person existiert bereits ","Error",JOptionPane.ERROR_MESSAGE);
			    		  }
			    	  }
			      }
		  }else if (ae.getActionCommand().equals("User befördern!")){
			  String userName = JOptionPane.showInputDialog(null, "Geben sie an wer Mitarbeiterrechte bekommen soll",JOptionPane.OK_CANCEL_OPTION);
			  if(userName!=null){
				  try{
					  Person p = lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName);
					  lag.personBefördern(p);
					  JOptionPane.showMessageDialog(null, p.getUsername()+" hat nun Mitarbeiterrechte");
				  }catch(PersonExistiertNichtException e){
					  JOptionPane.showMessageDialog(null, "Diese Person existiert nicht !","ERROR",JOptionPane.ERROR_MESSAGE);
				  }
			  }
		  }else if (ae.getActionCommand().equals("User entfernen")){
			  //Personen werden mit der personEntfernen Methode gelöscht
			  //Dies geschieht mit der Hilfe eines JOptionPane.showInputDialog 
				  try {
					  String userName = JOptionPane.showInputDialog(null, "Geben sie den UserNamen der zu entfernenden Person ein",JOptionPane.OK_CANCEL_OPTION);
					  if(userName!= null){
						  lag.personEntfernen(lag.getMeinePersonenVerwaltung().getPersonenObjekte().get(userName));
						  JOptionPane.showMessageDialog(null, "Erfolgreich gelöscht");
					  }
				  } catch (PersonExistiertNichtException e) {
					  JOptionPane.showMessageDialog(null, "Diese Person existiert nicht !","ERROR",JOptionPane.ERROR_MESSAGE);
				  }
			  
		  }else if (ae.getActionCommand().equals("Alle User ausgeben")){
			  //Alle user werden in einer JTable ausgegeben die sortierbar ist
			  
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
		      
			  //Auch diese Tabelle soll sortierbar sein also ein extra TableRowSorter für diese Tabelle
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
			  
		  }
	  }
  }
  /**
   * WarenMenu ist ein Menu mit dem man den Bestand von Waren ändern, Waren löschen und den Bestandslog für eine Datei ausgeben kann
   * 
   * @author Florian
   *
   */
  class WarenMenu extends JMenu implements ActionListener{
	  public WarenMenu(){
		  super ("Waren");
		  
		  bestandsItem = new JMenuItem("Bestand ändern");
		  bestandsItem.setVisible(false);
		  add (bestandsItem);
		  bestandsItem.addActionListener(this);
		  
		  delWareItem = new JMenuItem("Ware löschen");
		  delWareItem.setVisible(false);
		  add(delWareItem);
		  delWareItem.addActionListener(this);
		  
		  warenlogItem = new JMenuItem("Bestandslog für eine Datei ausgeben");
		  warenlogItem.setVisible(false);
		  add(warenlogItem);
		  warenlogItem.addActionListener(this);
		  
	  }
	  
	  public void actionPerformed (ActionEvent ae){
		  if(ae.getActionCommand().equals("Bestand ändern")){
			  //Panel mit eingabeFeldern erstellen
			  JTextField warenField = new JTextField();
			  JTextField bestandsField = new JTextField();
			  
			  JPanel setBestandPanel = new JPanel();
			  setBestandPanel.setLayout(new GridLayout(2,2));
			  setBestandPanel.add(new JLabel("Bezeichnung"));
			  setBestandPanel.add(warenField);
			  setBestandPanel.add(new JLabel("neuer Bestand"));
			  setBestandPanel.add(bestandsField);
			  
			  //Wessen Bestand soll geändert werden mit OK_CANCEL option
			  int result = JOptionPane.showConfirmDialog(null,setBestandPanel,"Bestand einer Ware ändern", JOptionPane.OK_CANCEL_OPTION);
			  if (result == JOptionPane.OK_OPTION){
				  if(warenField.getText().equals("")||bestandsField.getText().equals("")){
					  JOptionPane.showMessageDialog(null, "Es darf Kein Feld leer sein");
				  }else{
					  //Parsen
					  String bestandsString = bestandsField.getText();
					  int bestand = Integer.parseInt(bestandsString);
					  try {
						  //Bestand der angegebenen Ware ändern
						  lag.aendereBestand(lag.getMeineWarenVerwaltung().getWarenObjekte().get(warenField.getText()), bestand);
						  updateList(lag.gibAlleWaren());
						  JOptionPane.showMessageDialog(null, "Bestand erfolgreich geändert !");
					  } catch (IOException e) {
						  JOptionPane.showMessageDialog(null, e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
					  } catch (WareExistiertNichtException e1){
						  JOptionPane.showMessageDialog(null, e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
					  }
				  }
			  }
		  }else if (ae.getActionCommand().equals("Ware löschen")){
			  //Ware löschen wird mit einem InputDialog geregelt
			  String bezeichnung = JOptionPane.showInputDialog(null, "Geben sie den Namen der zu löschenden Ware ein",JOptionPane.OK_CANCEL_OPTION);
			  //Wenn eine Bezeichnung angegeben wurde wird die Datei gelöscht
			  if (bezeichnung!=null){
				  try {
					  lag.entferneWare(lag.getMeineWarenVerwaltung().getWarenObjekte().get(bezeichnung));
				  } catch (WareExistiertNichtException e) {
					  JOptionPane.showMessageDialog(null, e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
				  }
			  }
		  }else{
			  //Panel für Bestandslog abfrage
			  JTextField bezeichnungsField = new JTextField();
			  JTextField tagesField = new JTextField();
			  
			  JPanel warenlogPanel = new JPanel();
			  warenlogPanel.setLayout(new GridLayout(2,2));
			  warenlogPanel.add(new JLabel("Welche ware?"));
			  warenlogPanel.add(bezeichnungsField);
			  warenlogPanel.add(new JLabel("wieviele Tage in der Vergangenheit?"));
			  warenlogPanel.add(tagesField);
			  
			  //Nachfragen für welche Ware
			  int result = JOptionPane.showConfirmDialog(null,warenlogPanel,"Bestandslog einer Ware ausgeben", JOptionPane.OK_CANCEL_OPTION);
			  if (result == JOptionPane.OK_OPTION){
				  if(bezeichnungsField.getText().equals("")||tagesField.getText().equals("")){
					  JOptionPane.showMessageDialog(null, "Es darf Kein Feld leer sein");
				  }else{
					  //Parsen
					  String bezeichnung = bezeichnungsField.getText();
					  String tagesString = tagesField.getText();
					  int tage = Integer.parseInt(tagesString);
					  
					  //Die textArea mit Vorkommnissen von Bestandsänderungen füllen
					  JTextArea textArea = new JTextArea();
					  try {
						  for (int i = 0; i < lag.getWarenLog(bezeichnung, tage).size(); i++) {
							  textArea.append(lag.getWarenLog(bezeichnung,tage).elementAt(i).toString());
						  }
					  } catch (IOException | ParseException e) {
						  JOptionPane.showMessageDialog(null, e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
					  }
					  JScrollPane warenlogScrollPane = new JScrollPane(textArea);  
					  textArea.setLineWrap(true);  
					  textArea.setWrapStyleWord(true); 
					  warenlogScrollPane.setPreferredSize( new Dimension( 500, 500 ) );
					
					  JOptionPane.showMessageDialog(null,warenlogScrollPane,"Warenlog",JOptionPane.PLAIN_MESSAGE);
					  
				  }
			  }
		  }
	  }
  }
  /**
   * eine sehr unausgereifte HilfeFunktion
   * @author Florian
   *
   */
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
  //Accessor Methoden für die gui
  private boolean getEingelogged(){
	  return eingelogged;
  }
  private boolean getMitarbeiterBerechtigung(){
	  return mitarbeiterBerechtigung;
  }
  
  private void setEingelogged(boolean log){
	  this.eingelogged=log;
  }
  /*(non-javadoc)
   * private Methode um auf nummernvergabe zu prüfen, ist eine nummer vergeben muss eine nicht vorhandene angegeben werden
   * Für Waren
   */
  private boolean checkWarenNummer(List<Ware> waren, int nummer){
  	Iterator<Ware> iter = waren.iterator();
  	while (iter.hasNext()){
  		Ware ware = iter.next();
  		if(ware.getNummer() == nummer){
  			return false;
  		}
  	}
  	return true;
  }
  
  /*(non-javadoc)
   * private Methode um auf nummernvergabe zu prüfen, ist eine nummer vergeben muss eine nicht vorhandene angegeben werden
   * Für Personen
   */
  private boolean checkPersonNummer(List<Person> personen,int nummer){
  	Iterator<Person> iter = personen.iterator();
  	while (iter.hasNext()){
  		Person person = iter.next();
  		if(person.getNummer() == nummer){
  			return false;
  		}
  	}
  	return true;
  }
}