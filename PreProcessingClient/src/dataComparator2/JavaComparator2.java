package dataComparator2;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import dataReader.PairReader;
import localIOUtils.IOUtils;

/**
 * <p>Compares two SORTED, UNIQUE OR NOT string files, returns the number of common entries.</p>
 * <p>Using Java method.</p>
 * 
 * @author CMWT420
 *
 */
public class JavaComparator2 {

	public final static int S = 0;
	public final static int O = 1;
	
	public static void main(String[] args) {
		if(args.length!=4){
			System.out.println(" -[file 1 with sorted column] "
					+ "-[file 2 with sorted column] "
					+ "-[sorted column number] "
					+ "-[output folder]");
			System.exit(-1);
		}
		
		//TODO
	}
	
	public Long compareTwoPredicates(File f1, File f2)
			throws IOException, ParseException, InterruptedException, ExecutionException {
		IOUtils.logLog("Start comparison");
		PairReader reader1 = new PairReader(f1);
		PairReader reader2 = new PairReader(f2);
		long common = 0;
		String entry1 = reader1.nextLine();
		String temp1;
		String entry2 = reader2.nextLine();
		String temp2;
		while (true) {
			if ((entry1 == null) || (entry2 == null)) {
				return common;
			}
			//System.out.println(entry1+" "+entry2);
			if (entry1.compareTo(entry2) < 0){
				while ((temp1 = reader1.nextLine()) != null) {
					if(!temp1.equals(entry1)) break;
				}
				//System.out.println(temp1);
				entry1 = temp1;
			} else if (entry1.compareTo(entry2) == 0) {
				while ((temp1 = reader1.nextLine()) != null) {
					if(!temp1.equals(entry1)) break;
				}
				entry1 = temp1;
				while ((temp2 = reader2.nextLine()) != null) {
					if(!temp2.equals(entry2)) break;
				}
				entry2 = temp2;
				common++;
			} else {
				while ((temp2 = reader2.nextLine()) != null) {
					if(!temp2.equals(entry2)) break;
				}
				//System.out.println(temp2);
				entry2 = temp2;
			}
		}
	}
}
