package dataCleaner.token;


/**
 * <p><i>Token</i></p>
 * <p>Internationalized Resource Identifier</p>
 * <p>Basically the same thing as a URI, except it's a generalization, 
 * which means it may contain non-Latin alphabet characters from the Universal Character Set (Unicode/ISO 10646), 
 * such as Greek letters and Chinese characters.</p>
 * 
 * @author CMWT420
 *
 */
public class IRI extends Token {

	private String _iri = null;
	private boolean _hasprefix = false;
	private String _prefix = null;

	IRI(String iri){
		if(iri.startsWith("<") && iri.endsWith(">")){
			_hasprefix = false;
			_iri = iri;
		}
		else {
			_hasprefix = true;
			String split[] = iri.split(":");
			if(split.length != 2){
				System.out.println("Error : " + iri);
				_hasprefix = false;
				_iri = iri;
			}
			else {
				_prefix = split[0] + ":";
				_iri = split[1];
			}
		}
	}

	public String getIri(){
		return _iri;
	}
	
	public boolean hasPrefix(){
		return _hasprefix;
	}
	
	public String getPrefix(){
		return _prefix;
	}

	@Override
	public boolean isIRI() {
		return true;
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
		return false;
	}

	@Override
	public IRI getIRI() {
		return this;
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
		return null;
	}

	@Override
	public String toString(){
		if(_hasprefix){
			return _prefix + _iri;
		}
		else {
			return _iri;
		}
	}

}
