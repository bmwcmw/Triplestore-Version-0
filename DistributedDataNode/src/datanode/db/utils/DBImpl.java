package datanode.db.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map.Entry;

/**
 * <p>Database utility for ONE predicate.</p>
 * This implementation allows to store and fetch triple data, not only for the BitMat compression 
 * but also to prepare the execution of SPARQL queries (loading data into databases inside different 
 * environments, etc). 
 *
 * For data nodes
 * 
 * @author CMWT420
 *
 */
public interface DBImpl {

	/**
	 * Cleans all the things
	 */
	public void cleanAll();

	/**
	 * Closes the connection
	 * @throws SQLException
	 */
	public void closeAll() throws Exception;
	
	

	/* * * * * * *
	 * * Index * *
	 * * * * * * */
	/**
	 * Loads the index from an index file
	 * @param path
	 */
	public void loadIndexFromFile(String path) throws IOException, ParseException;
	
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
	
	
	
	
	/* * * * * * *
	 * * Matrix  *
	 * * * * * * */
	/**
	 * Loads the matrix from a compressed matrix file
	 * @param path
	 */
	public void loadMatrixFromFile(String path) throws IOException, ParseException;
	
	/**
	 * Returns the number of SO pairs
	 * @return
	 */
	public Long fetchSOSize();
	
	
	
	/* * * * * * *
	 * Metadata  *
	 * * * * * * */
	/**
	 * Detects and loads meta-data of ONE compressed predicate files (block mode)
	 * @param path
	 */
	public void loadMetaFromFile(String path) throws IOException, ParseException;

	/**
	 * Loads the current predicate's number of files appear in the meta list
	 */
	public int fetchLoadedMetaSize();
	
	/**
	 * Returns the filename and the line number of a asked lineId(S/O) and colId(O/S) in a specific
	 * predicate's matrix
	 * @param predName
	 * @param matName
	 * @param lineId
	 * @param colId
	 */
	public Entry<String, Integer> getFileLineNumber(String predName, String matName, Long lineId, 
			Long colId);
	
}
