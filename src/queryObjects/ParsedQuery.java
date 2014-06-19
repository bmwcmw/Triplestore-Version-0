package queryObjects;

import java.util.ArrayList;
import java.util.HashMap;

import queryUtils.QueryUtils;
import queryUtils.QueryUtils.VarType;

/**
 * This is used to store parsed SPARQL queries with all sub-queries categorized
 * by their variable type, as well as all relations between variables.
 * @author Cedar
 */
public class ParsedQuery {
	/**
	 * Number of variables and sets of sub-queries with specified number of variables
	 */
	private HashMap<Integer, SubQuerySet> patterns;
	private HashMap<QueryVariable, ArrayList<Integer>> selectedvariables;
	
	public ParsedQuery(){}
	
	public void putPattern(StringPattern p){
		if (QueryUtils.isVariable(p.getS())) 
			patterns.add(p)
			return id of pattern
			selectedvariables.put(new QueryVariable(VarType.S, p.getS()), id of pattern)
		if (QueryUtils.isVariable(p.getP())) 
			countVar += "P";
		if (QueryUtils.isVariable(p.getO())) 
			countVar += "O";
	}
}