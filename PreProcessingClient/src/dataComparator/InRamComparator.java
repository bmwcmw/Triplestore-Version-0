package dataComparator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import localio.IOUtils;

/**
 * This one loads all data into HashSet then obtains a HashSet of all common nodes. 
 * @author CMWT420
 */
public class InRamComparator {

	public final static int S = 0;
	public final static int O = 1;
	
	public static long compareTwoPredicates(File f1, File f2)
			throws IOException, ParseException, 
			InterruptedException, ExecutionException {
		IOUtils.logLog("Start loading");
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Future<HashSet<String>> fut1 = executor.submit(
				new FileLoader(f1.getAbsolutePath()));
		Future<HashSet<String>> fut2 = executor.submit(
				new FileLoader(f2.getAbsolutePath()));
		HashSet<String> set1 = fut1.get();
		HashSet<String> set2 = fut2.get();
		executor.shutdown();
		return compareTwoHashSets(set1, set2);
	}
	
	private static int compareTwoHashSets(HashSet<String> s1, 
			HashSet<String> s2){
		//small.retainAll(large);
		if(s1.size()<s2.size()){
			int bigsize = s2.size();
			int oldsize = s1.size();
			s1.retainAll(s2);
			return bigsize - oldsize;
		} else {
			int bigsize = s1.size();
			int oldsize = s2.size();
			s2.retainAll(s1);
			return bigsize - oldsize;
		}
	}
}
