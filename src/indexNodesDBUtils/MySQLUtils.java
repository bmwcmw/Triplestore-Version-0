package indexNodesDBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.google.common.collect.BiMap;

import dataCompressor.SOLongPair;

public class MySQLUtils implements JDBCImpl{

	protected String _tablename;
	protected Statement _st;
	protected ResultSet _rs;
	protected Connection _conn = null;
    
	public MySQLUtils() throws SQLException, ClassNotFoundException{
		//TODO
		_conn = DriverManager.getConnection(DBConstants.MySQLurl, "root", "");
		_st = _conn.createStatement();
	}

	@Override
	public void addSO(SOLongPair so) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long fetchSOSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTableName(String name) {
		_tablename = name;
	}

	@Override
	public Long fetchIndexSize() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long insertNode(String node) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long fetchIdByNode(String node) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchNodeById(Long index) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeAll() throws SQLException {
		if(_rs!=null) _rs.close();
		if(_st!=null) _st.close();
		if(_conn!=null) _conn.close();
	}

	@Override
	public BiMap<Integer, String> fetchIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SOLongPair> fetchSOList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadFromFile(String path) {
		// TODO Auto-generated method stub
		
	}

}
