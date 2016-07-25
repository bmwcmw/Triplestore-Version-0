package datanode.db.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import data.cleaner.RDFPairStr;
import data.reader.PairReader;
import localio.IOUtils;

/**
 * <p>Redis is an open source, BSD licensed, advanced key-value store. It is often referred 
 * to as a data structure server since keys can contain strings, hashes, lists, sets and sorted sets.</p>
 * @author CMWT420
**/
public class RedisUtils implements DBImpl{
	private final int DBLOAD = 0;
	private final int DBINDEX1 = 1; //Index to Node
	private final int DBINDEX2 = 2; //Node to Index
	private final int DBSO = 3;
	private final int DBMETA = 4;
	
	private Jedis jedis = null;
	private Pipeline pipeline = null;
    
	public RedisUtils() throws SQLException, ClassNotFoundException{
		this(constants.DBConstants.Redisurl);
	}
    
	public RedisUtils(String url) throws SQLException, ClassNotFoundException{
		jedis = new Jedis(url);
		jedis.connect();
	}
	
	public void connect(){
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


	
	/* * * * * * *
	 * * Index * *
	 * * * * * * */
	@Override
	public void loadIndexFromFile(String path) {
		// TODO direct bulk load https://github.com/ldodds/redis-load
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
		IOUtils.logLog("Successfully loaded. Current size of index entries : " + 
				fetchIndexSize());
	}

	@Override
	public Long fetchIndexSize() {
		//jedis.connect();
		jedis.select(DBINDEX1);
		Long size = jedis.dbSize();
		//jedis.close();
		return size;
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
	
	
	
	/* * * * * * *
	 * * Matrix  *
	 * * * * * * */
	@Override
	public void loadMatrixFromFile(String path) throws IOException {
		// TODO load matrix files
	}
	
	@Override
	public Long fetchSOSize() {
		//jedis.connect();
		jedis.select(DBSO);
		Long size = jedis.dbSize();
		//jedis.close();
		return size;
	}
	
	public static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	
	
	/* * * * * * *
	 * Metadata  *
	 * * * * * * */
	@Override
	public void loadMetaFromFile(String path) throws IOException,
			ParseException {
		try {
			jedis.connect();
			jedis.select(DBMETA);
			pipeline = jedis.pipelined();
			//TODO
		} finally {
			pipeline.syncAndReturnAll();
			jedis.close();
		}
		IOUtils.logLog("Successfully loaded. Current size of meta entries: " + 
				fetchLoadedMetaSize());
		
	}
	
	@Override
	public int fetchLoadedMetaSize() {
		jedis.connect();
		int size = safeLongToInt(jedis.dbSize());
		jedis.close();
		return size;
	}

	@Override
	public Entry<String, Integer> getFileLineNumber(String predName,
			String matName, Long lineId, Long colId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
