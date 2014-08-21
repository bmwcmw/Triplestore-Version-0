package dataCleaner;

/**
 * Valid S,O pair for POS
 * 
 * @author Cedar
 *
 */
public class RDFPair {
	
	private RDFSubject _subject;
	private RDFObject _object;
	
	public RDFPair(String subject, String object){
		_subject = new RDFSubject(subject);
		_object = new RDFObject(object);
	}

	
	public RDFSubject getSubject() {
		return _subject;
	}

	public RDFObject getObject() {
		return _object;
	}
}
