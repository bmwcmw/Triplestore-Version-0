package data.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;

import data.cleaner.RDFPair;
import data.cleaner.RDFPairStr;

/**
 * <p>
 * N3 triples reader
 * </p>
 * 
 * @author CMWT420
 * 
 */
public class PairReader implements RDFN3BasicReader {

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
	public PairReader(String filePath)
			throws IOException {
		this.filePath = filePath;
		reader = new BufferedReader(new FileReader(filePath));
	}
	
	/**
	 * @param dataManager : normally this reader is called by a dataManager, in this 
	 * case, use <tt>this</tt>
	 * @param file : specify the input File object of a NTriple file
	 * extension <tt>.n3</tt>
	 * @param invalidPath : specify invalid log's location
	 * @throws FileNotFoundException
	 */
	public PairReader(File file)
			throws IOException {
		this.filePath = file.getAbsolutePath();
		reader = new BufferedReader(new FileReader(file));
	}
	

	/**
	 * <p>
	 * Reads line by line from predicate files , then parse the current line
	 * into a <i>Long pair</i> object(S,O)
	 * </p>
	 * 
	 * <b>Since S, P, O are validated during PS, we don't need to re-validate them again.</b>
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public RDFPair next() throws IOException, ParseException {
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
					return new RDFPair(subj, object);
				}
			}
		}
		return null;
	}
	
	
	/**
	 * <p>
	 * Reads line by line from predicate files , then parse the current line
	 * into a <i>String pair</i> object(S,O)
	 * </p>
	 * 
	 * <b>Since S, P, O are validated during PS, we don't need to re-validate them again.</b>
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public RDFPairStr nextStr() throws IOException, ParseException {
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
					return new RDFPairStr(subj, object);
				}
			}
		}
		return null;
	}
	
	
	/**
	 * <p>
	 * Reads line by line from a file, returns a String
	 * </p>
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public String nextLine() throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			return line;
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
