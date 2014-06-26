package indexNodesDBUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import dataCompressor.SOLongPair;

public interface DBImpl {
	
	/**
	 * Adds an subject-object pair (in integer) for the compression
	 * @param SO pair
	 */
	public void addSO(SOLongPair so);
	
	/**
	 * Returns the number of subject-object pairs
	 * @return Number
	 */
	public Long fetchSOSize();
    
	/**
	 * Fetches current number of subject-object nodes
	 * @return Current index-node table's size
	 * @throws SQLException
	 */
    public Long fetchIndexSize() throws SQLException;
	
    /**
     * Inserts a note into the table then return the id of newly inserted node
     * @param node
     * @return index
     * @throws SQLException
     */
    public Long insertNode(String node) throws SQLException;
    
    /**
     * Inputs Node (literal) ==> Fetches Index (numerical)
     * @param node
     * @return index
     * @throws SQLException
     */
	public Long fetchIdByNode(String node) throws SQLException;

	/**
     * Inputs Index (numerical) ==> Fetches Node (literal)
	 * @param index
	 * @return node
	 * @throws SQLException
	 */
	public String fetchNodeById(Long index) throws SQLException;
	
	/**
	 * Cleans all tables/DBs
	 */
	public void cleanAll();
	
	/**
	 * Closes the connection
	 * @throws SQLException
	 */
	public void closeAll() throws Exception;
	
	/**
	 * Returns the whole index as a Map
	 * @return Map index-literal
	 */
	@SuppressWarnings("rawtypes")
	public Map fetchIndex();
	
	/**
	 * Returns the whole list of SO string pairs
	 * @return
	 */
	public ArrayList<SOLongPair> fetchSOList();
	
	/*
	 * For data nodes
	 */
	
	/**
	 * Loads the index from a compressed index file into selected database
	 * @param path
	 */
	public void loadFromFile(String path) throws IOException;
}
