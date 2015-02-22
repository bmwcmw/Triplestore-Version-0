package dataCompressorUtils;

import java.io.IOException;
import java.sql.SQLException;

import dataCompressor.SOLongPair;

/**
 * This implementation allows to store and fetch triple data for the BitMat compression. Basically 
 * the databases comport as Key-Value stores.
 * @author Cedar
 *
 */
public interface DBImpl {

	/** 
	 * Since JVM will be very inefficient while a String become large, we flush it periodically.
	 * (Only for non-block mode)
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
	 * Writes : 
	 * <p>- the Subject-Object matrix with optional sorted S array file </p>
	 * <p>- the Object-Subject matrix with optional sorted O array file </p>
	 * <p>- the index file </p>
	 * <p>- and the meta-data of predicates (if block option is enabled) </p>
	 */
	public void writePredToFile(String inFileName, String outputFilePath, String comparePath) throws Exception;
	
	/*
	 * For data nodes
	 */

	/**
	 * Loads the index from a compressed index file
	 * @param path
	 */
	public void loadIndexFromFile(String path) throws IOException;
	

	/**
	 * Loads the size of loaded database
	 * @param path
	 */
	public Long fetchLoadedSize();
}
