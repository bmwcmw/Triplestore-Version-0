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
		int varType = 0;
		int nbvar = 0;
		VarType type;
		if (QueryUtils.isVariable(p.getS())){
			varType = varType + 1;
			nbvar++;
		}
		if (QueryUtils.isVariable(p.getP())){
			varType = varType + 2;
			nbvar++;
		}
		if (QueryUtils.isVariable(p.getO())){
			varType = varType + 4;
			nbvar++;
		}
		switch(varType){
			case 0:
				type = VarType.NON;
				break;
			case 1:
				type = VarType.S;
				break;
			case 2:
				type = VarType.P;
				break;
			case 3:
				type = VarType.SP;
				break;
			case 4:
				type = VarType.O;
				break;
			case 5:
				type = VarType.SO;
				break;
			case 6:
				type = VarType.PO;
				break;
			case 7:
				type = VarType.SPO;
				break;
		}
		SubQuerySet dest = patterns.get(nbvar);
		if(dest==null){
			dest = new SubQuerySet();
			patterns.put(nbvar, dest);
		}
		Integer id = dest.putStringPattern(p);
		
		//for all variables
		if(selectedvariables.containsKey(key))
		selectedvariables.put(new QueryVariable(VarType.S, p.getS()), id);
	}
}