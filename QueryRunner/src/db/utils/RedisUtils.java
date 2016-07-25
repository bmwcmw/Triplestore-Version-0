package db.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import constants.DBConstants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import data.cleaner.RDFPairStr;
import data.reader.PairReader;
import localio.IOUtils;

/**
 * <p>Redis is an open source, BSD licensed, advanced key-value store. It is often referred 
 * to as a data structure server since keys can contain strings, hashes, lists, sets and sorted sets.</p>
 * @author CMWT420
 */
public class RedisUtils implements DBImpl{
	private final int DBLOAD = 0;
	private final int DBINDEX1 = 1; //Index to Node
	private final int DBINDEX2 = 2; //Node to Index
	
	private Jedis jedis = null;
	private Pipeline pipeline = null;
    
	public RedisUtils() throws SQLException, ClassNotFoundException{
		this(DBConstants.Redisurl);
	}
    
	public RedisUtils(String url) throws SQLException, ClassNotFoundException{
		jedis = new Jedis(url);
		jedis.connect();
	}

	@Override
	public Long fetchIdByNode(String node) {
		jedis.select(DBINDEX2);
		String index = jedis.get(node);
		return (index==null)?null:Long.valueOf(index);
	}

	@Override
	public String fetchNodeById(Long index) {
		jedis.select(DBINDEX1);
		String node = jedis.get(index.toString());
		return (node==null)?null:node;
	}

	@Override
	public void cleanAll() {
		jedis.flushAll();
	}
	
	/**
	 * Cleans current selected DB
	 */
	public void cleanDB() {
		if(jedis != null)
			jedis.flushDB();
	}

	@Override
	public void closeAll() {
		if(jedis != null)
			jedis.quit();
	}

	@Override
	public void loadIndexFromFile(String path) {
		// TODO direct load https://github.com/ldodds/redis-load
		try {
			jedis.connect();
			jedis.select(DBLOAD);
			pipeline = jedis.pipelined();
			PairReader reader = new PairReader(path);
			RDFPairStr pair = null;
			while ((pair = reader.nextStr()) != null) {
				pipeline.set(pair.getSubject(), pair.getObject());
			}
		} catch (IOException e) {
			IOUtils.logLog("Aborted while loading " + path);
			cleanDB();
			IOUtils.logLog("DB cleaned");
			e.printStackTrace();
		} catch (ParseException e) {
			IOUtils.logLog("Aborted while loading " + path);
			cleanDB();
			IOUtils.logLog("DB cleaned");
			e.printStackTrace();
		} finally {
			pipeline.syncAndReturnAll();
			jedis.close();
		}
		IOUtils.logLog("Successfully loaded. Current size of key-value pair(s) : " + fetchLoadedSize());
	}

	@Override
	public Long fetchLoadedSize() {
		jedis.connect();
		Long size = jedis.dbSize();
		jedis.close();
		return size;
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
