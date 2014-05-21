package indexNodesDBUtils;

import java.sql.DriverManager;
import java.sql.SQLException;

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
public class PostgreSQLUtils extends DBUtils{
    
	public PostgreSQLUtils() throws SQLException, ClassNotFoundException{
		_conn = DriverManager.getConnection(DBConstants.PostgreSQLurl, "postgres", "postgres");
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

}
