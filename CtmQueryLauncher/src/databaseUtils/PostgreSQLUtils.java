package databaseUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dataCompressor.SOLongPair;

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
public class PostgreSQLUtils implements DBImpl{

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
    public Long fetchIndexSize() throws SQLException{
    	_rs = _st.executeQuery("SELECT count(id) as nb FROM indexnodes");
    	if ( _rs.next() ) return _rs.getLong("nb");
    	else return null;
    }

	@Override
    public void insertNode(String node) throws SQLException{
    	_rs = _st.executeQuery("INSERT INTO indexnodes(data) "
    			+ "values ('" + node + "') RETURNING id;");
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
	public void addSO(SOLongPair so) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long fetchSOSize() {
		// TODO Auto-generated method stub
		return null;
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
	public void writePredToFile(String inFileName, String outputFilePath, String comparePath) 
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long fetchLoadedSize() {
		// TODO Auto-generated method stub
		return null;
	}

}
