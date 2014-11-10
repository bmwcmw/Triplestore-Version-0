package queryExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import localDBUtils.DBImpl;
import localDBUtils.InRamDBUtilsPOS;
import localIOUtils.IOUtils;

import org.json.simple.JSONArray;

import ctmSPARQLLauncher.CTMConstants;
import queryObjects.ParsedQuery;
import queryObjects.QueryPatternResult;
import queryObjects.QueryResult;
import queryObjects.StringPattern;
import queryObjects.SubQueryPatternSet;

/**
 * This is the query executor which performs processed SPARQL query by asking 
 * different file systems.
 * <p>In this simple version, we use a naive algorithm which begins the execution
 * by processing firstly sub-queries having least variable(s).</p>
 * <p>Don't forget to setLocalPath(String) if a local file system is used. In this
 * case, the execute function accepts null destination information. Otherwise, 
 * the destination information cannot be null while launching the execution.</p>
 * @author CEDAR
 *
 */
public class LocalBasicQueryExecutor implements ExecutorImpl, ExecutorImplLocal {
	
	private final MODE mode = MODE.LOCALFS;
	
	private String localPath = null;
	
	private HashMap<String, DBImpl> dbuList;
	
	@Override
	public void setMode(MODE toSet){
		//mode = toSet;
	}
	
	@Override
	public MODE getMode(){
		return mode;
	}

	@Override
	public void setLocalPath(String path){
		localPath = path;
	}

	@Override
	public String getLocalPath(){
		return localPath;
	}

	@Override
	public QueryPatternResult fetchFromDest(String dest, StringPattern pat) throws Exception {
		switch(mode){
			case LOCALFS:
				return fetchFromLocalFS(dest, pat);
//			case HDFS:
//				return fetchFromHDFS(dest, pat);
//			case CEDAR:
//				return fetchFromCEDAR(dest, pat);
			default:
				return null;
		}
	}
	
