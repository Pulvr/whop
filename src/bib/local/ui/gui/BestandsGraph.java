package bib.local.ui.gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import bib.local.valueobjects.WarenLog;

public class BestandsGraph extends Canvas {
    private static final long serialVersionUID = 1L;
    
    private Vector<Integer> daten = new Vector<Integer>();
    private int tage = 30;
    /**
     * tageX definiert die genaue Pixelposition der Tage
     * wo sich auf der Pixel X-Achse der Tag befindet
     */
    private Vector<Integer> tageX;
    private int schrittBestand;
    private int pixelPositionYBestandNull;
    
    public BestandsGraph() {
        super();
        tageX = new Vector<Integer>();
    }
    
    public void paint(Graphics g) {
        Dimension d = this.getSize();
        int width = (int) d.getWidth();
        int height = (int) d.getHeight();
        
        zeichneHintergrund(g, width, height);
        
        if (daten != null && !daten.isEmpty()) {
            zeichneAchsen(g, width, height);
            zeichneGraph(g, width, height);
        }
    }
    
    /**
     * Findet den maximalen Warenbestand in den verfügbaren Log-Einträgen
     * @return Der maximale Bestand im WarenLog
     */
    private int findeBestandMax() {
        int max = 0;
        if (daten == null)
            return 0;
        for (Integer bestand : daten) {
            max = Math.max(bestand, max);
        }
        return max;
    }

    /**
     * zeichnet die Bestandslinie in X & Y
     * 
     * drawPolyline  werden die X- und Y-Positionen übergeben 
     * um eine zusammenhängende Linie zu zeichnen
     * 
     * @param g
     * @param width
     * @param height
     */
    private void zeichneGraph(Graphics g, int width, int height) {
        int[] xPoints = new int[tage];
        int[] yPoints = new int[tage];
        for (int i = 0; i < daten.size(); i++) {
            xPoints[i] = tageX.get(i);
            yPoints[i] = pixelPositionYBestandNull - daten.get(i) * schrittBestand;
        }
        g.drawPolyline(xPoints, yPoints, tage); 
    }

    private void zeichneAchsen(Graphics g, int width, int height) {
        int bestandSchritte = 5; //geht beim Bestand in fünfer Schritten nach oben
        int bestandMax = findeBestandMax(); // sucht die größte Bestandszahl
        bestandMax = (int)Math.ceil(((double)bestandMax / bestandSchritte)) * bestandSchritte; //wird immer entsprechend der Schritte aufgerundet
        tageX.clear(); //Liste wird geleert
        
        //zum berechnen von Textgrößen
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D tagRahmen = fm.getStringBounds("Tag", g); //wie hoch und wie breit
        Rectangle2D bestandRahmen = fm.getStringBounds("Bestand", g);
        
        int bestandBreite = (int)(bestandRahmen.getWidth());
        int tagHoehe = (int)(tagRahmen.getHeight());
        int tagBreite = (int)(tagRahmen.getWidth());
        int abstandY = 20; //abstand zum Fensterrahmen
        
        int laengeX = width - bestandBreite - 45;
        int schrittX = laengeX / tage; //abstände zwischen den Tagen
        int laengeY = height - abstandY - 30;
        int schrittY = schrittBestand = laengeY / bestandMax; //abstände zwischen den Bestands-Etappen
        
        g.drawString("Bestand", 5, 15);
        pixelPositionYBestandNull = laengeY + abstandY - 1; //direkt Null beim Graphen
        g.fillRect(bestandBreite + 10, abstandY, 3, laengeY); //zeichnet die dicke Linie für die Y-Achse

        g.drawString("Tag", width - 5 - tagBreite, height - tagHoehe + 7);
        g.fillRect(bestandBreite + 10, laengeY + abstandY, laengeX + 20, 3); //dicke Linie für die X-Achse
        
        // x-skala zeichnen
        int schrittYvon = height - tagHoehe - 21;
        int schrittYbis = schrittYvon + 14;
        for (int tag = 1; tag < 31; tag++) {
            int x = tag * schrittX + bestandBreite + 10;
            tageX.add(x);
            g.drawLine(x, schrittYvon, x, schrittYbis);
            String tagStr = String.valueOf(tag);
            g.drawString(tagStr, x - (int)(fm.getStringBounds(tagStr, g).getWidth() / 2), schrittYbis + 15); //zeichnet einen Tag in den Graphen
        }
        
        // y-skala zeichnen
        int schrittXvon = bestandBreite - 5;
        int schrittXbis = schrittXvon + 5;
        for (int bestand = bestandSchritte; bestand <= bestandMax; bestand += bestandSchritte) {
            int y = laengeY - bestand * schrittY + abstandY;
            g.drawLine(schrittXvon, y, schrittXbis, y);
            String bestandStr = String.valueOf(bestand);
            g.drawString(bestandStr, schrittXvon - (int)(fm.getStringBounds(bestandStr, g).getWidth()) - 5, y + 5);
        }
    }

