package dataComparator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dataCleaner.CTMDoubleStr;
import dataReader.SOReader;
import localIOUtils.IOUtils;

/**
 * Compares two 
 * @author Cedar
 */
public class Comparator {

	public final static int S = 0;
	public final static int O = 1;
	
	public static long compareTwoPredicates(File f1, File f2)
			throws IOException, ParseException, InterruptedException, ExecutionException {
		IOUtils.logLog("Start loading");
		SOReader reader1 = new SOReader(f1);
		SOReader reader2 = new SOReader(f2);
		long commun = 0;
		String entry1 = null;
		String entry2 = null;
		while (true) {
			if ( ((entry1 = reader1.nextLine()) == null) 
					&& ((entry2 = reader2.nextLine()) == null)){
				return commun;
			}
			
		}
	}
}
