package queryExecutor;

import indexNodesDBUtils.DBImpl;
import queryObjects.QueryPatternResult;
import queryObjects.StringPattern;

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

	@Override
	public void setMode(MODE toSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MODE getMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocalPath(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLocalPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDBU(DBImpl toSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DBImpl getDBU() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryPatternResult fetchFromDest(String dest, StringPattern pat)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
