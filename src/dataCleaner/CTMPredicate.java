package dataCleaner;

import dataCleaner.token.IRI;
import dataCleaner.token.Token;
import dataCleaner.token.Variable;


/**
 * <p>A predicate of CTM Triple can be created : </p>
 * - by parsing a string
 * <br>
 * - from an IRI object
 * <br>
 * - from a variable object
 * 
 * @author Cedar
 *
 */
public class CTMPredicate {
	
	private Token _predicate;
	
	/**
	 * Create a CTMPredicate by parsing a string
	 */
	public CTMPredicate(String predicate){
		_predicate = Token.parse(predicate); //new IRI(predicate);
	}
	
	/**
	 * Create an CTMPredicate using an IRI
	 */
	public CTMPredicate(IRI iri){
		_predicate = iri;
	}
	
	/**
	 * Create an CTMPredicate using a variable
	 */
	public CTMPredicate(Variable variable){
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
