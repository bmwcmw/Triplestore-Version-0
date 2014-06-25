package dataComparator;

import java.util.HashSet;
import java.util.concurrent.Callable;

import localIOUtils.IOUtils;
import dataReader.PairReader;

public class FileLoader implements Callable<HashSet<String>> {
	
	private String filename;
	
	public FileLoader(String fn){
		filename = fn;
	}
	
	@Override
	public HashSet<String> call() throws Exception {
		IOUtils.logLog("Loading " + filename);
		PairReader reader = new PairReader(filename);
		String line = null;
		HashSet<String> hset = new HashSet<String>(220000,0.80F);
		while ((line = reader.nextLine()) != null) {
			hset.add(line);
		}
		IOUtils.logLog("Loaded : " + filename);
		return hset;
	}

}
