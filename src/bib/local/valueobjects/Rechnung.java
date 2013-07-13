package bib.local.valueobjects;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;


public class Rechnung {
	
	//private GregorianCalendar gcal = new GregorianCalendar();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Date date;
	private String dateStr;
	
	private String rechnungsNr;
	
	private float gSumme = 0.0F;
	

	private DecimalFormat df = new DecimalFormat("0.00");

	private Person p;
	
	public Rechnung (Person person){
		this.dateStr = this.sdf.format(new Date());
		this.p =person;
	}
	
	public String toString(){
		
		String output = "";
		//output += "Rechnungs-Nummer: "+ getRechnungsNummer()+ " am " +dateStr+ " \n";
		output += this.dateStr + "\n";
		output += p.getAnrede()+" \n";
		output += p.getName()+" \n";
		output += p.getStrasse()+" \n";
		output += p.getPlz()+" "+p.getWohnort()+ " \n";
		output += "*-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-* \n";
		for(int i = 0; i < p.getWarenkorb().size(); i++){
			output += p.getWarenkorb().elementAt(i)+"\n";
			this.gSumme += p.getWarenkorb().elementAt(i).getPreis();
		}
		output += "Gesamtpreis: " + df.format(this.gSumme) + "€";
		
		return output;
	}
}
