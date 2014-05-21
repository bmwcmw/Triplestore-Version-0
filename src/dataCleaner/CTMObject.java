package dataCleaner;

import dataCleaner.token.Anonymous;
import dataCleaner.token.IRI;
import dataCleaner.token.Litteral;
import dataCleaner.token.Token;
import dataCleaner.token.Variable;


/**
 * <p>An object of CTM Triple can be created : </p>
 * - by parsing a string
 * <br>
 * - from an IRI object
 * <br>
 * - from a variable object
 * <br>
 * - from a litteral object
 * <br>
 * - from an anonymous object
 * 
 * @author Cedar
 *
 */
public class CTMObject {

	private Token _object;
	
	/**
	 * Create a CTMObject by parsing a string
	 */
	public CTMObject(String object){
		_object = Token.parse(object);// new IRI(object);
	}
	
	/**
	 * Create an CTMObject using an IRI
	 */
	public CTMObject(IRI iri){
		_object = iri;
	}

	/**
	 * Create an CTMObject using an anonymous
	 */
	public CTMObject(Anonymous anonymous){
		_object = anonymous;
	}
	
	/**
	 * Create an CTMObject using a litteral
	 */
	public CTMObject(Litteral litteral){
		_object = litteral;
	}
	
	/**
	 * Create an CTMObject using a variable
	 */
	public CTMObject(Variable variable){
		_object = variable;
	}
	
	public boolean isIRI() {
		return _object.isIRI();
	}

	public boolean isAnonymous() {
		return _object.isAnonymous();
	}

	public boolean isLitteral() {
		return _object.isLitteral();
	}

	public boolean isVariable() {
		return _object.isVariable();
	}

	public IRI getIRI() {
		return _object.getIRI();
	}

	public Litteral getLitteral() {
		return _object.getLitteral();
	}

	public Variable getVariable() {
		return _object.getVariable();
	}

	public Anonymous getAnonymous() {
		return _object.getAnonymous();
	}

	public String toString(){
		return _object.toString();
	}
}
