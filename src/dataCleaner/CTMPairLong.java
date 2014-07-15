package dataCleaner;


/**
 * <p>A CTM Pair Long contains two long values : Subject and Object</p>
 * 
 * @author Cedar
 *
 */
public class CTMPairLong implements SPOObject{

	private Long _subject;
	private Long _object;
	
	/**
	 * <p>Create a CTMPairLong directly using two longs.</p>
	 */
	public CTMPairLong(Long subject, Long object){
		_subject = subject;
		_object = object;
	}
	
	public Long getSubject() {
		return _subject;
	}

	public Long getObject() {
		return _object;
	}
	
	/**
	 * @return S and O separated by whitespace
	 */
	public String toString(){
		return _subject.toString() + " " + _object.toString();
	}
	
}
