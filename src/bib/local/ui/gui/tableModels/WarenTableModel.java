package bib.local.ui.gui.tableModels;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


import bib.local.valueobjects.MassengutWare;
import bib.local.valueobjects.Ware;

/**
 * Ein WarenTableModel zum darstellen der Waren tabelle in der mitte der GUI
 * @author Florian
 *
 */
public class WarenTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;
    
    private Vector<String> columnNames;
	private Vector<Vector<String>> data;
	// Das DecimalFormat ist ein Member der WarenTableModel Klasse um in updateDataVector wiederverwendet zu werden.
	private DecimalFormat df = new DecimalFormat("#,##0.00");

	
	public WarenTableModel(List<Ware> waren) {
		super();
		
		// Die Spalten, in einem Vector gespeichert
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
			warenVector.add(String.valueOf(w.getNummer()));
			warenVector.add(String.valueOf(w.getBezeichnung()));
			warenVector.add(String.valueOf(w.getBestand()));
			warenVector.add(df.format(w.getPreis()) + " €");
			// die Warenlist beinhaltet auch Massengutwaren und muss daher auf diesen Typ geprüft werden
			if (w instanceof MassengutWare) {
			    // (MassengutWare) w um die variable als massengutware zu handlen
				//sollte es sich um eine massengutware handeln werden die letztn beiden zeilen mit "" gefüllt
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
	 * Klasse von javax.swing.JTable Überschrieben um das editieren aller Zellen zu verbieten
	 */
    public boolean isCellEditable(int row, int column) {
       return false;
    }
}

