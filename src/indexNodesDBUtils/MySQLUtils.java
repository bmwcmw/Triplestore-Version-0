package indexNodesDBUtils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLUtils extends DBUtils{
    
	public MySQLUtils() throws SQLException, ClassNotFoundException{
		//TODO
		_conn = DriverManager.getConnection(DBConstants.MySQLurl, "root", "");
		_st = _conn.createStatement();
	}

}