	private QueryPatternResult fetchFromLocalFS(String pred, StringPattern pat) throws Exception {
		QueryPatternResult result = null;
		
		IOUtils.logLog("Predicate term : "+pred);

		String dbuName = null;
		InRamDBUtilsPOS dbu;
		
		boolean isType = pred.equals(
				CTMConstants.rdfTypeHeader.substring(
						0, CTMConstants.rdfTypeHeader.length()-1)) 
				|| pred.equals("a");
		
		/* Here P is never variable */
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
				if(isType) {
					for(String fName : dbuList.keySet()){
						if(fName.contains(CTMConstants.rdfTypeHeader) 
								&& fName.contains(pat.getO())) {
							dbuName = fName;
							break;
						}
					}
					if (dbuName == null) {
						IOUtils.logLog("Cannot solve predicate name...");
						throw new Exception();
					}
					dbu = (InRamDBUtilsPOS) dbuList.get(dbuName);
					ArrayList<String> arrSubject = dbu.getArr1();
					boolean solved = false;
					for(String s : arrSubject){
						if(s.equals(pat.getS())){
							solved = true;
						}
					}
					if(solved){
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
						//System.out.println(resList);
						result = new QueryPatternResult(pat, resList);
					} else {
						/* If this non-variable pattern isn't not satisfied, return 
						 * null as result list */
						result = new QueryPatternResult(pat, null);
					}
				} else {
					for(String fName : dbuList.keySet()){
						if(fName.contains(pred)) {
							dbuName = fName;
							break;
						}
					}
					if (dbuName == null) {
						IOUtils.logLog("Cannot solve predicate name...");
						throw new Exception();
					}
					dbu = (InRamDBUtilsPOS) dbuList.get(dbuName);
					ArrayList<String> arrSubject = dbu.getArr1();
					ArrayList<String> arrObject = dbu.getArr2();
					boolean solved = false;
					for(int i=0;i<arrSubject.size();i++){
						if(arrSubject.get(i).equals(pat.getS())
								&& arrObject.get(i).equals(pat.getO())){
							solved = true;
						}
					}
					if(solved){
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
						//System.out.println(resList);
						result = new QueryPatternResult(pat, resList);
					} else {
						/* If this non-variable pattern isn't not satisfied, return 
						 * null as result list */
						result = new QueryPatternResult(pat, null);
					}
				}
				break;
			case S: 
				/* Fetch the line containing the Subject, and see if the object 
				 * id appears in the set decoded from the subject's line */
				if(isType) {
					for(String fName : dbuList.keySet()){
						if(fName.contains(CTMConstants.rdfTypeHeader) 
								&& fName.contains(pat.getO())) {
							dbuName = fName;
							break;
						}
					}
					if (dbuName == null) {
						IOUtils.logLog("Cannot solve predicate name...");
						throw new Exception();
					}
					dbu = (InRamDBUtilsPOS) dbuList.get(dbuName);
					ArrayList<String> arrSubject = dbu.getArr1();
					if(arrSubject.size()>0){
						/* If this non-variable pattern is satisfied, return a list 
						 * containing 3 sub-lists (with size=n,1,1) where S, P and O
						 * strings are the only values separately stored in each list 
						 */
						ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
						ArrayList<String> arrS = arrSubject;
						ArrayList<String> arrP = new ArrayList<String>();
						arrP.add(pat.getP());
						ArrayList<String> arrO = new ArrayList<String>();
						arrO.add(pat.getO());
						resList.add(arrS);
						resList.add(arrP);
						resList.add(arrO);
						/* DEBUG */
						//System.out.println(resList);
						result = new QueryPatternResult(pat, resList);
					} else {
						/* If this non-variable pattern isn't not satisfied, return 
						 * null as result list */
						result = new QueryPatternResult(pat, null);
					}
				} else {
					for(String fName : dbuList.keySet()){
						if(fName.contains(pred)) {
							dbuName = fName;
							break;
						}
					}
					if (dbuName == null) {
						IOUtils.logLog("Cannot solve predicate name...");
						throw new Exception();
					}
					dbu = (InRamDBUtilsPOS) dbuList.get(dbuName);
					ArrayList<String> arrSubject = dbu.getArr1();
					ArrayList<String> arrObject = dbu.getArr2();
					ArrayList<String> toAdd = new ArrayList<String>();
					for(int i=0;i<arrSubject.size();i++){
						if(arrObject.get(i).equals(pat.getO())){
							toAdd.add(arrSubject.get(i));
						}
					}
					if(toAdd.size()>0){
						/* If this non-variable pattern is satisfied, return a list 
						 * containing 3 sub-lists (with size=n,1,1) where S, P and O
						 * strings are the only values separately stored in each list 
						 */
						ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
						ArrayList<String> arrS = toAdd;
						ArrayList<String> arrP = new ArrayList<String>();
						arrP.add(pat.getP());
						ArrayList<String> arrO = new ArrayList<String>();
						arrO.add(pat.getO());
						resList.add(arrS);
						resList.add(arrP);
						resList.add(arrO);
						/* DEBUG */
						//System.out.println(resList);
						result = new QueryPatternResult(pat, resList);
					} else {
						/* If this non-variable pattern isn't not satisfied, return 
						 * null as result list */
						result = new QueryPatternResult(pat, null);
					}
				}
				break;
			case O:
				/* Fetch the line containing the Subject, and see if the object 
				 * id appears in the set decoded from the subject's line */
				if(isType) {
					ArrayList<String> toAdd = new ArrayList<String>();
					for(Entry<String, DBImpl> ent : dbuList.entrySet()){
						String fName = ent.getKey();
						if(fName.contains(CTMConstants.rdfTypeHeader)) {
							ArrayList<String> arrSubject = 
									((InRamDBUtilsPOS) ent.getValue()).getArr1();
							for(String s : arrSubject){
								if(s.equals(pat.getS())){
									//TODO obj name
									toAdd.add(fName);
									break;
								}
							}
						}
					}
					if(toAdd.size()>0){
						/* If this non-variable pattern is satisfied, return a list 
						 * containing 3 sub-lists (with size=1,1,n) where S, P and O
						 * strings are the only values separately stored in each list 
						 */
						ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
						ArrayList<String> arrS = new ArrayList<String>();
						arrS.add(pat.getS());
						ArrayList<String> arrP = new ArrayList<String>();
						arrP.add(pat.getP());
						ArrayList<String> arrO = toAdd;
						resList.add(arrS);
						resList.add(arrP);
						resList.add(arrO);
						/* DEBUG */
						//System.out.println(resList);
						result = new QueryPatternResult(pat, resList);
					} else {
						/* If this non-variable pattern isn't not satisfied, return 
						 * null as result list */
						result = new QueryPatternResult(pat, null);
					}
				} else {
					for(String fName : dbuList.keySet()){
						if(fName.contains(pred)) {
							dbuName = fName;
							break;
						}
					}
					if (dbuName == null) {
						IOUtils.logLog("Cannot solve predicate name...");
						throw new Exception();
					}
					dbu = (InRamDBUtilsPOS) dbuList.get(dbuName);
					ArrayList<String> arrSubject = dbu.getArr1();
					ArrayList<String> arrObject = dbu.getArr2();
					ArrayList<String> toAdd = new ArrayList<String>();
					for(int i=0;i<arrSubject.size();i++){
						if(arrSubject.get(i).equals(pat.getS())){
							toAdd.add(arrObject.get(i));
						}
					}
					if(toAdd.size()>0){
						/* If this non-variable pattern is satisfied, return a list 
						 * containing 3 sub-lists (with size=1,1,n) where S, P and O
						 * strings are the only values separately stored in each list 
						 */
						ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
						ArrayList<String> arrS = new ArrayList<String>();
						arrS.add(pat.getS());
						ArrayList<String> arrP = new ArrayList<String>();
						arrP.add(pat.getP());
						ArrayList<String> arrO = toAdd;
						resList.add(arrS);
						resList.add(arrP);
						resList.add(arrO);
						/* DEBUG */
						//System.out.println(resList);
						result = new QueryPatternResult(pat, resList);
					} else {
						/* If this non-variable pattern isn't not satisfied, return 
						 * null as result list */
						result = new QueryPatternResult(pat, null);
					}
				}
				break;
			case SO:
				/* Fetch the line containing the Subject, and see if the object 
				 * id appears in the set decoded from the subject's line */
				if(isType) {
					ArrayList<String> toAddS = new ArrayList<String>();
					ArrayList<String> toAddO = new ArrayList<String>();
					for(Entry<String, DBImpl> ent : dbuList.entrySet()){
						String fName = ent.getKey();
						if(fName.contains(CTMConstants.rdfTypeHeader)) {
							toAddS.addAll(
									((InRamDBUtilsPOS) ent.getValue()).getArr1());
							toAddO.addAll(
									((InRamDBUtilsPOS) ent.getValue()).getArr2());
						}
					}
					if(toAddS.size()>0){
						/* If this non-variable pattern is satisfied, return a list 
						 * containing 3 sub-lists (with size=n,1,n) where S, P and O
						 * strings are the only values separately stored in each list 
						 */
						ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
						ArrayList<String> arrS = toAddS;
						ArrayList<String> arrP = new ArrayList<String>();
						arrP.add(pat.getP());
						ArrayList<String> arrO = toAddO;
						resList.add(arrS);
						resList.add(arrP);
						resList.add(arrO);
						/* DEBUG */
						//System.out.println(resList);
						result = new QueryPatternResult(pat, resList);
					} else {
						/* If this non-variable pattern isn't not satisfied, return 
						 * null as result list */
						result = new QueryPatternResult(pat, null);
					}
				} else {
					for(String fName : dbuList.keySet()){
						if(fName.contains(pred)) {
							dbuName = fName;
							break;
						}
					}
					if (dbuName == null) {
						IOUtils.logLog("Cannot solve predicate name...");
						throw new Exception();
					}
					dbu = (InRamDBUtilsPOS) dbuList.get(dbuName);
					ArrayList<String> arrSubject = dbu.getArr1();
					ArrayList<String> arrObject = dbu.getArr2();
					if(arrSubject.size()>0){
						/* If this non-variable pattern is satisfied, return a list 
						 * containing 3 sub-lists (with size=n,1,n) where S, P and O
						 * strings are the only values separately stored in each list 
						 */
						ArrayList<ArrayList<String>> resList = 
							new ArrayList<ArrayList<String>>();
						ArrayList<String> arrS = arrSubject;
						ArrayList<String> arrP = new ArrayList<String>();
						arrP.add(pat.getP());
						ArrayList<String> arrO = arrObject;
						resList.add(arrS);
						resList.add(arrP);
						resList.add(arrO);
						/* DEBUG */
						//System.out.println(resList);
						result = new QueryPatternResult(pat, resList);
					} else {
						/* If this non-variable pattern isn't not satisfied, return 
						 * null as result list */
						result = new QueryPatternResult(pat, null);
					}
				}
				break;
			default:
				break;
		}
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
		
//		/* If we use distributed systems, we must specify the connection
//		 * information */
//		if((mode == MODE.HDFS||mode==MODE.CEDAR) && dstInfo == null){
//			throw new Exception("ERROR : Destination information null " +
//					"while using distributed file system mode.");
//		}
		
		//TODO parse JSON array
//		for (Object o : dstInfo){
//			JSONObject newJO = (JSONObject) o;
//		}
		
		HashMap<Integer, SubQueryPatternSet> patterns = parsed.getPatterns();
		SubQueryPatternSet subset;
		QueryResult result = new QueryResult(parsed.getSelect());
		
		/* Naive version : execute from 0 to 3 variable(s) */
		for(int i=0; i<=3; i++){
			if((subset = patterns.get(i)) != null){
				HashMap<Integer, StringPattern> subpatterns = subset.getAll();
				for(Entry<Integer, StringPattern> ent : subpatterns.entrySet()){
					StringPattern pat = ent.getValue();
					if(pat.getType().toString().contains("P")){
						IOUtils.logLog("Predicate is variable. "
								+ "Broadcast not supported yet.");
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
						result.appendPatternResult(thisRes);						
					}
				}
			}
		}
		return result;
	}

	@Override
	public void setDBUList(HashMap<String, DBImpl> list) {
		dbuList = list;
	}

}
