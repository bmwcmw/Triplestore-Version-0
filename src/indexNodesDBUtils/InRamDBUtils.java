package indexNodesDBUtils;

import java.util.ArrayList;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dataCompressor.SOIntegerPair;

public class InRamDBUtils implements COMMImpl{
	private BiMap<Integer, String> nodes;
	private ArrayList<SOIntegerPair> soList = new ArrayList<SOIntegerPair>();

	
	public InRamDBUtils(){
		nodes = HashBiMap.create();
	}
	
	@Override
	public void addSO(SOIntegerPair so){
		soList.add(so);
	}
	
	@Override
	public Integer fetchSOSize(){
		return new Integer(soList.size());
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

	@Override
	public void closeAll() {}

	@Override
	public void loadFromFile(String path) {
		// TODO Auto-generated method stub
		
	}
}
