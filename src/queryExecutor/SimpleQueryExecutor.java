package queryExecutor;

import java.util.ArrayList;

import queryUtils.SubQuerySet;
import queryUtils.Pattern;
import queryUtils.QueryUtils;

public class SimpleQueryExecutor {
	
	public static void execute(SubQuerySet subqueries, String dst){
		ArrayList<Pattern> listPattern;
		ArrayList<String> listObject;
		if((listPattern=subqueries.get(QueryUtils.VarType.S, true)) != null){
			for(Pattern p : listPattern){
				String s = fetchOS(p.getP(), p.getO());//only 1,4,5,100,2...
				listObject = fetchNumberString(s);
				
			}
		}
		if((temp=g.get(QueryUtils.VarType.P, true)) != null){
			
		}
		if((temp=g.get(QueryUtils.VarType.O, true)) != null){
			
		}
		if((temp=g.get(QueryUtils.VarType.SP, true)) != null){
			
		}
		if((temp=g.get(QueryUtils.VarType.SO, true)) != null){
			
		}
		if((temp=g.get(QueryUtils.VarType.PO, true)) != null){
			
		}
		if((temp=g.get(QueryUtils.VarType.SPO, true)) != null){
			
		}
	}

}
