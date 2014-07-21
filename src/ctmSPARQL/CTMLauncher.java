package ctmSPARQL;

import indexNodesDBUtils.InRamDBUtils;
import queryExecutor.SimpleQueryExecutor;
import queryObjects.ParsedQuery;
import queryPlanner.SimpleQueryPlanner;
import queryUtils.InvalidPatternException;

public class CTMLauncher {

	public static void main(String[] args) throws InvalidPatternException {
		SimpleQueryExecutor.setMode(SimpleQueryExecutor.MODE.LOCALFS);
		SimpleQueryExecutor.setDBU(new InRamDBUtils());
		
		String query = 
				"SELECT ?X, ?Y, ?Z"
				+ "WHERE"
				+ "{?X rdf:type ub:GraduateStudent ."
				+ "  ?Y rdf:type ub:University ."
				+ "  ?Z rdf:type ub:Department ."
				+ "  ?X ub:memberOf ?Z ."
				+ "  ?Z ub:subOrganizationOf ?Y ."
				+ "  ?X ub:undergraduateDegreeFrom ?Y}";
		
		ParsedQuery planed = SimpleQueryPlanner.plan(query);
		
	}

}
