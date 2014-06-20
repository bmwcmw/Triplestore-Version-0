package queryObjects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import queryUtils.BiList;
import queryUtils.BiList.BiListPair;
import queryUtils.QueryUtils;
import queryUtils.QueryUtils.VarType;

/**
 * This is used to store parsed SPARQL queries with all sub-queries categorized
 * by their variable type, as well as all relations between variables.
 * @author Cedar
 */
public class ParsedQuery {
	/**
	 * All variables in the SELECT clause
	 */
	private HashSet<String> selectedvariables;
	
	/**
	 * Number of variables and sets of sub-queries with specified number of variables
	 */
	private HashMap<Integer, SubQuerySet> patterns;
	
	/**
	 * All variables with the set containing all sub-queries having this variable
	 */
	private HashMap<QueryVariable, HashSet<Integer>> variables;
	
	public ParsedQuery(){
		patterns = new HashMap<Integer, SubQuerySet>();
		variables = new HashMap<QueryVariable, HashSet<Integer>>();
		selectedvariables = new HashSet<String>();
	}
	
	public void addVariable(String var){
		selectedvariables.add(var);
	}
	
	public void putPattern(StringPattern p){
//		int varType = 0;
		/*
		 * Size = number of variables, Content = variables in current pattern
		 */
		BiList varList = new BiList();
//		VarType type;
		if (QueryUtils.isVariable(p.getS())){
//			varType = varType + 1;
			varList.add(VarType.S, p.getS());
		}
		if (QueryUtils.isVariable(p.getP())){
//			varType = varType + 2;
			varList.add(VarType.P, p.getP());
		}
		if (QueryUtils.isVariable(p.getO())){
//			varType = varType + 4;
			varList.add(VarType.O, p.getO());
		}
//		switch(varType){
//			case 0:
//				type = VarType.NON;
//				break;
//			case 1:
//				type = VarType.S;
//				break;
//			case 2:
//				type = VarType.P;
//				break;
//			case 3:
//				type = VarType.SP;
//				break;
//			case 4:
//				type = VarType.O;
//				break;
//			case 5:
//				type = VarType.SO;
//				break;
//			case 6:
//				type = VarType.PO;
//				break;
//			case 7:
//				type = VarType.SPO;
//				break;
//		}
		
		SubQuerySet dest = patterns.get(varList.size());
		if(dest==null){
			dest = new SubQuerySet();
			patterns.put(varList.size(), dest);
		}
		Integer id = dest.putStringPattern(p);
		
		/*
		 * For all variables
		 */
		for(int i=0; i<varList.size(); i++){
			BiListPair tempVar = varList.get(i);
			QueryVariable tempKey = new QueryVariable((VarType)tempVar.elem1(), (String)tempVar.elem2());
			if(!variables.containsKey(tempKey))
				variables.put(tempKey, new HashSet<Integer>());
			variables.get(tempKey).add(id);
		}
	}
	
	public String toString(){
		String returns = "SELECT : \n"
				+ selectedvariables.toString() + "\n"
				+ "WHERE : \n";
		for(Entry<Integer, SubQuerySet> e : patterns.entrySet()){
			returns = returns + "with (" + e.getKey() + ") variable(s) \n";
			returns = returns + e.getValue().getAll().toString() + "\n";
		}
		returns += "All Variables in WHERE : \n";
		for(Entry<QueryVariable, HashSet<Integer>> e : variables.entrySet()){
			returns = returns + e.getKey().toString() + " "
					+ e.getValue().toString() + "\n";
		}
		return returns;
	}
}