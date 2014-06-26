package indexNodesDBUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import localIOUtils.IOUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.google.common.collect.BiMap;

import dataCleaner.CTMPairStr;
import dataCompressor.SOLongPair;
import dataReader.PairReader;

/**
 * <p>Redis is an open source, BSD licensed, advanced key-value store. It is often referred 
 * to as a data structure server since keys can contain strings, hashes, lists, sets and sorted sets.</p>
**/
public class RedisUtils implements COMMImpl{
	private final int DBLOAD = 0;
	private final int DBINDEX = 1;
	private final int DBSO = 2;
	
	private Jedis jedis = null;
	private Pipeline pipeline = null;
    
	public RedisUtils() throws SQLException, ClassNotFoundException{
		this(DBConstants.Redisurl);
	}
    
	public RedisUtils(String url) throws SQLException, ClassNotFoundException{
		jedis = new Jedis(url);
	}

	@Override
	public void addSO(SOLongPair so) {
		try {
			jedis.connect();
			jedis.select(DBSO);
			jedis.set(so.S.toString(), so.O.toString());
		} catch (Exception e) {
			IOUtils.logLog("Aborted while adding SO pair");
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
	public Long fetchSOSize() {
		jedis.connect();
		jedis.select(DBSO);
		Long size = jedis.dbSize();
		jedis.close();
		return size;
	}

	@Override
	public Long fetchIndexSize() {
		jedis.connect();
		jedis.select(DBINDEX);
		Long size = jedis.dbSize();
		jedis.close();
		return size;
	}

	@Override
	public Long insertNode(String node) {
		try {
			jedis.connect();
			jedis.select(DBINDEX);
			jedis.set(String.valueOf(fetchIndexSize()), node);
		} catch (Exception e) {
			IOUtils.logLog("Aborted while adding SO pair");
			cleanDB();
			IOUtils.logLog("DB cleaned");
			e.printStackTrace();
		} finally {
			pipeline.syncAndReturnAll();
			jedis.close();
		}
		return null;
	}

	@Override
	public Long fetchIdByNode(String node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchNodeById(Long index) {
		// TODO Auto-generated method stub
		return null;
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
			jedis.close();
	}

	@Override
	public BiMap<Integer, String> fetchIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SOLongPair> fetchSOList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadFromFile(String path) {
		// TODO https://github.com/ldodds/redis-load
		try {
			jedis.connect();
			jedis.select(DBLOAD);
			pipeline = jedis.pipelined();
			PairReader reader = new PairReader(path);
			CTMPairStr pair = null;
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
		long size = jedis.dbSize();
		jedis.close();
		return size;
	}
	
}
