package bib.local.domain.exceptions;

/**
 * Falls nach eine Ware nicht existiert sollte diese Exception geworfen werden
 * 
 * @author Florian
 *
 */

public class WareExistiertNichtException extends Exception{
	public WareExistiertNichtException(){
		super("Diese Ware existiert nicht");
	}
}
