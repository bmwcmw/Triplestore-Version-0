package dataReader;

import java.io.IOException;
import java.text.ParseException;

import dataCleaner.SPOObject;


/**
 * <p><i>interface</i></p>
 * 
 * @author Cedar
 *
 */
public interface TripleReader {
	/**
	 * Reads triples from a file, line by line, then parse the current line into a <i>CTMTriple</i> object(S,P,O)
	 * @throws IOException
	 * @throws ParseException
	 */
    public SPOObject next() throws IOException, ParseException;
    
	/**
	 * Goes back to the first line of current file
	 */	
    public void reset();
}
