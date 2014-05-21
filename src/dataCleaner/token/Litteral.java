package dataCleaner.token;


/**
 * <p><i>Token</i></p>
 * <p>Litteral</p>
 * <p>A direct value, such as a string (e.g. a name: "Bob"), a number, a date, something like that. 
 * RDF statements typically consist of a combination of URIs and literals.</p>
 * 
 * @author Cedar
 *
 */
public class Litteral extends Token {

	private String _litteral = null;
	
	Litteral(String litteral){
		_litteral = litteral;
	}
	
	@Override
	public boolean isIRI() {
		return false;
	}

	@Override
	public boolean isAnonymous() {
		return false;
	}

	@Override
	public boolean isLitteral() {
		return true;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public IRI getIRI() {
		return null;
	}

	@Override
	public Litteral getLitteral() {
		return this;
	}

	@Override
	public Variable getVariable() {
		return null;
	}

	@Override
	public Anonymous getAnonymous() {
		return null;
	}

	@Override
	public String toString(){
		return _litteral;
	}
	
}

