package indexNodesDBUtils;

import java.util.ArrayList;
import java.util.Map;

import dataCompressor.SOIntegerPair;

public interface COMMImpl extends DBImpl{
	
	/**
	 * Adds an SO String pair for the compression
	 * @param so
	 */
	public void addSO(SOIntegerPair so);
	
	/**
	 * Returns the number of SO pairs
	 * @return number of SO pairs
	 */
	public Integer fetchSOSize();
    
	/**
	 * Fetches current number of nodes
	 * @return Current index-node table's size
	 */
    public Integer fetchIndexSize();
	
    /**
     * Inserts a note into the table then return the id of newly inserted node
     * @param node data string
     * @return index value
     */
    public Integer insertNode(String node);
    
    /**
     * Node ==> Index
     * @param node
     * @return index
     */
	public Integer fetchIdByNode(String node);

	/**
     * Index ==> Node
	 * @param index
	 * @return node
	 */
	public String fetchNodeById(Integer index);
	
	/**
	 * Cleans all tables
	 */
	public void cleanAll();
	
	/**
	 * Closes the connection
	 */
	public void closeAll();
	
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
	public ArrayList<SOIntegerPair> fetchSOList();
	
	/*
	 * For data nodes
	 */
	
	/**
	 * Loads the index from a compressed index file
	 * @param path
	 */
	public void loadFromFile(String path);
	
}
