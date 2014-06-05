package indexNodesDBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import dataCompressor.SOIntegerPair;

public class DBUtils {
	
	protected String _tablename;
	protected Statement _st;
	protected ResultSet _rs;
	protected Connection _conn = null;
	
	/**
	 * Adds an SO String pair for the compression
	 * @param so
	 */
	public void addSO(SOIntegerPair so){
		
	}
	
	/**
	 * Returns the number of SO pairs
	 * @return
	 */
	public Integer fetchSOSize(){
		return null;
	}
	
	/**
	 * Sets the current table name
	 * @param name
	 */
	public void setTableName(String name){
		_tablename = name;
	}
    
	/**
	 * Fetches current number of nodes
	 * @return Current index-node table's size
	 * @throws SQLException
	 */
    public Integer fetchIndexSize() throws SQLException{
		return null;
    }
	
    /**
     * Inserts a note into the table then return the id of newly inserted node
     * @param node
     * @return index
     * @throws SQLException
     */
    public Integer insertNode(String node) throws SQLException{
		return null;
    }
    
    /**
     * Node ==> Index
     * @param node
     * @return index
     * @throws SQLException
     */
	public Integer fetchIdByNode(String node) throws SQLException {
		return null;
	}

	/**
     * Index ==> Node
	 * @param index
	 * @return node
	 * @throws SQLException
	 */
	public String fetchNodeById(Integer index) throws SQLException {
		return null;
	}
	
	/**
	 * Cleans all tables
	 */
	public void cleanAll(){
		
	}
	
	/**
	 * Closes the connection
	 * @throws SQLException
	 */
	public void closeAll() throws SQLException{
		if(_rs!=null) _rs.close();
		if(_st!=null) _st.close();
		if(_conn!=null) _conn.close();
	}
	
	/**
	 * Returns the whole index
	 * @return Map index-litteral
	 */
	@SuppressWarnings("rawtypes")
	public Map fetchIndex(){
		return null;
	}
	
	/**
	 * Returns the whole list of SO string pairs
	 * @return
	 */
	public ArrayList<SOIntegerPair> fetchSOList(){
		return null;
	}
	
	/**
	 * Loads the index from a compressed index file
	 * @param path
	 */
	public void loadFromFile(String path){
		
	}
}
