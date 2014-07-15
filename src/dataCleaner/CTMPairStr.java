package dataCleaner;


/**
 * <p>Light version of CTM Triple</p>
 * <p>A CTM Pair String contains two strings : Subject and Object</p>
 * 
 * @author Cedar
 *
 */
public class CTMPairStr implements SPOObject{

	private String _subject;
	private String _object;
	
	/**
	 * <p>Create a CTMPairStr directly using three strings.</p>
	 */
	public CTMPairStr(String subject, String object){
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
