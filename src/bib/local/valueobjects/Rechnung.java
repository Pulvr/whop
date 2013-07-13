package bib.local.valueobjects;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;


public class Rechnung {
	
	private GregorianCalendar gcal = new GregorianCalendar();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Date date = gcal.getTime();
	private String dateStr = sdf.format(date);
	
	private String rechnungsNr;
	
	private float gSumme = 0.0F;
	

	private DecimalFormat df = new DecimalFormat("0.00");

	private Person p = null;
	private Vector<Ware>warenkorbPerson = null;
	
	public Rechnung (Person person, Vector<Ware> warenkorbPerson){
		this.p =person;
		this.warenkorbPerson = warenkorbPerson;
	}
	
	public String toString(){
		
		String output = "";
		output += "Rechnungs-Nummer: "+ getRechnungsNummer()+ " am " +dateStr+ " \n";
		output += p.getAnrede()+" \n";
		output += p.getName()+" \n";
		output += p.getStrasse()+" \n";
		output += p.getPlz()+" "+p.getWohnort()+ " \n";
		output += "*-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-* \n";
		Iterator<Ware> iter = warenkorbPerson.iterator();
			while(iter.hasNext()){
				Ware w = iter.next();
				if (iter.next().equals(iter)){
					int anz = 0;
					anz++;
				}
				//gibWarenRechnung(w,warenkorbPerson.)
			}
		
		return output;
	}
	
	public String gibWarenRechnung(Ware w,int anzahl, float Preis){
		return w.getNummer()+"\t"+w.getBezeichnung()+"\t"+anzahl+"\t"+df.format(w.getPreis());
	}
	
	public String getRechnungsNummer(){
		return rechnungsNr;
	}
}
