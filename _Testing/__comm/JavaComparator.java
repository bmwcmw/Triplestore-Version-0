package dataComparator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import dataReader.SOReader;
import localIOUtils.IOUtils;

/**
 * <p>Compares two SORTED, UNIQUE OR NOT string files, returns the number of common entries.</p>
 * <p>Using Java method.</p>
 * 
 * @author CMWT420
 *
 */
public class JavaComparator {

	public final static int S = 0;
	public final static int O = 1;
	
	public static main(){
		File f1 = new File("ot1.out");
		File f1 = new File("ot2.out");
		System.out.println();
	}
	
	public static Long compareTwoPredicates(File f1, File f2)
			throws IOException, ParseException, InterruptedException, ExecutionException {
		IOUtils.logLog("Start comparison");
		SOReader reader1 = new SOReader(f1);
		SOReader reader2 = new SOReader(f2);
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
