package dataCleaner;


/**
 * <p>Light version of RDF Triple</p>
 * <p>An RDF Pair String contains two strings : Subject and Object</p>
 * 
 * @author Cedar
 *
 */
public class RDFPairStr {

	private String _subject;
	private String _object;
	
	/**
	 * <p>Create a CTMPairStr directly using three strings.</p>
	 */
	public RDFPairStr(String subject, String object){
		_subject = subject;
		_object = object;
	}
	
	public String getSubject() {
		return _subject;
	}

	public String getObject() {
		return _object;
	}
	
	/**
	 * @return S and O separated by whitespace
	 */
	public String toString(){
		return _subject.toString() + " " + _object.toString();
	}
	
}
