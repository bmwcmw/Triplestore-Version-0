package indexNodesDBUtils;

import java.io.IOException;
import java.sql.SQLException;

import dataCompressor.SOLongPair;

/**
 * This implementation allows to store and fetch triple data, not only for the BitMat compression 
 * but also to prepare the execution of SPARQL queries (load data into databases inside different 
 * environments, etc). 
 * @author Cedar
 *
 */
public interface DBImpl {

	/** 
	 * Since JVM will be very inefficient while a String become large, we flush it periodically.
	 */
	public static final int MAXSTRLENGTH = 40000;
	
	/**
	 * Adds an SO String pair for the compression
	 * @param so
	 */
	public void addSO(SOLongPair so);

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
     * Inserts a note into the table after checking the existence
     * @param node
     * @throws SQLException
     */
    public void insertNode(String node) throws Exception;

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
	 * Writes the Subject-Object matrix, and optional sorted S array file
	 */
	public void writeMatS(String outputFilePath, String comparePath, String inFileName) throws Exception;

	/**
	 * Writes the Object-Subject matrix, and optional sorted O array file
	 */
	public void writeMatO(String outputFilePath, String comparePath, String inFileName) throws Exception;

	/**
	 * Writes the index file
	 */
	public void writeIndex(String outputFilePath) throws Exception;

	/**
	 * Writes the meta-data of predicates if block option is enabled
	 * @param path
	 */
	public void writeMeta(String path) throws Exception;
	
//	@SuppressWarnings("rawtypes")
//	public Map fetchIndex();
//	
//	public ArrayList<SOLongPair> fetchSOList();
	
	/*
	 * For data nodes
	 */

	/**
	 * Loads the index from a compressed index file
	 * @param path
	 */
	public void loadFromFile(String path) throws IOException;
	

	/**
	 * Loads the size of loaded database
	 * @param path
	 */
	public Long fetchLoadedSize();
}
