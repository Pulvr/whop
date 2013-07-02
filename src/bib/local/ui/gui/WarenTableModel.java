package bib.local.ui.gui;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import bib.local.valueobjects.Ware;

public class WarenTableModel extends DefaultTableModel {

	private Vector<String> columnNames;
	private Vector<Vector<String>> data;
	
	public WarenTableModel(List<Ware> waren) {
		super();
		
		columnNames = new Vector<String>();
		columnNames.add("Titel");
		columnNames.add("Preis");
		columnNames.add("Bestand");
		columnNames.add("Artikelnummer");
		
		data = new Vector<Vector<String>>();
		updateDataVector(waren);
	}

	public void updateDataVector(List<Ware> waren) {
		data.clear();
		
		for (Ware w: waren) {
			Vector<String> warenVector = new Vector<String>();
			warenVector.add(w.getBezeichnung());
			warenVector.add(w.getPreis()+" €");
			warenVector.add(w.getBestand()+"");
			warenVector.add(w.getNummer()+"");
			data.add(warenVector);
		}
		setDataVector(data, columnNames);
	}
}
