package dataCleaner.token;

/**
 * <p><i>Token</i></p>
 * <p>Anonymous</p>
 * <p>Anonymous nodes, blank nodes, or bnodes are synonymous.
 * Anonymous nodes are written like namespaced names, but in the reserved "_" 
 * namespace with an arbitrary local name after the colon (for example, "_:cedarIntern").</p>
 * 
 * @author Cedar
 *
 */
public class Anonymous extends Token {
	public final static String BLANK_NODE_IDENTIFIER = "_";

	private String _value = null;
	
	Anonymous(String blank){
		String split[] = blank.split(":");
		if(split.length != 2){
			System.out.println("Error : " + blank);
			_value = blank;
		}
		else {
			_value = split[1];
		}
	}
	
//	Anonymous(){}

	@Override
	public boolean isIRI() {
		return false;
	}

	@Override
	public boolean isAnonymous() {
		return true;
	}

	@Override
	public boolean isLitteral() {
		return false;
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
		return null;
	}

	@Override
	public Variable getVariable() {
		return null;
	}

	@Override
	public Anonymous getAnonymous() {
		return this;
	}
	
//	@Override
//	public String toString() {
//		return "";
//	}
	
	@Override
	public String toString() {
		return BLANK_NODE_IDENTIFIER+":"+_value;
	}

}
