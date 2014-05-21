package indexNodesDBUtils;

import java.util.ArrayList;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dataCompressor.SOIntegerPair;

public class InRamDBUtils extends DBUtils{
	private BiMap<Integer, String> nodes;
	private ArrayList<SOIntegerPair> soList = new ArrayList<SOIntegerPair>();
	
	@Override
	public void addSO(SOIntegerPair so){
		soList.add(so);
	}
	
	@Override
	public Integer fetchSOSize(){
		return new Integer(soList.size());
	}

	@Override
	public void setTableName(String name){
		_tablename = name;
	}
	
	public InRamDBUtils(){
		nodes = HashBiMap.create();
	}
	
	@Override
    public Integer fetchIndexSize(){
    	return nodes.size();
    }

	@Override
    public Integer insertNode(String node){
		int newid = nodes.size();
    	nodes.put(newid, node);
		return newid;
    }

	@Override
	public Integer fetchIdByNode(String node){
		return nodes.inverse().get(node);
	}

	@Override
	public String fetchNodeById(Integer index){
		return nodes.get(index);
	}
	
	@Override
	public void cleanAll(){
		nodes = HashBiMap.create();
		soList = new ArrayList<SOIntegerPair>();
	}
	
	@Override
	public BiMap<Integer, String> fetchIndex(){
		return nodes;
	}
	
	@Override
	public ArrayList<SOIntegerPair> fetchSOList(){
		return soList;
	}
}
