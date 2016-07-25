package data.comparator2;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import constants.AppConstants;
import data.cleaner.RDFPairStr;
import data.reader.PairReader;
import localio.IOUtils;

/**
 * <p>Compares two SORTED, UNIQUE OR NOT string files, returns the number of common entries.</p>
 * <p>Using Java method.</p>
 * 
 * @author CMWT420
 *
 */
public class JavaComparator2 {
	
	public static void main(String[] args) throws IOException, ParseException {
		if(args.length!=4){
			System.out.println(" -[file 1 with sorted column] "
					+ "-[file 2 with sorted column] "
					+ "-[sorted column number] "
					+ "-[output folder]");
			System.exit(-1);
		}
		
//		File f1 = new File(System.getProperty("user.dir") + File.separator + "2.sortedSO");
//		File f2 = new File(System.getProperty("user.dir") + File.separator + "2.sortedOS");
//		JavaComparator2 jc2 = new JavaComparator2();
//		System.out.println(jc2.compareTwoPredicates(f1,f2));
	}
	
	public Long compareTwoPredicates(File f1, File f2) throws IOException, ParseException {
		IOUtils.logLog("Start comparison");
		PairReader reader1 = new PairReader(f1);
		PairReader reader2 = new PairReader(f2);
		long common = 0;
		RDFPairStr entry1 = reader1.nextStr();
		RDFPairStr temp1;
		RDFPairStr entry2 = reader2.nextStr();
		RDFPairStr temp2;
		
		int sortedCol = 0;//default is AppConstants.SOSortedExt
		if(IOUtils.getExtension(f1.getName()) == AppConstants.OSSortedExt)
			sortedCol = 1;
		if (sortedCol==0) {
			while (true) {
				if ((entry1 == null) || (entry2 == null)) {
					return common;
				}
				//System.out.println(entry1+" "+entry2);
				if (entry1.getSubject().compareTo(entry2.getSubject()) < 0){
					while ((temp1 = reader1.nextStr()) != null) {
						if(!temp1.getSubject().equals(entry1.getSubject())) break;
					}
					//System.out.println(temp1);
					entry1 = temp1;
				} else if (entry1.getSubject().compareTo(entry2.getSubject()) == 0) {
					while ((temp1 = reader1.nextStr()) != null) {
						if(!temp1.getSubject().equals(entry1.getSubject())) break;
					}
					entry1 = temp1;
					while ((temp2 = reader2.nextStr()) != null) {
						if(!temp2.getSubject().equals(entry2.getSubject())) break;
					}
					entry2 = temp2;
					common++;
				} else {
					while ((temp2 = reader2.nextStr()) != null) {
						if(!temp2.getSubject().equals(entry2.getSubject())) break;
					}
					//System.out.println(temp2);
					entry2 = temp2;
				}
			}
		} else {
			while (true) {
				if ((entry1 == null) || (entry2 == null)) {
					return common;
				}
				//System.out.println(entry1+" "+entry2);
				if (entry1.getObject().compareTo(entry2.getObject()) < 0){
					while ((temp1 = reader1.nextStr()) != null) {
						if(!temp1.getObject().equals(entry1.getObject())) break;
					}
					//System.out.println(temp1);
					entry1 = temp1;
				} else if (entry1.getObject().compareTo(entry2.getObject()) == 0) {
					while ((temp1 = reader1.nextStr()) != null) {
						if(!temp1.getObject().equals(entry1.getObject())) break;
					}
					entry1 = temp1;
					while ((temp2 = reader2.nextStr()) != null) {
						if(!temp2.getObject().equals(entry2.getObject())) break;
					}
					entry2 = temp2;
					common++;
				} else {
					while ((temp2 = reader2.nextStr()) != null) {
						if(!temp2.getObject().equals(entry2.getObject())) break;
					}
					//System.out.println(temp2);
					entry2 = temp2;
				}
			}
		}
	}
}
