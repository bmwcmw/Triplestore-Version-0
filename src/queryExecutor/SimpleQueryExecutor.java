package queryExecutor;

import java.util.ArrayList;

import queryUtils.Graph;
import queryUtils.Pattern;
import queryUtils.QueryUtils;

public class SimpleQueryExecutor {
	
	public static void execute(Graph g){
		ArrayList<Pattern> temp;
		if((temp=g.get(QueryUtils.VarType.S, true)) != null){
			
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
