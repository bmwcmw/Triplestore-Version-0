package runnerDbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import constants.DBConstants;

/**
 * Oracle
 * @author CMWT420
 */
/*
 * See notes
 */
public class OracleUtils implements DBImpl{

	protected String _tablename;
	protected Statement _st;
	protected ResultSet _rs;
	protected Connection _conn = null;
    
	public OracleUtils() throws SQLException, ClassNotFoundException{
		this(DBConstants.Oracleurl, "root", "");
	}
    
	public OracleUtils(String url, String user, String pwd) throws SQLException, ClassNotFoundException{
		_conn = DriverManager.getConnection(url, user, pwd);
		_st = _conn.createStatement();
	}

	@Override
	public Long fetchIdByNode(String node) throws SQLException {
		_rs = _st.executeQuery("SELECT id FROM indexnodes "
    			+ "WHERE data = '" + node + "'");
		if ( _rs.next() ) return _rs.getLong("id");
    	else return null;
	}

	@Override
	public String fetchNodeById(Long index) throws SQLException {
		_rs = _st.executeQuery("SELECT data FROM indexnodes "
    			+ "WHERE id = " + index);
		if ( _rs.next() ) return _rs.getString("data");
    	else return null;
	}

	@Override
	public void cleanAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeAll() throws SQLException {
		// TODO Auto-generated method stub
		
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
