package indexNodesDBUtils;

import java.io.IOException;

import dataCompressor.SOLongPair;

public class MongoDBUtils implements DBImpl{
    
	public MongoDBUtils() {
		//TODO
	}

	@Override
	public void addSO(SOLongPair so) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long fetchSOSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long fetchIndexSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertNode(String node) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadIndexFromFile(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long fetchLoadedSize() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void writePredToFile(String inFileName, String outputFilePath, String comparePath) 
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
