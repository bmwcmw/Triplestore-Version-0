package dataCleaner;

/**
 * Valid S,O pair for POS
 * 
 * @author Cedar
 *
 */
public class CTMPair implements SPOObject{
	
	private CTMSubject _subject;
	private CTMObject _object;
	
	public CTMPair(String subject, String object){
		_subject = new CTMSubject(subject);
		_object = new CTMObject(object);
	}

	
	public CTMSubject getSubject() {
		return _subject;
	}

	public CTMObject getObject() {
		return _object;
	}
}
