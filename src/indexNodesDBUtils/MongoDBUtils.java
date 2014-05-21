package indexNodesDBUtils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MongoDBUtils extends DBUtils{
    
	public MongoDBUtils() throws SQLException, ClassNotFoundException{
		//TODO
		_conn = DriverManager.getConnection(DBConstants.MongoDBurl, "", "");
		_st = _conn.createStatement();
	}

}
