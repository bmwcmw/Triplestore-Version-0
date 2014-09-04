package ctmSPARQL;

import java.io.File;

import indexNodesDBUtils.InRamDBUtils;
import queryExecutor.NaiveQueryExecutor;
import queryObjects.ParsedQuery;
import queryPlanner.SimpleQueryPlanner;

public class CTMLauncher {

	public static void main(String[] args) throws Exception {
		NaiveQueryExecutor exe = new NaiveQueryExecutor();
		exe.setMode(NaiveQueryExecutor.MODE.LOCALFS);
		exe.setLocalPath(System.getProperty("user.dir") + 
				File.separator + "_compressedFake");
		exe.setDBU(new InRamDBUtils());
		
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
		exe.execute(planed, null);
		
//		MatrixLineParser.parseMatrixLine("4:[1]1,1,1,2,2");
		
	}

}
