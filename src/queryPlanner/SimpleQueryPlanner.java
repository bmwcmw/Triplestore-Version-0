package queryPlanner;

import java.util.LinkedList;

import queryObjects.StringPattern;
import queryUtils.SubQuerySet;
import queryUtils.QueryUtils;
import queryUtils.QueryUtils.VarType;

public class SimpleQueryPlanner {
	
	/**
	 * Splits a list of patterns into chunks by type of variables
	 * @param triplepatterns
	 * @return a Graph
	 */
	public static SubQuerySet plan(LinkedList<StringPattern> triplepatterns){
		StringPattern p;
		String countVar = "";
		SubQuerySet set = new SubQuerySet();
		while((p = triplepatterns.getFirst()) != null){
			if (QueryUtils.isVariable(p.getS())) 
				countVar += "S";
			if (QueryUtils.isVariable(p.getP())) 
				countVar += "P";
			if (QueryUtils.isVariable(p.getO())) 
				countVar += "O";
			set.addPattern(VarType.valueOf(countVar), p);
			countVar = "";
			triplepatterns.removeFirst();
		}
		return set;
	}
	
}
