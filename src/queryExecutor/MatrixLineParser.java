package queryExecutor;

import java.text.ParseException;
import java.util.HashSet;

/**
 * Parses a line of compressed matrix to a hashset containing numerical indexes.
 * @author Cedar
 */
public class MatrixLineParser {
	
	/**
	 * Parses a line of compressed matrix to a hashset containing numerical indexes.
	 * @param line of compressed matrix
	 * @return a hashset
	 * @throws ParseException When the string line is not correctly formatted
	 */
	public static HashSet<Long> parseMatrixLine(String line) throws ParseException{
		HashSet<Long> tempSet = new HashSet<Long>();
		//4:[1]1,1,1,2,2
		String nbEntry = line.substring(0, line.indexOf(":"));
		String begin = line.substring(0, line.indexOf("]"))
				.substring(line.indexOf("[") + 1);
		String data = line.substring(line.indexOf("]") + 1);
		/* DEBUG */
		System.out.println(nbEntry + " entry(ies) in the line, " +
				"begins by " + begin);
		int currentBegin = 0;
		int currentEnd = data.indexOf(",", currentBegin);
		boolean one = false;
		long lastLength = 0;
		if(begin.equals("1")) one = true;
		else if(!begin.equals("0")) 
			throw new ParseException("Line begins from " + begin, -1);
		while (true) {
			String tempStr = data.substring(currentBegin, currentEnd);
			Long toAdd = Long.valueOf(tempStr);
			/* DEBUG */
			System.out.println("Begin : " + currentBegin + " End : " + currentEnd);
			System.out.println("Length " + lastLength + " plus " + toAdd);
			if (one) {
				for(long i=lastLength;i<(lastLength+toAdd);i++){
					System.out.println("Added "+i);
					tempSet.add(i);
				}
			}
			lastLength += toAdd;
			currentBegin = data.indexOf(",", currentEnd)+1;
			currentEnd = data.indexOf(",", currentBegin);
			one = !one;
			if(currentEnd == -1){
				currentEnd = data.length();
				tempStr = data.substring(currentBegin, currentEnd);
				toAdd = Long.valueOf(tempStr);
				System.out.println("Begin : " + currentBegin + " End : " + currentEnd);
				System.out.println("Length " + lastLength + " plus " + toAdd + "(EOL)");
				if (one) {
					for(long i=lastLength;i<lastLength+toAdd;i++){
						System.out.println("Added "+i);
						tempSet.add(i);
					}
				}
				break;
			}
		}
		/* DEBUG */
		System.out.println("Numerical result set for subquery : "+tempSet.toString());
		return tempSet;
	}

}
