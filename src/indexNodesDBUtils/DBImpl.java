package indexNodesDBUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import dataCompressor.SOLongPair;

public interface DBImpl {
	
	public void addSO(SOLongPair so);
	
	public Long fetchSOSize();
    
    public Long fetchIndexSize() throws SQLException;
	
    public void insertNode(String node) throws SQLException;
    
	public Long fetchIdByNode(String node) throws SQLException;

	public String fetchNodeById(Long index) throws SQLException;
	
	public void cleanAll();
	
	public void closeAll() throws Exception;
	
	@SuppressWarnings("rawtypes")
	public Map fetchIndex();
	
	public ArrayList<SOLongPair> fetchSOList();
	
	/*
	 * For data nodes
	 */
	public void loadFromFile(String path) throws IOException;
}
