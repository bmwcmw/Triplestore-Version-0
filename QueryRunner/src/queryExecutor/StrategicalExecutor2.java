package queryExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import localIOUtils.IOUtils;

import org.json.simple.JSONArray;

import constants.CTMConstants;
import queryExecutor.ExecutorImpl.MODE;
import queryObjects.LongPattern;
import queryObjects.ParsedQuery;
import queryObjects.QueryPatternResult;
import queryObjects.QueryResult;
import queryObjects.StringPattern;
import queryObjects.SubQueryPatternSet;
import queryRewriter.SimpleQueryTranslator;
import runnerDbUtils.DBImpl;

/**
 * This is the query executor which performs processed SPARQL query by asking 
 * different file systems.
 * <p>In this second strategical version, we use 1) the cost of query pattern which begins the 
 * execution by processing firstly query patterns having least variable(s), 2) the cost of 
 * variable and 3) the cost of predicate(see our technical report). What's more, it tries to 
 * combines all query patterns sharing the same predicate together in order to minimize the cost of 
 * network requests.</p>
 * <p>Don't forget to setLocalPath(String) if a local file system is used. In this
 * case, the execute function accepts null destination information. Otherwise, 
 * the destination information cannot be null while launching the execution.</p>
 * @author CEDAR
 *
 */
public class StrategicalExecutor2 implements ExecutorImpl {
	
	private MODE mode = MODE.STANDARD;
	
	private String localPath = null;
	
	private DBImpl dbu;
	
	private HashMap<String, DBImpl> dbuList;
	
	@Override
	public void setMode(MODE toSet){
		mode = toSet;
	}
	
	@Override
	public MODE getMode(){
		return mode;
	}
	
	@Override
	public void setDBUList(HashMap<String, DBImpl> list) {
		dbuList = list;
	}

	@Override
	public QueryPatternResult fetchFromDest(String dest, StringPattern pat) throws Exception {
		switch(mode){
			case LOCALFS:
				return fetchFromLocalFS(dest, pat);
			case HDFS:
				return fetchFromHDFS(dest, pat);
			case CEDAR:
				return fetchFromCEDAR(dest, pat);
			default:
				return null;
		}
	}
	
