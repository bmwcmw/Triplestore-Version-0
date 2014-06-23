package queryExecutor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.collect.Sets;

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
	
	public static void execute(ParsedQuery parsed, JSONArray dstInfo){
		for (Object o : dstInfo){
			JSONObject newJO = (JSONObject) o;
		}
		
		HashMap<Integer, SubQuerySet> patterns = parsed.getPatterns();
		SubQuerySet subset;
		Set<String> result;
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
							result = SimpleQueryExecutor.fetchFromDest(destPred, VarType.S);

							if(pat.getType().toString().contains("O")){
								Set<String> resultO = 
										SimpleQueryExecutor.fetchFromDest(destPred, VarType.O);
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
	}

}
