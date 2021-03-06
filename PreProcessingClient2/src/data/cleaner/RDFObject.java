package data.cleaner;

import data.cleaner.token.Anonymous;
import data.cleaner.token.IRI;
import data.cleaner.token.Litteral;
import data.cleaner.token.Token;
import data.cleaner.token.Variable;


/**
 * <p>An object of  Triple can be created : </p>
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
 * @author CMWT420
 *
 */
public class RDFObject {

	private Token _object;
	
	/**
	 * Create a Object by parsing a string
	 */
	public RDFObject(String object){
		_object = Token.parse(object);// new IRI(object);
	}
	
	/**
	 * Create an Object using an IRI
	 */
	public RDFObject(IRI iri){
		_object = iri;
	}

	/**
	 * Create an Object using an anonymous
	 */
	public RDFObject(Anonymous anonymous){
		_object = anonymous;
	}
	
	/**
	 * Create an Object using a litteral
	 */
	public RDFObject(Litteral litteral){
		_object = litteral;
	}
	
	/**
	 * Create an Object using a variable
	 */
	public RDFObject(Variable variable){
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
