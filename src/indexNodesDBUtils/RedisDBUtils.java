package indexNodesDBUtils;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>Redis is an open source, BSD licensed, advanced key-value store. It is often referred 
 * to as a data structure server since keys can contain strings, hashes, lists, sets and sorted sets.</p>
**/
public class RedisDBUtils extends DBUtils{
	
	public RedisDBUtils() throws SQLException, ClassNotFoundException{
		Class.forName("nl.cwi.monetdb.jdbc.MonetDriver");
		_conn = DriverManager.getConnection(DBConstants.MonetDBurl, "monetdb", "monetdb");
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
    	_st.executeUpdate("INSERT INTO indexnodes(data) "
    			+ "values ('" + node + "');");
    	return fetchIdByNode(node);
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
