package indexNodesDBUtils;

import java.util.ArrayList;

import com.google.common.collect.BiMap;

import dataCompressor.SOIntegerPair;

/**
 * <p>Redis is an open source, BSD licensed, advanced key-value store. It is often referred 
 * to as a data structure server since keys can contain strings, hashes, lists, sets and sorted sets.</p>
**/
public class RedisUtils implements COMMImpl{

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeAll() {
		// TODO Auto-generated method stub
		
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
		
	}
	
}
