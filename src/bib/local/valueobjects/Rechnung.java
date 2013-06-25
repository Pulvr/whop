package bib.local.valueobjects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Rechnung {
	
	private GregorianCalendar gcal = new GregorianCalendar();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private Date date = gcal.getTime();
	private String dateStr = sdf.format(date);
	
	private String rechnungsNr;
	
	private float gSumme = 0.0F;
	
	private Person p = null;
	private HashMap<String, Ware>warenListe = null;
	
	public Rechnung (Person person, HashMap<String, Ware> warenListe){
		this.p = person;
		this.warenListe = warenListe;
	}
	
}
