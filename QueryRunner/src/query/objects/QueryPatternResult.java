package query.objects;

import java.util.ArrayList;

/**
 * Result set of a query pattern, with n lists where n is the number of 
 * variable(s) in the pattern.
 * @author Cedar
 *
 */
public class QueryPatternResult {
	
	/* Pattern with type */
	private StringPattern pattern;
	
	private ArrayList<ArrayList<String>> resultSet;
	
	public QueryPatternResult(StringPattern pat, 
			ArrayList<ArrayList<String>> set){
		pattern = pat;
		resultSet = set;
	}
	
	public int nbVariable(){
		return resultSet.size();
	}
	
	public StringPattern getPattern(){
		return pattern;
	}
	
	public ArrayList<ArrayList<String>> getResultSet(){
		return resultSet;
	}

}
