package queryExecutor;

import dataCompressorUtils.DBImpl;
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
	
	/**
	 * Acccessing modes of the executor
	 */
	public static enum MODE{
		LOCALFS, HDFS, CEDAR
	}
	
	/**
	 * Sets the accessing mode
	 * @param toSet
	 */
	public void setMode(MODE toSet);
	
	/**
	 * Gets the current accessing mode
	 * @return MODE
	 */
	public MODE getMode();
	
	/**
	 * Sets the local path for the local mode
	 * @param path
	 */
	public void setLocalPath(String path);
	
	/**
	 * Gets the local path for the local mode
	 * @return String path
	 */
	public String getLocalPath();
	
	/**
	 * Sets the DB Util
	 * @param toSet
	 */
	public void setDBU(DBImpl toSet);
	
	/**
	 * Gets the current DB Util
	 * @return DBU
	 */
	public DBImpl getDBU();
	
	/**
	 * Fetches the result of a pattern from the destination
	 * @param dest Destination
	 * @param pat Query pattern
	 * @return QueryPatternResult
	 * @throws Exception
	 */
	public QueryPatternResult fetchFromDest(String dest, StringPattern pat) throws Exception;

}
