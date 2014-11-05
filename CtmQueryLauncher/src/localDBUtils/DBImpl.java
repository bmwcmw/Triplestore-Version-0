package localDBUtils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This implementation allows to store and fetch triple data for the query execution. Basically 
 * the databases comport as Key-Value stores.
 * @author Cedar
 *
 */
public interface DBImpl {
	
	/*
	 * For data nodes
	 */
	
	/**
     * Index ==> Node
	 * @param index
	 * @return node
	 * @throws SQLException
	 */
	public Long fetchIdByNode(String node) throws Exception;

    /**
     * Node ==> Index
     * @param node
     * @return index
     * @throws SQLException
     */
	public String fetchNodeById(Long index) throws Exception;

	/**
	 * Cleans all the things
	 */
	public void cleanAll();

	/**
	 * Closes the connection
	 * @throws SQLException
	 */
	public void closeAll() throws Exception;

	/**
	 * Loads the index from a compressed index file
	 * @param path
	 */
	public void loadIndexFromFile(String path) throws IOException;
	
	/**
	 * Puts a pair manually
	 * @param k
	 * @param v
	 */
	public void put(Long k, String v);
	
	/**
	 * Puts a string pair manually
	 * @param k
	 * @param v
	 */
	public void put(String k, String v);

	/**
	 * Loads the size of loaded database
	 * @param path
	 */
	public Long fetchLoadedSize();
}
