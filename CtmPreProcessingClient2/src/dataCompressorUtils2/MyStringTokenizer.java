package dataCompressorUtils2;

import java.util.ArrayList;
import java.util.List;

public class MyStringTokenizer {

	/**
	 * Tokenizes a string into a list.
	 * @param input
	 * @return the list
	 */
	public static List<String> tokenize(char delim, String input) {
		List<String> output = new ArrayList<String>();
		int pos = 0, end;
		while ((end = input.indexOf(delim, pos)) >= 0) {
			output.add(input.substring(pos, end));
			pos = end + 1;
		}
		String last = input.substring(pos, input.length());
		if(last.length()>0)
			output.add(last);
		return output;
	}

}
