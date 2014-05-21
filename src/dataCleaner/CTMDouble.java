package dataCleaner;

/**
 * Valid S,O pair for POS
 * 
 * @author Cedar
 *
 */
public class CTMDouble implements SPOObject{
	
	private CTMSubject _subject;
	private CTMObject _object;
	
	public CTMDouble(String subject, String object){
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
