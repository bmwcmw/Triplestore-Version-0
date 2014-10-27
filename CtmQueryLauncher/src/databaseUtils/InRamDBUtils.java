package databaseUtils;

import java.io.IOException;
import java.text.ParseException;
import localIOUtils.IOUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dataCleaner.RDFPairStr;
import dataReader.PairReader;

public class InRamDBUtils implements DBImpl{
	private BiMap<Long, String> nodes;
	
	public InRamDBUtils(){
		nodes = HashBiMap.create();
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
	}

	@Override
	public void closeAll() {}

	@Override
	public void loadIndexFromFile(String path) {
		try {
			PairReader reader = new PairReader(path);
			RDFPairStr pair = null;
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
		return (long)nodes.size();
	}

}
