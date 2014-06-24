package indexNodesDBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.google.common.collect.BiMap;

import dataCompressor.SOIntegerPair;

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
	public void addSO(SOIntegerPair so) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer fetchSOSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTableName(String name) {
		_tablename = name;
	}

	@Override
	public Integer fetchIndexSize() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insertNode(String node) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer fetchIdByNode(String node) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchNodeById(Integer index) throws SQLException {
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
	public ArrayList<SOIntegerPair> fetchSOList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadFromFile(String path) {
		// TODO Auto-generated method stub
		
	}

}
