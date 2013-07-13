package bib.local.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.Vector;

import bib.local.valueobjects.WarenLog;


public class LogPersistenceManager {
	
	private SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
	private GregorianCalendar cal = new GregorianCalendar();
	
	public void writeLog(File datei, String text) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(datei, true)));
		writer.write(text);
		writer.newLine();
		writer.close();
	}
	
	public Vector<WarenLog> readLog(File datei, String bezeichnung, int daysInPast) throws IOException, ParseException{
		
		Vector<String> log = new Vector<String>();
		Vector<Date> dates = new Vector<Date>();
		Vector<String> bezeichnungen = new Vector<String>();
		Vector<Integer> warenNummern = new Vector<Integer>();
		Vector<Float> preise = new Vector<Float>();
		Vector<Integer> bestaende = new Vector<Integer>();
		Vector<WarenLog> warenLogTmp = new Vector<WarenLog>();
		Vector<WarenLog> warenLogUebereinstimmendeBz = new Vector<WarenLog>();
		Vector<WarenLog> warenLog = new Vector<WarenLog>();
		
		Date today = new Date();
		
		this.cal.setTime(today);
		
		if(daysInPast > 0){
			this.cal.add(Calendar.DAY_OF_YEAR, -daysInPast);
		} else {
			this.cal.add(Calendar.DAY_OF_YEAR, daysInPast);
		}
		
		Scanner reader = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(datei)))).useDelimiter("\n");
		
		while(reader.hasNext()){
			log.add(reader.next());
		}
		
		reader.close();
		
		for(int i = 0; i < log.size(); i+=6){
			dates.add(this.ft.parse(log.elementAt(i)));
		}
		
		for(int i = 1; i < log.size(); i+=6){
			bezeichnungen.add(log.elementAt(i));
		}
		
		for(int i = 2; i < log.size(); i+=6){
			warenNummern.add(Integer.parseInt(log.elementAt(i)));
		}
		
		for(int i = 3; i < log.size(); i+=6){
			preise.add(Float.parseFloat(log.elementAt(i)));
		}
		
		for(int i = 4; i < log.size(); i+=6){
			bestaende.add(Integer.parseInt(log.elementAt(i)));
		}
		
		for(int i = 0; i < dates.size(); i++){
			warenLogTmp.add(new WarenLog(dates.elementAt(i), bezeichnungen.elementAt(i), warenNummern.elementAt(i), preise.elementAt(i), bestaende.elementAt(i)));
		}
		
		for(int i = 0; i < warenLogTmp.size(); i++){
			if(warenLogTmp.elementAt(i).getBezeichnung().equals(bezeichnung)){
				warenLogUebereinstimmendeBz.add(warenLogTmp.elementAt(i));
			}
		}
		
		for(int i = 0; i < warenLogUebereinstimmendeBz.size() - 1; i++){
			if(!warenLogUebereinstimmendeBz.elementAt(i).getDate().toString().equals(warenLogUebereinstimmendeBz.elementAt(i+1).getDate().toString())){
				if(warenLogUebereinstimmendeBz.elementAt(i).getDate().after(this.cal.getTime()) || warenLogUebereinstimmendeBz.elementAt(i).getDate().equals(this.cal.getTime())){
					warenLog.add(warenLogUebereinstimmendeBz.elementAt(i));
				}
			}
		}
		
		warenLog.add(warenLogUebereinstimmendeBz.elementAt(warenLogUebereinstimmendeBz.size() - 1));
		
		
		
		return warenLog;
	}

}
