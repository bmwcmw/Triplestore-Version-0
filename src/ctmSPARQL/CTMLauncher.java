package ctmSPARQL;

import java.io.File;

import indexNodesDBUtils.InRamDBUtils;
import queryExecutor.MatrixLineParser;
import queryExecutor.SimpleQueryExecutor;
import queryObjects.ParsedQuery;
import queryPlanner.SimpleQueryPlanner;
import queryUtils.InvalidPatternException;

public class CTMLauncher {

	public static void main(String[] args) throws Exception {
		SimpleQueryExecutor.setMode(SimpleQueryExecutor.MODE.LOCALFS);
		SimpleQueryExecutor.setLocalPath(System.getProperty("user.dir") + 
				File.separator + "_compressedFake");
		SimpleQueryExecutor.setDBU(new InRamDBUtils());
		
//		String query = 
//				"SELECT ?X, ?Y, ?Z"
//				+ "WHERE"
//				+ "{?X rdf:type ub:GraduateStudent ."
//				+ "  ?Y rdf:type ub:University ."
//				+ "  ?Z rdf:type ub:Department ."
//				+ "  ?X ub:memberOf ?Z ."
//				+ "  ?Z ub:subOrganizationOf ?Y ."
//				+ "  ?X ub:undergraduateDegreeFrom ?Y}";
		String query = 
				"SELECT ?X, ?Y, ?Z" +
				"WHERE" +
				"{?X likes ?Y ." +
				"?Y hasAuthor ?Z ." +
				"?X hasFriend Jill}";
		
		ParsedQuery planed = SimpleQueryPlanner.plan(query);
		SimpleQueryExecutor.execute(planed, null);
		
//		MatrixLineParser.parseMatrixLine("4:[1]1,1,1,2,2");
		
	}

}
