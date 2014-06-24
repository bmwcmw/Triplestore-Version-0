package queryExecutor;

import indexNodesDBUtils.DBUtils;
import indexNodesDBUtils.InRamDBUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.collect.Sets;

import queryObjects.IntegerPattern;
import queryObjects.ParsedQuery;
import queryObjects.StringPattern;
import queryObjects.SubQuerySet;
import queryRewriter.SimpleQueryTranslator;
import queryUtils.QueryUtils.VarType;

public class SimpleQueryExecutor {
	
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
	
	private static DBUtils dbu;
	
	public static void setDBU(DBUtils toSet){
		dbu = toSet;
	}
	
	public static DBUtils getDBU(){
		return dbu;
	}
	
	public static Set<String> fetchFromDest(String dest,  VarType type, StringPattern pat)
			throws SQLException, IOException{
		switch(mode){
			case LOCALFS:
				return fetchFromLocalFS(dest, type, pat);
			case HDFS:
				return fetchFromHDFS(dest, type, pat);
			case CEDAR:
				return fetchFromCEDAR(dest, type, pat);
			default:
				return null;
		}
	}
	
	private static Set<String> fetchFromLocalFS(String dest, VarType type, StringPattern pat) 
			throws SQLException, IOException{
		Set<String> result = new HashSet<String>();
		IntegerPattern intPat = SimpleQueryTranslator.toCompressed(dbu, pat);
		FileInputStream fs= new FileInputStream(dest);//TODO file location
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		//TODO functions to convert file names
		for(int i = 0; i < 30; i++)
			br.readLine();
		String lineIWant = br.readLine();
		//TODO
		return result;
	}
	
	private static Set<String> fetchFromHDFS(String dest, VarType type, StringPattern pat){
		Set<String> result = new HashSet<String>();
		return result;
	}
	
	private static Set<String> fetchFromCEDAR(String dest, VarType type, StringPattern pat){
		Set<String> result = new HashSet<String>();
		return result;
	}
	
	public static Set<String> execute(ParsedQuery parsed, JSONArray dstInfo) 
			throws SQLException, IOException{
		for (Object o : dstInfo){
			JSONObject newJO = (JSONObject) o;
		}
		
		HashMap<Integer, SubQuerySet> patterns = parsed.getPatterns();
		SubQuerySet subset;
		Set<String> result = null;
		for(int i=0; i<=3; i++){
			if( (subset = patterns.get(i)) != null){
				HashMap<Integer, StringPattern> subpatterns = subset.getAll();
				for(Entry<Integer, StringPattern> ent : subpatterns.entrySet()){
					StringPattern pat = ent.getValue();
					if(pat.getType().toString().contains("P")){
						//TODO broadcast
					} else {
						String destPred = pat.getP().replace(":", "-");
						if(pat.getType().toString().contains("S")){
							result = SimpleQueryExecutor.fetchFromDest(destPred, VarType.S, pat);

							if(pat.getType().toString().contains("O")){
								Set<String> resultO = 
										SimpleQueryExecutor.fetchFromDest(destPred, VarType.O, pat);
								/* Guava : If you have reason to believe one of your sets will generally
								 * smaller than the other, pass it first. */
								result = (Set<String>) Sets.intersection(
										result, resultO);
							}
						}
						//TODO directly using hdfs?
					}
				}
			}
		}
		return result;
	}

}
