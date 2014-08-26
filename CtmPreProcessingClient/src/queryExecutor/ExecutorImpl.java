package queryExecutor;

import indexNodesDBUtils.DBImpl;
import queryObjects.QueryPatternResult;
import queryObjects.StringPattern;

/**
 * A query executor performs processed SPARQL query by fetching data from different file systems.
 * <p>Don't forget to setLocalPath(String) if a local file system is used. In this
 * case, the execute function accepts null destination information. Otherwise, 
 * the destination information cannot be null while launching the execution.</p>
 * <p>Why not static? - 
 * It's great that interface can support static methods since Java 7, but not for lower versions
 * ...</p>
 * @author CEDAR
 *
 */
public interface ExecutorImpl {
	
	public static enum MODE{
		LOCALFS, HDFS, CEDAR
	}
	
	public void setMode(MODE toSet);
	
	public MODE getMode();
	
	public void setLocalPath(String path);
	
	public String getLocalPath();
	
	public void setDBU(DBImpl toSet);
	
	public DBImpl getDBU();
	
	public QueryPatternResult fetchFromDest(String dest, StringPattern pat) throws Exception;

}
