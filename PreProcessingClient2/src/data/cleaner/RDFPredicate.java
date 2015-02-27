package data.cleaner;

import data.cleaner.token.IRI;
import data.cleaner.token.Token;
import data.cleaner.token.Variable;


/**
 * <p>A predicate of CTM Triple can be created : </p>
 * - by parsing a string
 * <br>
 * - from an IRI object
 * <br>
 * - from a variable object
 * 
 * @author CMWT420
 *
 */
public class RDFPredicate {
	
	private Token _predicate;
	
	/**
	 * Create a CTMPredicate by parsing a string
	 */
	public RDFPredicate(String predicate){
		_predicate = Token.parse(predicate); //new IRI(predicate);
	}
	
	/**
	 * Create an CTMPredicate using an IRI
	 */
	public RDFPredicate(IRI iri){
		_predicate = iri;
	}
	
	/**
	 * Create an CTMPredicate using a variable
	 */
	public RDFPredicate(Variable variable){
		_predicate = variable;
	}

	public boolean isIRI() {
		return _predicate.isIRI();
	}

	public boolean isVariable() {
		return _predicate.isVariable();
	}

	public IRI getIRI() {
		return _predicate.getIRI();
	}

	public Variable getVariable() {
		return _predicate.getVariable();
	}

	public String toString(){
		return _predicate.toString();
	}
}
