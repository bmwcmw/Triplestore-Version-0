package queryPlanner;

import java.util.LinkedList;

import queryUtils.Graph;
import queryUtils.Pattern;
import queryUtils.QueryUtils;
import queryUtils.QueryUtils.VarType;

public class SimpleQueryPlanner {
	
	public static Graph plan(LinkedList<Pattern> triplepatterns){
		Pattern p;
		String countVar = "";
		Graph g = new Graph();
		while((p = triplepatterns.getFirst()) != null){
			if (QueryUtils.isVariable(p.getS())) 
				countVar += "S";
			if (QueryUtils.isVariable(p.getP())) 
				countVar += "P";
			if (QueryUtils.isVariable(p.getO())) 
				countVar += "O";
			g.addPattern(VarType.valueOf(countVar), p);
			countVar = "";
			triplepatterns.removeFirst();
		}
		return null;
	}
	
}
