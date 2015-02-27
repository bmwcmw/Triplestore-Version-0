package query.executor;

/**
 * Definition of functions in local file system
 * @author CMWT420
 *
 */
public interface ExecutorImplLocal {
	
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
	
}
