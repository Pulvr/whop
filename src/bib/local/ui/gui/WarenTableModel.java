package bib.local.ui.gui;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


import bib.local.valueobjects.MassengutWare;
import bib.local.valueobjects.Ware;

public class WarenTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;
    
    private Vector<String> columnNames;
	private Vector<Vector<String>> data;
	// Das DecimalFormat ist ein Member der WarenTableModel Klasse um in updateDataVector wiederverwendet zu werden.
	private DecimalFormat df = new DecimalFormat("#,##0.00");

	
	public WarenTableModel(List<Ware> waren) {
		super();
		
		columnNames = new Vector<String>();
		columnNames.add("Waren Nummer");
		columnNames.add("Titel");
		columnNames.add("Bestand");
        columnNames.add("Preis");
        columnNames.add("Pack.Gr.");
        columnNames.add("Pack.Preis");
		
		data = new Vector<Vector<String>>();
		updateDataVector(waren);
	}

	public void updateDataVector(List<Ware> waren) {
		data.clear();
		
		for (Ware w: waren) {
			Vector<String> warenVector = new Vector<String>();
			// String.valueOf() anstatt zahl+"" um Laufzeitoptimierungen der Java VM auszunutzen
			// String + String ist etwas rechenintensiver als ein einfaches valueOf()
			warenVector.add(String.valueOf(w.getNummer()));
			warenVector.add(String.valueOf(w.getBezeichnung()));
			warenVector.add(String.valueOf(w.getBestand()));
			warenVector.add(df.format(w.getPreis()) + " €");
			// die Warenlist beinhaltet auch Massengutwaren und muss daher auf diesen Typ geprüft werden
			if (w instanceof MassengutWare) {
			    // (MassengutWare) w    ist ein sog. Type-Cast um Java zu zwingen die Variable "w" als MassengutWare zu behandeln.
			    MassengutWare mw = (MassengutWare) w;
                warenVector.add(String.valueOf(mw.getPackungsGroesse()));
                warenVector.add(String.valueOf(df.format(mw.getRechnungsPreis()) + " €"));
			} else {
                warenVector.add("");
                warenVector.add("");
			}
			data.add(warenVector);
		}
		setDataVector(data, columnNames);
	}
	
	@Override
	/**
	 * Klasse von javax.swing.JTable Ã¼berschrieben um das editieren aller Zellen zu verbieten
	 */
    public boolean isCellEditable(int row, int column) {
       return false;
    }
}

