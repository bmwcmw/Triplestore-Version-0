package dbUtils;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This implementation allows to store and fetch triple data, not only for the BitMat compression 
 * but also to prepare the execution of SPARQL queries (load data into databases inside different 
 * environments, etc). 
 * @author Cedar
 *
 */

/*
 * For data nodes
 */
public interface DBImpl {

	/**
	 * Returns the number of SO pairs
	 * @return
	 */
	public Long fetchSOSize();

	/**
	 * Fetches current number of nodes
	 * @return Current index-node table's size
	 * @throws SQLException
	 */
    public Long fetchIndexSize() throws Exception;

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
	 * Loads the matrix from a compressed index file
	 * @param path
	 */
	public void loadMatrixFromFile(String path) throws IOException;

	/**
	 * Loads the size of loaded database
	 * @param path
	 */
	public Long fetchLoadedSize();
}
