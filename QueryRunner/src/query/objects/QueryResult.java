package query.objects;

import java.util.ArrayList;

import localIOUtils.IOUtils;
import query.utils.QueryUtils.VarType;

/**
 * Result set of an SPARQL query containing all selected variables and result 
 * lists in order.
 * 
 * @author CMWT420
 *
 */
public class QueryResult {
	
	private ParsedQuery query;
	
	private ArrayList<ArrayList<String>> resultSet;
	
	public QueryResult(ParsedQuery q){
		query = q;
		resultSet = new ArrayList<ArrayList<String>>();
		for(int i=0;i<q.getSelect().size();i++){
			resultSet.add(new ArrayList<String>());
		}
	}
	
	public boolean addEntryToList(String var, String elem){
		int indexV = query.getSelect().indexOf(var);
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
				int indexOfVar = query.getSelect().indexOf(var);
				int removed = 0;
				if(indexOfVar > -1){
					//For existing variable
					if(resultSet.get(indexOfVar).size()>0){
						for(int i=0;i<listToJoin1.size();i++){
							if(!resultSet.get(indexOfVar).contains(listToJoin1.get(i))){
								for(int j=0;j<resultSet.size();j++){
									System.out.println(j + " " + resultSet.get(j).size() + " "
											+ (i-removed));
									resultSet.get(j).remove(i-removed);
								}
								removed++;
							} else {
								
							}
						}
					} else {
						resultSet.get(indexOfVar).addAll(listToJoin1);
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
		return true;
	}
	
	public void outputToFile(String localDestFileName){
		
	}
	
	public void outputToTerminal(){
		for(int i=0;i<query.getSelect().size();i++){
			System.out.print(query.getSelect().get(i)+"\t");
		}
		System.out.println("--------------------------------");
	}
	
}
