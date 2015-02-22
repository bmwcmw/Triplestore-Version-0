package localDBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLUtils implements DBImpl{

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
	public void loadIndexFromFile(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long fetchLoadedSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(Long k, String v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(String k, String v) {
		// TODO Auto-generated method stub
		
	}

}
