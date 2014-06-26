package indexNodesDBUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import dataCompressor.SOLongPair;

public interface JDBCImpl extends DBImpl{
	
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
	 * Sets the current table name
	 * @param name
	 */
	public void setTableName(String name);
    
	/**
	 * Fetches current number of nodes
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
     * Node ==> Index
     * @param node
     * @return index
     * @throws SQLException
     */
	public Long fetchIdByNode(String node) throws SQLException;

	/**
     * Index ==> Node
	 * @param index
	 * @return node
	 * @throws SQLException
	 */
	public String fetchNodeById(Long index) throws SQLException;
	
	/**
	 * Cleans all tables
	 */
	public void cleanAll();
	
	/**
	 * Closes the connection
	 * @throws SQLException
	 */
	public void closeAll() throws SQLException;
	
	/**
	 * Returns the whole index
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
	 * Loads the index from a compressed index file
	 * @param path
	 */
	public void loadFromFile(String path);
}
