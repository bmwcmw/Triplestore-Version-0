package queryExecutor;

import indexNodesDBUtils.DBImpl;
import indexNodesDBUtils.InRamDBUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import localIOUtils.IOUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.collect.Sets;

import queryObjects.LongPattern;
import queryObjects.ParsedQuery;
import queryObjects.StringPattern;
import queryObjects.SubQuerySet;
import queryRewriter.SimpleQueryTranslator;
import queryUtils.QueryUtils.VarType;

/**
 * This is the query executor which performs processed SPARQL query by asking 
 * different file systems.
 * <p>In this simple version, we use a naif algorithm which begins the execution
 * by processing firstly subqueries having least variable(s).</p>
 * <p>Don't forget to setLocalPath(String) if a local file system is used. In this
 * case, the execute function accepts null destination information. Otherwise, 
 * the destination information cannot be null while launching the execution.</p>
 * @author Cedar
 *
 */
public class SimpleQueryExecutor {
	
	public static enum MODE{
		LOCALFS, HDFS, CEDAR
	}
	
	private static MODE mode = MODE.HDFS;
	
	public static void setMode(MODE toSet){
		mode = toSet;
	}
	
	public static MODE getMode(){
		return mode;
	}
	
	private static String localPath = null;
	
	public static void setLocalPath(String path){
		localPath = path;
	}
	
	public static String getLocalPath(){
		return localPath;
	}
	
	private static DBImpl dbu;
	
	public static void setDBU(DBImpl toSet){
		dbu = toSet;
	}
	
	public static DBImpl getDBU(){
		return dbu;
	}
	
	public static Set<String> fetchFromDest(String dest,  VarType type, StringPattern pat)
			throws Exception{
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
	
	private static Set<String> fetchFromLocalFS(String pred, VarType type, StringPattern pat) 
			throws Exception{
		IOUtils.logLog("Predicate term : "+pred);
		/* Paths of specified predicate */
		String indPath = localPath + File.separator + pred + ".index";
		String matSOPath = localPath + File.separator + pred + ".matrixSO";
		String matOSPath = localPath + File.separator + pred + ".matrixOS";
		
		dbu.loadFromFile(indPath);
		LongPattern intPat = SimpleQueryTranslator.toCompressed(dbu, pat);
		
		Set<String> result = new HashSet<String>();
		/* Here P is never variable */
		IOUtils.logLog(pat.toString() + " ==> " + intPat.toString());
		String line;
		long lineNb = 0;
		switch(type){
		/* 
		 * Note variable patterns and variable type, for example ?X pred0 ?Y has 
		 * type SO then note them in a SubQueryVariable containing string variable
		 * and variable type
		 * 
		 * Then return the string result set and SubQueryVariable of subquery, 
		 * while all subqueries are done, the executer combine results according 
		 * to selected variables in order (using getSelect() function) and return
		 * a list of list that can be displayed.
		 */
			case NON:
				break;
			case S: 
				BufferedReader matSOReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(matSOPath)));
				while ((line = matSOReader.readLine()) != null) {
					/* 
					 * When a line with content appear in the matrix SO file,
					 * add it to the result set.
					 */
					if (line.length()>0) {
						result.add(dbu.fetchNodeById(lineNb));
					}
					lineNb++;
				}
				matSOReader.close();
				break;
			case O:
				BufferedReader matOSReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(matOSPath)));
				while ((line = matOSReader.readLine()) != null) {
					/* 
					 * When a line with content appear in the matrix OS file,
					 * add it to the result set
					 */
					if (line.length()>0) {
						result.add(dbu.fetchNodeById(lineNb));
					}
					lineNb++;
				}
				matOSReader.close();
				break;
			case SO:
				break;
			default:
				break;
		}
		//TODO
		dbu.cleanAll();
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
	
	/**
	 * Executes a parsed query and gets the merged result set
	 * @param parsed : Parsed query
	 * @param dstInfo : Connection information of dataset if needed
	 * @return The result set
	 * @throws Exception
	 */
	public static Set<String> execute(ParsedQuery parsed, JSONArray dstInfo) 
			throws Exception{
		/*  */
		if(mode == MODE.LOCALFS && localPath == null){
			throw new Exception("ERROR : Local source folder not set " +
					"while using local file system mode.");
		}
		
		if((mode==MODE.HDFS||mode==MODE.CEDAR) && dstInfo == null){
			throw new Exception("ERROR : Destination information null " +
					"while using distributed file system mode.");
		}
		
		//TODO parse JSON array
//		for (Object o : dstInfo){
//			JSONObject newJO = (JSONObject) o;
//		}
		
		HashMap<Integer, SubQuerySet> patterns = parsed.getPatterns();
		SubQuerySet subset;
		Set<String> result = null;
		/* Naif version : execute from 0 to 3 variable(s) */
		for(int i=0; i<=3; i++){
			if((subset = patterns.get(i)) != null){
				HashMap<Integer, StringPattern> subpatterns = subset.getAll();
				for(Entry<Integer, StringPattern> ent : subpatterns.entrySet()){
					StringPattern pat = ent.getValue();
					if(pat.getType().toString().contains("P")){
						/* Predicate is a variable */
						//TODO broadcast, where is all predicates' information?
					} else {
						/* Predicate is not a variable */
						// Convert predicate to filename : 
						// Remove all before ":", then ":" to "-"
						String destPred = pat.getP().replaceAll(".*:", "").replace(":", "-");
						result = SimpleQueryExecutor.fetchFromDest(destPred, pat.getType(), pat);
//						/* First get results of S only */
//						if(pat.getType().toString().contains("S")){
//							result = SimpleQueryExecutor.fetchFromDest(destPred, VarType.S, pat);
//						}
//						/* Then get results of O only */
//						if(pat.getType().toString().contains("O")){
//							Set<String> resultO = 
//									SimpleQueryExecutor.fetchFromDest(destPred, VarType.O, pat);
//							/* Guava : If you have reason to believe one of your sets will generally
//							 * smaller than the other, pass it first. */
//							result = (Set<String>) Sets.intersection(
//									result, resultO);
//						}
					}
				}
			}
		}
		return result;
	}

}
