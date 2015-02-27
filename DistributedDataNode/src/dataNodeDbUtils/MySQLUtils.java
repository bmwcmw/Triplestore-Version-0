package dataNodeDbUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Map.Entry;

/**
 * 
 * @author CMWT420
 *
 */
public class MySQLUtils implements DBImpl{

	protected String _tablename;
	protected Statement _st;
	protected ResultSet _rs;
	protected Connection _conn = null;
    
	public MySQLUtils() throws SQLException, ClassNotFoundException{
		//TODO
		_conn = DriverManager.getConnection(constants.DBConstants.MySQLurl, "root", "");
		_st = _conn.createStatement();
	}

	@Override
	public void cleanAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeAll() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public Long fetchIndexSize() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long fetchIdByNode(String node) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchNodeById(Long index) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadIndexFromFile(String path) throws IOException {
		// TODO Auto-generated method stub
		
	}
	


	@Override
	public void loadMatrixFromFile(String path) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long fetchSOSize() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public void loadMetaFromFile(String path) throws IOException,
			ParseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int fetchLoadedMetaSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Entry<String, Integer> getFileLineNumber(String predName,
			String matName, Long lineId, Long colId) {
		// TODO Auto-generated method stub
		return null;
	}


}