    /**
     * Zeichnet eine leere Fläche mit einer Umrandung um den Graphen zu leeren.
     * @param g
     * @param width
     * @param height
     */
    private void zeichneHintergrund(Graphics g, int width, int height) {
        g.setColor(getBackground());
        g.fillRect(0, 0, width, height);
        g.setColor(getForeground());
        g.drawRect(0, 0, width - 1, height - 1);
    }

    /**
     * Die maximale Anzahl Tage die angezeigt werden soll
     * @return tage
     */
    public int getTage() {
        return tage;
    }

    /**
     * Die maximale Anzahl Tage die angezeigt werden soll
     * @param tage
     */
    public void setTage(int tage) {
        this.tage = tage;
    }
    
    /**
     * setDaten bereitet die Bestandshistorie auf.  Es ist quasi wie ein Verzeichnis, 
     * indem für jedes einzelne Datum Tag und Monat sowie auch den Betrag der jeweiligen Ware hinterlegt wird.
     * (Beispiel: "22-09" => 5, "20-09" => 10, ...)
     * 
     * erhaltene logDaten: 							[WarenLog(15-08,11), WarenLog(20-09,10), WarenLog(22-09,5)]
     * benötigter Zeitraum: 						Heute - 30 Tage (14-08) ... bis Heute (23-09)
     * Zusammengestellte Daten in der Schleife: 	14-08=0, 15-08=11, 16-08=11, 17-08=11, ..., 20-09=10, 21-09=10, 22-09=5, ..., 23-09=5
     * Resultat = Liste aus Beträgen für jeden Tag:	daten = [0, 11, 11, 11, ..., 10, 10, 5, ...]  mit so vielen Einträgen wie Tage angezeigt werden sollen
     * 
     * @param logDaten
     */
    public void setDaten(List<WarenLog> logDaten) {
    	//
        daten.clear();
        Calendar cal = Calendar.getInstance();
        HashMap<String, Integer> datumZuBestand = new HashMap<String, Integer>();
        for (WarenLog l : logDaten) {
            cal.setTime(l.getDate());
            String key = cal.get(Calendar.DATE) + "-" + cal.get(Calendar.MONTH);
            datumZuBestand.put(key, l.getBestand());
        }
        if (logDaten.size() > 0) {
		    int letzterBestand = logDaten.get(0).getBestand();
		    cal = Calendar.getInstance();
		    cal.add(Calendar.DATE, -tage);
		    for (int i = 0; i < tage; i++) {
		        String key = cal.get(Calendar.DATE) + "-" + cal.get(Calendar.MONTH); // "23-09"
		        if (datumZuBestand.containsKey(key)) {
		            letzterBestand = datumZuBestand.get(key);
		        }
		        daten.add(letzterBestand);
		        cal.add(Calendar.DATE, 1);
		    }
        }
    }
}
