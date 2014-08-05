package indexNodesDBUtils;

import dataCompressor.SOLongPair;

/**
 * Extending DBImpl, this interface is for non jdbc-like databases, generally NoSQL
 * @author Cedar
 */
public interface COMMImpl extends DBImpl{
	
	public void addSO(SOLongPair so);
	
	public Long fetchSOSize();
    
    public Long fetchIndexSize();
	
    public void insertNode(String node);
    
	public Long fetchIdByNode(String node);

	public String fetchNodeById(Long index);
	
	public void cleanAll();
	
	public void closeAll();
	
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
	
	public void writeIndex(String outputFilePath) throws Exception;
	
	public void writeMeta(String path) throws Exception;
	
	/*
	 * For data nodes
	 */
	
	public void loadFromFile(String path);
	
	public Long fetchLoadedSize();
	
}
