package bib.local.domain.exceptions;

import bib.local.valueobjects.Person;

/**
 * Exception zur Signalisierung, dass eine person bereits existiert (z.B. bei einem Einfügevorgang).
 * -wird momentan nicht verwendet
 */
public class PersonExistiertBereitsException extends Exception {
	
	public PersonExistiertBereitsException(Person einePerson, String zusatzMsg) {
		super("Ware mit Bezeichnung und Nummer  existiert bereits");
	
	}
}
