package queryObjects;

import java.util.ArrayList;

import localIOUtils.IOUtils;
import queryUtils.QueryUtils.VarType;

/**
 * Result set of an SPARQL query containing all selected variables and result 
 * lists in order.
 * 
 * @author Cedar
 *
 */
public class QueryResult {
	
	private ArrayList<String> selectedVariables;
	
	private ArrayList<ArrayList<String>> resultSet;
	
	public QueryResult(ArrayList<String> select){
		selectedVariables = select;
		resultSet = new ArrayList<ArrayList<String>>();
	}
	
	public QueryResult(ArrayList<String> select,
			ArrayList<ArrayList<String>> result){
		selectedVariables = select;
		resultSet = result;
	}
	
	public boolean addEntryToList(String var, String elem){
		int indexV = selectedVariables.indexOf(var);
		if(indexV > -1){
			resultSet.get(indexV).add(elem);
			return true;
		}
		return false;
	}
	
	public boolean appendPatternResult(QueryPatternResult patRes){
		VarType typeOfPat = patRes.getPattern().getType();
		ArrayList<String> listToJoin1;
		ArrayList<String> listToJoin2;
		String var;
		switch(typeOfPat){
			case S : 
				listToJoin1 = patRes.getResultSet().get(2);
				var = patRes.getPattern().getS();
				int indexOfVar = selectedVariables.indexOf(var);
				int removed = 0;
				if(indexOfVar > -1){
					//For existing variable
					for(int i=0;i<listToJoin1.size();i++){
						if(!resultSet.get(indexOfVar).contains(listToJoin1.get(i))){
							for(int j=0;j<resultSet.size();j++){
								resultSet.get(j).remove(i-removed);
							}
							removed++;
						} else {
							
						}
					}
				} else {
					//When the result set contains an unknown variable
					//THIS SHOULD NOT HAPPEN
					return false;
				}
				break;
			case O : 
				listToJoin1 = patRes.getResultSet().get(0);
				var = patRes.getPattern().getO();
				break;
			case SO : 
				listToJoin1 = patRes.getResultSet().get(0);
				listToJoin2 = patRes.getResultSet().get(2);
				break;
			default : IOUtils.logLog("Mode " + typeOfPat + " not supported.");
		}
		return false;
	}
	
	public void outputToFile(String localDestFileName){
		
	}
	
	public void outputToTerminal(){
		for(int i=0;i<selectedVariables.size();i++){
			System.out.print(selectedVariables.get(i)+"\t");
		}
		System.out.println("--------------------------------");
	}
	
}