	private QueryPatternResult fetchFromLocalFS(String pred, StringPattern pat) throws Exception {
		QueryPatternResult result = null;
		
		IOUtils.logLog("Predicate term : "+pred);
		/* Paths of specified predicate */
		String indPath = localPath + File.separator + pred + CTMConstants.IndexExt;
		String matSOPath = localPath + File.separator + pred + CTMConstants.SOMatrixExt;
		String matOSPath = localPath + File.separator + pred + CTMConstants.OSMatrixExt;
		
		dbu.loadIndexFromFile(indPath);
		LongPattern intPat = SimpleQueryTranslator.toCompressed(dbu, pat);
		
		Set<String> resultLines = new HashSet<String>();
		/* Here P is never variable */
		IOUtils.logLog(pat.toString() + " ==> " + intPat.toString());
		Long sId = null;
		Long oId = null;
		String sLine = null;
		String oLine = null;
		HashSet<Long> sSet = null;
		HashSet<Long> oSet = null;
		String line;
		long lineNb = 0;
		switch(pat.getType()){
		/* 
		 * Note variable patterns and variable type, for example ?X pred0 ?Y has 
		 * type SO then note them in a QueryPatternResult containing string variable
		 * and variable type
		 * 
		 * Then return the string result set and QueryPatternResult of pattern, 
		 * while all patterns are done, the executer combine results according 
		 * to selected variables in order (using getSelect() function) and return
		 * a list of list that can be displayed.
		 */
			case NON:
				/* Fetch the line containing the Subject, and see if the object 
				 * id appears in the set decoded from the subject's line */
				sId = dbu.fetchIdByNode(pat.getS());
				oId = dbu.fetchIdByNode(pat.getO());
				sLine = MatrixLineReaderLocalFS.readLocalLine(sId, new File(matSOPath));
				oSet = MatrixLineParser.parseMatrixLine(sLine);
				if(!oSet.contains(oId)) 
					/* If this non-variable pattern isn't not satisfied, return 
					 * null as result list */
					result = new QueryPatternResult(pat, null);
				else {
					/* If this non-variable pattern is satisfied, return a list 
					 * containing 3 sub-lists (with size=1,1,1) where S, P and O
					 * strings are the only values separately stored in each list 
					 */
					ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
					ArrayList<String> arrS = new ArrayList<String>();
					arrS.add(pat.getS());
					ArrayList<String> arrP = new ArrayList<String>();
					arrP.add(pat.getP());
					ArrayList<String> arrO = new ArrayList<String>();
					arrO.add(pat.getO());
					resList.add(arrS);
					resList.add(arrP);
					resList.add(arrO);
					/* DEBUG */
					System.out.println(resList);
					result = new QueryPatternResult(pat, resList);
				}
				break;
			case S: 
				/* Fetch the line containing the Object, and see the set decoded 
				 * from the object's line has any entry */
				oId = dbu.fetchIdByNode(pat.getO());
				oLine = MatrixLineReaderLocalFS.readLocalLine(oId, new File(matOSPath));
				sSet = MatrixLineParser.parseMatrixLine(oLine);
				if(sSet.size()==0)
					/* If the pattern isn't not satisfied, return null as result 
					 * list */
					result = new QueryPatternResult(pat, null);
				else {
					/* If the pattern is satisfied, return a list containing 3 
					 * sub-lists (with size=n,1,1) where P, O strings are 
					 * the only values separately stored in each list, and S the
					 * list decoded from object's line */
					ArrayList<String> convertedList = new ArrayList<String>();
					for(Long i : sSet){
						convertedList.add(dbu.fetchNodeById(i));
					}
					ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
					ArrayList<String> arrP = new ArrayList<String>();
					arrP.add(pat.getP());
					ArrayList<String> arrO = new ArrayList<String>();
					arrO.add(pat.getO());
					resList.add(convertedList);
					resList.add(arrP);
					resList.add(arrO);
					/* DEBUG */
					System.out.println(resList);
					result = new QueryPatternResult(pat, resList);
				}
				break;
			case O:
				/* Fetch the line containing the Subject, and see the set decoded 
				 * from the subject's line has any entry */
				sId = dbu.fetchIdByNode(pat.getS());
				sLine = MatrixLineReaderLocalFS.readLocalLine(sId, new File(matSOPath));
				oSet = MatrixLineParser.parseMatrixLine(sLine);
				if(oSet.size()==0)
					/* If the pattern isn't not satisfied, return null as result 
					 * list */
					result = new QueryPatternResult(pat, null);
				else {
					/* If the pattern is satisfied, return a list containing 3 
					 * sub-lists (with size=1,1,n) where S, P strings are 
					 * the only values separately stored in each list, and O the
					 * list decoded from subject's line */
					ArrayList<String> arrS = new ArrayList<String>();
					arrS.add(pat.getS());
					ArrayList<String> arrP = new ArrayList<String>();
					arrP.add(pat.getP());
					ArrayList<String> convertedList = new ArrayList<String>();
					for(Long i : oSet){
						convertedList.add(dbu.fetchNodeById(i));
					}
					ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
					resList.add(arrS);
					resList.add(arrP);
					resList.add(convertedList);
					/* DEBUG */
					System.out.println(resList);
					result = new QueryPatternResult(pat, resList);
				}
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
	
	private QueryPatternResult fetchFromHDFS(String dest, StringPattern pat){
		QueryPatternResult result = new QueryPatternResult(pat, null);
		return result;
	}
	
	private QueryPatternResult fetchFromCEDAR(String dest, StringPattern pat){
		QueryPatternResult result = new QueryPatternResult(pat, null);
		return result;
	}
	
	/**
	 * Executes a parsed query and gets the merged result set
	 * @param parsed : Parsed query
	 * @param dstInfo : Connection information of dataset if needed
	 * @return The result set
	 * @throws Exception
	 */
	public QueryResult execute(ParsedQuery parsed, JSONArray dstInfo) throws Exception{
		/* Check the path of local files if using Local FS */
		if(mode == MODE.LOCALFS && localPath == null){
			throw new Exception("ERROR : Local source folder not set " +
					"while using local file system mode.");
		}
		
		/* If we use distributed systems, we must specify the connection
		 * information */
		if((mode == MODE.HDFS||mode==MODE.CEDAR) && dstInfo == null){
			throw new Exception("ERROR : Destination information null " +
					"while using distributed file system mode.");
		}
		
		//TODO parse JSON array
//		for (Object o : dstInfo){
//			JSONObject newJO = (JSONObject) o;
//		}
		
		HashMap<Integer, SubQueryPatternSet> patterns = parsed.getPatterns();
		SubQueryPatternSet subset;
		QueryResult result = null;
		/* Naive version : execute from 0 to 3 variable(s) */
		for(int i=0; i<=3; i++){
			if((subset = patterns.get(i)) != null){
				HashMap<Integer, StringPattern> subpatterns = subset.getAll();
				for(Entry<Integer, StringPattern> ent : subpatterns.entrySet()){
					StringPattern pat = ent.getValue();
					if(pat.getType().toString().contains("P")){
						IOUtils.logLog("Predicate is variable. Preparing broadcast.");
						/* Predicate is a variable */
						//NO broadcast
					} else {
						IOUtils.logLog("Predicate is not variable. Preparing subqueries.");
						/* Predicate is not a variable 
						 * Convert predicate to filename : 
						 * Remove all before ":", then ":" to "-"
						 */
						String destPred = pat.getP().replaceAll(".*:", "").replace(":", "-");
						QueryPatternResult thisRes = fetchFromDest(destPred, pat);
					}
				}
			}
		}
		return result;
	}

}
