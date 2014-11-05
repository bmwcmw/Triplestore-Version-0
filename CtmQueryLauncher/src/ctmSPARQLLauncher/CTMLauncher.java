package ctmSPARQLLauncher;

import java.io.File;
import java.util.HashMap;

import localDBLoader.DBLoaderImpl.MODE;
import localDBLoader.InRamDBLoaderPOS;
import localDBUtils.DBImpl;
import localDBUtils.InRamDBUtils;
import queryExecutor.LocalBasicQueryExecutor;
import queryObjects.ParsedQuery;
import queryObjects.QueryResult;
import queryPlanner.SimpleQueryPlanner;

public class CTMLauncher {

	public static void main(String[] args) throws Exception {
		LocalBasicQueryExecutor exe = new LocalBasicQueryExecutor();
		exe.setLocalPath(System.getProperty("user.dir") 
				+ File.separator + ".." + File.separator + "CtmPreProcessingClient2"
				+ File.separator + "_pos");
		System.out.println("TO LOAD : " + exe.getLocalPath());
		
		InRamDBLoaderPOS loader = new InRamDBLoaderPOS();
		HashMap<String, DBImpl> dbUnits = loader.getDBList(exe.getLocalPath(), MODE.POS);
		exe.setDBUList(dbUnits);
		
		String query = 
			"SELECT ?X, ?Y" +
			"WHERE" +
			"{"
			+ "?X a ?Y ." 
			+ "?Y rdf-type FullProfessor ." +
			"}";

		ParsedQuery planed = SimpleQueryPlanner.plan(query);
		QueryResult res = exe.execute(planed, null);
		
//		String query = 
//				"SELECT ?X, ?Y, ?Z"
//				+ "WHERE"
//				+ "{?X rdf:type ub:GraduateStudent ."
//				+ "  ?Y rdf:type ub:University ."
//				+ "  ?Z rdf:type ub:Department ."
//				+ "  ?X ub:memberOf ?Z ."
//				+ "  ?Z ub:subOrganizationOf ?Y ."
//				+ "  ?X ub:undergraduateDegreeFrom ?Y}";
		
//		String query = 
//				"SELECT ?X, ?Y, ?Z" +
//				"WHERE" +
//				"{?X likes ?Y ." +
//				"?Y hasAuthor ?Z ." +
//				"?X hasFriend Jill}";
//		
//		ParsedQuery planed = SimpleQueryPlanner.plan(query);
//		exe.execute(planed, null);
		
//		MatrixLineParser.parseMatrixLine("4:[1]1,1,1,2,2");
		
	}

}
