package queryExecutor;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import queryObjects.ParsedQuery;
import queryObjects.StringPattern;
import queryObjects.SubQuerySet;
import queryUtils.QueryUtils;

public class SimpleQueryExecutor {
	
	public static void execute(ParsedQuery parsed, JSONArray dstInfo){
		for (Object o : dstInfo){
			JSONObject newJO = (JSONObject) o;
		}
		
		ArrayList<StringPattern> listPattern;
		ArrayList<String> listObject;
		if((listPattern=subqueries.get(QueryUtils.VarType.S, true)) != null){
			for(StringPattern p : listPattern){
				String s = fetchOS(p.getP(), p.getO());//only 1,4,5,100,2...
				listObject = fetchNumberString(s);
				
			}
		}
		if((listPattern=subqueries.get(QueryUtils.VarType.P, true)) != null){
			
		}
		if((listPattern=subqueries.get(QueryUtils.VarType.O, true)) != null){
			
		}
		if((listPattern=subqueries.get(QueryUtils.VarType.SP, true)) != null){
			
		}
		if((listPattern=subqueries.get(QueryUtils.VarType.SO, true)) != null){
			
		}
		if((listPattern=subqueries.get(QueryUtils.VarType.PO, true)) != null){
			
		}
		if((listPattern=subqueries.get(QueryUtils.VarType.SPO, true)) != null){
			
		}
	}

}
