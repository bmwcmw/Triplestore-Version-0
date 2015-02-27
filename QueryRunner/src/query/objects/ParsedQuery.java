package query.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import objects.BiList;
import objects.BiPair;
import query.utils.QueryUtils;
import query.utils.QueryUtils.VarType;

/**
 * This is used to store parsed SPARQL queries with all sub-queries categorized
 * by their variable type, as well as all relations between variables.
 * @author Cedar
 */
public class ParsedQuery {
	/**
	 * All variables in the SELECT clause
	 */
	private ArrayList<String> selectedvariables;
	
	/**
	 * Number of variables and sets of sub-queries with specified number of variables
	 */
	private HashMap<Integer, SubQueryPatternSet> patterns;
	
	/**
	 * All variables with the set containing all sub-queries having this variable
	 */
	private HashMap<QueryVariable, HashSet<Integer>> variables;
	
	private int idPattern;
	
	public ParsedQuery(){
		selectedvariables = new ArrayList<String>();
		idPattern = 0;
		patterns = new HashMap<Integer, SubQueryPatternSet>();
		variables = new HashMap<QueryVariable, HashSet<Integer>>();
	}
	
	public ArrayList<String> getSelect(){
		return selectedvariables;
	}
	
	public HashMap<Integer, SubQueryPatternSet> getPatterns(){
		return patterns;
	}
	
	public void addVariable(String var){
		selectedvariables.add(var);
	}
	
	public void putPattern(StringTriple p){
		/*
		 * Size = number of variables, Content = variables in current pattern
		 */
		BiList<VarType, String> varList = new BiList<VarType, String>();

		if (QueryUtils.isVariable(p.getS())){
			varList.add(VarType.S, p.getS());
		}
		if (QueryUtils.isVariable(p.getP())){
			varList.add(VarType.P, p.getP());
		}
		if (QueryUtils.isVariable(p.getO())){
			varList.add(VarType.O, p.getO());
		}
		
		SubQueryPatternSet dest = patterns.get(varList.size());
		if(dest==null){
			dest = new SubQueryPatternSet();
			patterns.put(varList.size(), dest);
		}
		dest.putStringPattern(idPattern, p);
		
		/*
		 * For all variables
		 */
		for(int i=0; i<varList.size(); i++){
			@SuppressWarnings("rawtypes")
			BiPair tempVar = varList.get(i);
			QueryVariable tempKey = new QueryVariable((VarType)tempVar.elem1(), (String)tempVar.elem2());
			if(!variables.containsKey(tempKey))
				variables.put(tempKey, new HashSet<Integer>());
			variables.get(tempKey).add(idPattern);
		}
		
		idPattern++;
	}
	
	public String toString(){
		String returns = "SELECT : \n"
				+ selectedvariables.toString() + "\n"
				+ "WHERE : \n";
		for(Entry<Integer, SubQueryPatternSet> e : patterns.entrySet()){
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