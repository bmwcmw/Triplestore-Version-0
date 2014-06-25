package indexNodesDBUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import localIOUtils.IOUtils;
import redis.clients.jedis.Jedis;

import com.google.common.collect.BiMap;

import dataCleaner.CTMPairStr;
import dataCompressor.SOIntegerPair;
import dataReader.PairReader;

/**
 * <p>Redis is an open source, BSD licensed, advanced key-value store. It is often referred 
 * to as a data structure server since keys can contain strings, hashes, lists, sets and sorted sets.</p>
**/
public class RedisUtils implements COMMImpl{
	
	protected Jedis jedis = null;
    
	public RedisUtils() throws SQLException, ClassNotFoundException{
		this(DBConstants.Redisurl);
	}
    
	public RedisUtils(String url) throws SQLException, ClassNotFoundException{
		jedis = new Jedis(url);
	}

	@Override
	public void addSO(SOIntegerPair so) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer fetchSOSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer fetchIndexSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insertNode(String node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer fetchIdByNode(String node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchNodeById(Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanAll() {
		jedis.connect();
		jedis.dbSize();
	}

	@Override
	public void closeAll() {
		jedis.close();
	}

	@Override
	public BiMap<Integer, String> fetchIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SOIntegerPair> fetchSOList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadFromFile(String path) {
		// TODO https://github.com/ldodds/redis-load
		try {
			jedis.connect();
			PairReader reader = new PairReader(path);
			CTMPairStr pair = null;
			while ((pair = reader.nextStr()) != null) {
				jedis.set(pair.getSubject(), pair.getObject());
			}
		} catch (IOException e) {
			cleanAll();
			e.printStackTrace();
		} catch (ParseException e) {
			cleanAll();
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		IOUtils.logLog("File charged. Current size of key-value pair(s) : " + fetchLoadedSize());
	}

	@Override
	public Long fetchLoadedSize() {
		jedis.connect();
		long size = jedis.dbSize();
		jedis.close();
		return size;
	}
	
}
