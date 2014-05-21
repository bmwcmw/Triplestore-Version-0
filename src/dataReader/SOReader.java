package dataReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;

import dataCleaner.CTMDoubleStr;
import dataCleaner.CTMDouble;

/**
 * <p>
 * N3 triples reader
 * </p>
 * 
 * @author Cedar
 * 
 */
public class SOReader implements TripleReader {

	private String filePath = null;
	private BufferedReader reader = null;
	private int lineNumber = 0;

	/**
	 * @param dataManager : normally this reader is called by a dataManager, in this 
	 * case, use <tt>this</tt>
	 * @param fileName : specify the filename of a NTriples file, normally with the 
	 * extension <tt>.n3</tt>
	 * @param invalidPath : specify invalid log's location
	 * @throws FileNotFoundException
	 */
	public SOReader(String filePath)
			throws IOException {
		this.filePath = filePath;
		reader = new BufferedReader(new FileReader(filePath));
	}
	

	/**
	 * <p>
	 * Reads line by line from predicate files , then parse the current line
	 * into a <i>CTMDouble</i> object(S,O)
	 * </p>
	 * 
	 * <b>Since S, P, O are validated during PS, we don't need to re-validate them again.</b>
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public CTMDouble next() throws IOException, ParseException {
		String line;
		while ((line = reader.readLine()) != null) {
			++lineNumber;
			StringTokenizer itr = new StringTokenizer(line);
			// at least two tokens
			// One - subject
			if (itr.hasMoreTokens()) {
				String subj = itr.nextToken();
				if (!subj.startsWith("#")) {
					// Two - object
					if (!itr.hasMoreTokens()) {
						throw new ParseException(filePath, lineNumber);
					}
					String object = itr.nextToken();
					return new CTMDouble(subj, object);
				}
			}
		}
		return null;
	}
	
	
	/**
	 * <p>
	 * Reads line by line from predicate files , then parse the current line
	 * into a <i>CTMDoubleStr</i> object(S,O)
	 * </p>
	 * 
	 * <b>Since S, P, O are validated during PS, we don't need to re-validate them again.</b>
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public CTMDoubleStr nextStr() throws IOException, ParseException {
		String line;
		while ((line = reader.readLine()) != null) {
			++lineNumber;
			StringTokenizer itr = new StringTokenizer(line);
			// at least two tokens
			// One - subject
			if (itr.hasMoreTokens()) {
				String subj = itr.nextToken();
				if (!subj.startsWith("#")) {
					// Two - object
					if (!itr.hasMoreTokens()) {
						throw new ParseException(filePath, lineNumber);
					}
					String object = itr.nextToken();
					return new CTMDoubleStr(subj, object);
				}
			}
		}
		return null;
	}

	/**
	 * Closes the BufferedReader while finished
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		reader.close();
	}

	@Override
	public void reset() {
		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
		}
	}
}
