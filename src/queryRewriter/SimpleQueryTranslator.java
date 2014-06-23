package queryRewriter;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import queryObjects.IntegerPattern;
import queryObjects.StringPattern;
import queryUtils.QueryUtils.VarType;
import indexNodesDBUtils.DBUtils;

public class SimpleQueryTranslator {
	
	public static enum MODE{
		LOCALFS, HDFS, CEDAR
	}
	
	private static MODE mode;
	
	public static void setMode(MODE toSet){
		mode = toSet;
	}
	
	public static MODE getMode(){
		return mode;
	}
	
	public static Set<String> fetchFromDest(String dest,  VarType type){
		switch(mode){
			case LOCALFS:
				return fetchFromLocalFS(dest, type);
			case HDFS:
				return fetchFromHDFS(dest, type);
			case CEDAR:
				return fetchFromCEDAR(dest, type);
			default:
				return null;
		}
	}
	
	private static Set<String> fetchFromLocalFS(String dest, VarType type){
		Set<String> result = new HashSet<String>();
		return result;
	}
	
	private static Set<String> fetchFromHDFS(String dest, VarType type){
		Set<String> result = new HashSet<String>();
		return result;
	}
	
	private static Set<String> fetchFromCEDAR(String dest, VarType type){
		Set<String> result = new HashSet<String>();
		return result;
	}
	
	public static IntegerPattern toCompressed(DBUtils dbu, StringPattern input) throws SQLException{
		return new IntegerPattern(
				dbu.fetchIdByNode(input.getS()),
				dbu.fetchIdByNode(input.getP()),
				dbu.fetchIdByNode(input.getO()) );
	}

}
