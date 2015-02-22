package localDBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>MonetDB aims to use as much of the main memory available, as many cores as can be 
 * practically deployed in parallel processing of queries, and trying to avoid going to 
 * a slow disk.</p>
**/
/*
CREATE TABLE indexnodes (
	    id        integer PRIMARY KEY AUTO_INCREMENT,
	    data       varchar(128) NOT NULL UNIQUE
);
DELETE FROM indexnodes;
DROP TABLE indexnodes;
*/
public class MonetDBUtils implements DBImpl{

	protected String _tablename;
	protected Statement _st;
	protected ResultSet _rs;
	protected Connection _conn = null;
	
	public MonetDBUtils() throws SQLException, ClassNotFoundException{
		Class.forName("nl.cwi.monetdb.jdbc.MonetDriver");
		_conn = DriverManager.getConnection(DBConstants.MonetDBurl, "monetdb", "monetdb");
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
	public void closeAll() {
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
