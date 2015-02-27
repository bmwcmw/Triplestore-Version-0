package query.executor;

import java.util.HashMap;

import db.utils.DBImpl;
import query.objects.QueryPatternResult;
import query.objects.StringTriple;

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
		LOCALFS, HDFS, CEDAR, STANDARD
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
	 * Fetches the result of a pattern from the destination
	 * @param dest Destination
	 * @param pat Query pattern
	 * @return QueryPatternResult
	 * @throws Exception
	 */
	public QueryPatternResult fetchFromDest(String dest, StringTriple pat) throws Exception;
	
	/**
	 * Sets a list for all available predicates
	 * @param list
	 */
	public void setDBUList(HashMap<String, DBImpl> list);

}
