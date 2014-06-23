package queryPlanner;

import java.util.HashSet;
import java.util.StringTokenizer;

import queryObjects.ParsedQuery;
import queryObjects.StringPattern;
import queryUtils.InvalidPatternException;

public class SimpleQueryPlanner {
	
	/**
	 * Splits an string into chunks by type of variables
	 * @param triplepatterns
	 * @return a Graph
	 * @throws InvalidPatternException 
	 */
	public static ParsedQuery plan(String sparql) throws InvalidPatternException{
		if (sparql.startsWith("\uFEFF"))
			sparql = sparql.substring(1);
		ParsedQuery parsed = new ParsedQuery();
		
		String select = sparql.substring(sparql.indexOf("SELECT") + 6, sparql.indexOf("WHERE"));
		select = select.replace(" ", "");
		StringTokenizer itrSelect = new StringTokenizer(select, ",");
		while(itrSelect.hasMoreTokens()){
			parsed.addVariable(itrSelect.nextToken());
		}
		
		//Reduce redundancy of sub-queries
		HashSet<String> forWhere = new HashSet<String>();
		String where = sparql.substring(sparql.indexOf("{") + 1, sparql.indexOf("}"));
		StringTokenizer itrWhere = new StringTokenizer(where, ".");
		while(itrWhere.hasMoreTokens()){
			forWhere.add(itrWhere.nextToken());
		}
		for(String str : forWhere){
			parsed.putPattern(new StringPattern(str));
		}
		
		return parsed;
	}
	
}
