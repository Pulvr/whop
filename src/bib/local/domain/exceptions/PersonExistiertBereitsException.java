package bib.local.domain.exceptions;

import bib.local.valueobjects.Person;

public class PersonExistiertBereitsException extends Exception {
	
	public PersonExistiertBereitsException(Person einePerson, String zusatzMsg) {
		super("Ware mit Bezeichnung und Nummer  existiert bereits");
	
	}
}
