package queryPlanner;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import queryObjects.ParsedQuery;
import queryObjects.StringPattern;
import queryObjects.SubQueryPatternSet;
import queryUtils.InvalidPatternException;

/**
 * This is the query planner which splits SPARQL queries into sub-queries by the
 * number of variable(s).
 * @author CEDAR
 */
public class SimpleQueryPlanner {
	
	/**
	 * Splits an string into chunks by type of variables
	 * @param triplepatterns
	 * @return a Graph with detailed information
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
		
		/* DEBUG */
		System.out.println("PARSED------------");
		for(String sel : parsed.getSelect()){
			System.out.print(sel + "\t");
		}
		System.out.println("\n------------------");
		for(Entry<Integer, SubQueryPatternSet> pat : parsed.getPatterns().entrySet()){
			System.out.print(pat.toString());
		}
		System.out.println("PARSED------------");
		
		return parsed;
	}
	
}
