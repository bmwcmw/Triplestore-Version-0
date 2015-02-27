package dataCleaner.token;


/**
 * <p><i>Token</i></p>
 * <p>Variable</p>
 * <p>It begins with a "?". In SPARQL queries, we use variables (for example, ?x) to denote what we want back in the result. 
 * A query containing variables is evaluated by a SPARQL engine, which means it tries to find values 
 * (URIs or literals) that bind to the variables.</p>
 * 
 * @author CMWT420
 *
 */
public class Variable extends Token {

	private String _variableName = null;
	
	Variable(String name){
		_variableName = name;
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
		return false;
	}

	@Override
	public boolean isVariable() {
		return true;
	}

	@Override
	public IRI getIRI() {
		return null;
	}

	@Override
	public Litteral getLitteral() {
		return null;
	}

	@Override
	public Variable getVariable() {
		return this;
	}

	@Override
	public Anonymous getAnonymous() {
		return null;
	}

	@Override
	public String toString() {
		return "?"+ _variableName;
	}

}
