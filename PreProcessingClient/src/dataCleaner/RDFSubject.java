package dataCleaner;

import dataCleaner.token.Anonymous;
import dataCleaner.token.IRI;
import dataCleaner.token.Token;
import dataCleaner.token.Variable;


/**
 * <p>A subject of CTM Triple can be created : </p>
 * - by parsing a string
 * <br>
 * - from an IRI object
 * <br>
 * - from a variable object
 * <br>
 * - from an anonymous object
 * 
 * @author CMWT420
 *
 */
public class RDFSubject {

	private Token _subject = null;
	
	/**
	 * Create a CTMSubject by parsing a string
	 */
	public RDFSubject(String subject){
		_subject = Token.parse(subject); //new IRI(subject);
	}
	
	/**
	 * Create an CTMSubject using an IRI
	 */
	public RDFSubject(IRI iri){
		_subject = iri;
	}
	
	/**
	 * Create an CTMSubject using a variable
	 */
	public RDFSubject(Variable variable){
		_subject = variable;
	}
	
	/**
	 * Create an CTMSubject using an anonymous
	 */
	public RDFSubject(Anonymous anonymous){
		_subject = anonymous;
	}
	
	public boolean isIRI() {
		return _subject.isIRI();
	}

	public boolean isAnonymous() {
		return _subject.isAnonymous();
	}

	public boolean isVariable() {
		return _subject.isVariable();
	}

	public IRI getIRI() {
		return _subject.getIRI();
	}

	public Variable getVariable() {
		return _subject.getVariable();
	}

	public Anonymous getAnonymous() {
		return _subject.getAnonymous();
	}

	public String toString(){
		return _subject.toString();
	}
}
