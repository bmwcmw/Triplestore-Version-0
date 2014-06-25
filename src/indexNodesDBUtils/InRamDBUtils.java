package indexNodesDBUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import localIOUtils.IOUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dataCleaner.CTMPairStr;
import dataCompressor.SOIntegerPair;
import dataReader.PairReader;

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
		return soList.size();
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
		try {
			PairReader reader = new PairReader(path);
			CTMPairStr pair = null;
			while ((pair = reader.nextStr()) != null) {
				nodes.put(Integer.valueOf(pair.getSubject()), pair.getObject());
			}
		} catch (IOException e) {
			e.printStackTrace();
			cleanAll();
		} catch (ParseException e) {
			e.printStackTrace();
			cleanAll();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			cleanAll();
		} finally {
		}
		IOUtils.logLog("File charged. Current size of key-value pair(s) : " + fetchLoadedSize());
	}

	@Override
	public Long fetchLoadedSize() {
		return new Long(fetchIndexSize());
	}
}
