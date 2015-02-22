package dataCleaner;


/**
 * <p>Light version of CTM Triple</p>
 * <p>A CTM String Triple contains three strings : Subject, Predicate and Object</p>
 * 
 * @author Cedar
 *
 */
public class RDFTriple {

	private String _subject;
	private String _predicate;
	private String _object;
	
	/**
	 * <p>Create a CTMTriple directly using three strings.</p>
	 */
	public RDFTriple(String subject, String predicate, String object){
		_subject = subject;
		_predicate = predicate;
		_object = object;
	}
	
	public String getSubject() {
		return _subject;
	}

	public String getPredicate() {
		return _predicate;
	}

	public String getObject() {
		return _object;
	}
	
	/**
	 * @return S, P and O separated by whitespace
	 */
	public String toString(){
		return _subject.toString() + " " + _predicate.toString() + " " + _object.toString();
	}
	
}
