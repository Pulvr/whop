package bib.local.ui.gui;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import bib.local.valueobjects.Ware;

public class BooksTableModel extends DefaultTableModel {

	private Vector<String> columnNames;
	private Vector<Vector<String>> data;
	
	public BooksTableModel(List<Ware> buecher) {
		super();
		
		columnNames = new Vector<String>();
		columnNames.add("Nummer");
		columnNames.add("Titel");
		columnNames.add("Bestand");
		columnNames.add("Preis");
		//columnNames.add("Verfügbarkeit");
		
		data = new Vector<Vector<String>>();
		updateDataVector(buecher);
	}

	public void updateDataVector(List<Ware> waren) {
		data.clear();
		
		for (Ware w: waren) {
			Vector<String> warenVector = new Vector<String>();
			warenVector.add(w.getNummer()+"");
			warenVector.add(w.getBezeichnung());
			warenVector.add(w.getBestand()+"");
			warenVector.add(w.getPreis()+"");
			//buchVector.add(w.isVerfuegbar() ? "true" : "false");
			data.add(warenVector);
		}
		
		setDataVector(data, columnNames);
	}
}
