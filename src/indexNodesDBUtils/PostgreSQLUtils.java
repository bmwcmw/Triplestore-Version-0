package indexNodesDBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.google.common.collect.BiMap;

import dataCompressor.SOIntegerPair;

/**
 * Postgres
 * @author Cedar
 */
/*
CREATE TABLE indexnodes (
	    id        serial PRIMARY KEY,
	    data       varchar(128) NOT NULL UNIQUE
);
TRUNCATE TABLE indexnodes;
*/
public class PostgreSQLUtils implements JDBCImpl{

	protected String _tablename;
	protected Statement _st;
	protected ResultSet _rs;
	protected Connection _conn = null;
    
	public PostgreSQLUtils() throws SQLException, ClassNotFoundException{
		this(DBConstants.PostgreSQLurl, "postgres", "postgres");
	}
    
	public PostgreSQLUtils(String url, String user, String pwd) throws SQLException, ClassNotFoundException{
		_conn = DriverManager.getConnection(url, user, pwd);
		_st = _conn.createStatement();
	}
	
	@Override
    public Integer fetchIndexSize() throws SQLException{
    	_rs = _st.executeQuery("SELECT count(id) as nb FROM indexnodes");
    	if ( _rs.next() ) return _rs.getInt("nb");
    	else return null;
    }

	@Override
    public Integer insertNode(String node) throws SQLException{
    	_rs = _st.executeQuery("INSERT INTO indexnodes(data) "
    			+ "values ('" + node + "') RETURNING id;");
    	if ( _rs.next() ) return _rs.getInt("id");
    	else return null;
    }

	@Override
	public Integer fetchIdByNode(String node) throws SQLException {
		_rs = _st.executeQuery("SELECT id FROM indexnodes "
    			+ "WHERE data = '" + node + "'");
		if ( _rs.next() ) return _rs.getInt("id");
    	else return null;
	}

	@Override
	public String fetchNodeById(Integer index) throws SQLException {
		_rs = _st.executeQuery("SELECT data FROM indexnodes "
    			+ "WHERE id = " + index);
		if ( _rs.next() ) return _rs.getString("data");
    	else return null;
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
		// TODO Auto-generated method stub
		
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
