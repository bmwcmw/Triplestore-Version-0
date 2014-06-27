package indexNodesDBUtils;

import java.util.ArrayList;
import java.util.Map;

import dataCompressor.SOLongPair;

public interface COMMImpl extends DBImpl{
	
	/**
	 * Adds an SO String pair for the compression
	 * @param so
	 */
	public void addSO(SOLongPair so);
	
	/**
	 * Returns the number of SO pairs
	 * @return number of SO pairs
	 */
	public Long fetchSOSize();
    
	/**
	 * Fetches current number of nodes
	 * @return Current index-node table's size
	 */
    public Long fetchIndexSize();
	
    /**
     * Inserts a note into the table after checking the existence
     * @param node data string
     * @return index value
     */
    public void insertNode(String node);
    
    /**
     * Node ==> Index
     * @param node
     * @return index
     */
	public Long fetchIdByNode(String node);

	/**
     * Index ==> Node
	 * @param index
	 * @return node
	 */
	public String fetchNodeById(Long index);
	
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
	public ArrayList<SOLongPair> fetchSOList();
	
	/*
	 * For data nodes
	 */
	
	/**
	 * Loads the index from a compressed index file
	 * @param path
	 */
	public void loadFromFile(String path);
	
	/**
	 * Loads the size of loaded database
	 * @param path
	 */
	public Long fetchLoadedSize();
	
}
