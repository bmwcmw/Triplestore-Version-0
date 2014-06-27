package indexNodesDBUtils;

import java.sql.SQLException;

import dataCompressor.SOLongPair;

public interface JDBCImpl extends DBImpl{
	
	public void addSO(SOLongPair so);
	
	public Long fetchSOSize();

	/**
	 * Sets the current table name
	 * @param name
	 */
	public void setTableName(String name);
    
    public Long fetchIndexSize() throws SQLException;
	
    public void insertNode(String node) throws SQLException;
    
	public Long fetchIdByNode(String node) throws SQLException;

	public String fetchNodeById(Long index) throws SQLException;
	
	public void cleanAll();
	
	public void closeAll() throws SQLException;
	
//	/**
//	 * Returns the whole index
//	 * @return Map index-literal
//	 */
//	@SuppressWarnings("rawtypes")
//	public Map fetchIndex();
//	
//	/**
//	 * Returns the whole list of SO string pairs
//	 * @return
//	 */
//	public ArrayList<SOLongPair> fetchSOList();
	

	public void writeMatS(String outputFilePath, String comparePath, String inFileName) throws Exception;

	public void writeMatO(String outputFilePath, String comparePath, String inFileName) throws Exception;
	
	public void writeIndex(String outputFilePath);
	
	/*
	 * For data nodes
	 */
	
	public void loadFromFile(String path);

	public Long fetchLoadedSize();
}
