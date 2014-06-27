package indexNodesDBUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import localIOUtils.IOUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dataCleaner.CTMPairStr;
import dataCompressor.SOLongPair;
import dataReader.PairReader;

public class InRamDBUtils implements COMMImpl{
	private BiMap<Long, String> nodes;
	private ArrayList<SOLongPair> soList = new ArrayList<SOLongPair>();

	
	public InRamDBUtils(){
		nodes = HashBiMap.create();
	}
	
	@Override
	public void addSO(SOLongPair so){
		soList.add(so);
	}
	
	@Override
	public Long fetchSOSize(){
		return new Long(soList.size());
	}
	
	@Override
    public Long fetchIndexSize(){
    	return new Long(nodes.size());
    }

	@Override
    public void insertNode(String node){
		if(fetchIdByNode(node)==null){
			long newid = nodes.size();
	    	nodes.put(newid, node);
		}
    }

	@Override
	public Long fetchIdByNode(String node){
		return nodes.inverse().get(node);
	}

	@Override
	public String fetchNodeById(Long index){
		return nodes.get(index);
	}
	
	@Override
	public void cleanAll(){
		nodes = HashBiMap.create();
		soList = new ArrayList<SOLongPair>();
	}
	
	@Override
	public BiMap<Long, String> fetchIndex(){
		return nodes;
	}
	
	@Override
	public ArrayList<SOLongPair> fetchSOList(){
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
				nodes.put(Long.valueOf(pair.getSubject()), pair.getObject());
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
