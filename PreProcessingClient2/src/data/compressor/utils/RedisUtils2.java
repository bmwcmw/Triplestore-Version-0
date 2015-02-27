package data.compressor.utils;

import java.io.IOException;
import java.sql.SQLException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import constants.DBConstants;

/**
 * <p>Redis is an open source, BSD licensed, advanced key-value store. It is often referred 
 * to as a data structure server since keys can contain strings, hashes, lists, sets and sorted sets.</p>
 * @author CMWX230
 */
public class RedisUtils2 extends DBImpl2{
	// TODO see old class
	
	private final int DBLOAD = 0;
	private final int DBINDEX1 = 1; //Index to Node
	private final int DBINDEX2 = 2; //Node to Index
	private final int DBSO = 3;
	
	private Jedis jedis = null;
	private Pipeline pipeline = null;
    
	public RedisUtils2() throws SQLException, ClassNotFoundException{
		this(DBConstants.Redisurl);
	}
    
	public RedisUtils2(String url) throws SQLException, ClassNotFoundException{
		jedis = new Jedis(url);
		jedis.connect();
	}
	
	public void connect(){
	}

	@Override
	public int fetchIndexSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer fetchIdByNode(String node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadAuxIndexFromFile() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void compress(boolean writeIndex) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeMat(boolean writeIndex) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeAuxIndex() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeMeta() throws IOException {
		// TODO Auto-generated method stub
		
	}


	
}
