package bib.local.domain.exceptions;
/**
 * Wird geworfen falls eine Person nicht existiert
 * @author Florian
 *
 */
public class PersonExistiertNichtException extends Exception{
	public PersonExistiertNichtException(){
		super("Die angegebene Person existiert nicht");
	}

}
