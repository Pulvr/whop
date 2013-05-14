package bib.local.valueobjects;

import java.util.HashMap;
import java.util.Map;

public class Warenkorb {
	private Map<Ware, Integer> warenListe;
	
	public Warenkorb(){
		warenListe = new HashMap<Ware, Integer>();
	}
}
