package bib.local.domain.exceptions;

/**
 * Falls nach eine Ware verlangt wird die nicht existieren sollte
 * 
 * @author Florian
 *
 */

public class WareExistiertNichtException extends Exception{
	
	public WareExistiertNichtException(){
		
		super("Diese Ware existiert nicht");
		
	}
	
}
