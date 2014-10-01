package dataCompressor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 1.Split a big file into N small files(smaller than the RAM requirement)
 * 2.Sort small files
 * 3.Merge small files into a big file
 * @author CMWT420
 *
 */
public class BigDataSort {

	public static final String myFolder = System.getProperty("user.dir");
	public final static String SORT_FILE_PATH = myFolder + File.separator + "_SortBigFile" 
			+ File.separator;
	private int BIG_NUM_LINE = 10000000; // Generate file's size eg. 10000000
	public final static String ORIG_FILE_PATH = SORT_FILE_PATH + File.separator
			+ "BIGFILE";
	private int SMALL_FILE_LINE = 100000; // About 1M for 1 small file
	private File tempFiles[];

	/**
	 * Constructor
	 * @throws IOException
	 */
	public BigDataSort(int bnl, int sfl) throws IOException {
		File file = new File(SORT_FILE_PATH);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Folder created.");
			} else {
				System.out.println("Failed to create folder.");
			}
		}
		BIG_NUM_LINE = bnl;
		SMALL_FILE_LINE = sfl;
	}
	
	/**
	 * Constructor
	 * @throws IOException
	 */
	public BigDataSort() throws IOException {
		this(10000000, 100000);
	}
	
	/**
	 * Create a big file for test purpose
	 * @throws IOException
	 */
	public void createBigsortNums() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				ORIG_FILE_PATH));
		Random random = new Random();
		for (int i = 0; i < BIG_NUM_LINE; i++) {
			writer.write(String.valueOf(random.nextInt(100000000)));
			writer.newLine();// add a new line . in order to show easy by file
		}
		writer.close();
	}

	/**
	 * Cut the big file into small files
	 * @throws IOException
	 */
	public void beSmallFileAndSort() throws IOException {
		BufferedReader bigDataFile = new BufferedReader(new FileReader(
				ORIG_FILE_PATH));
		List<Integer> smallLine = null;
		tempFiles = new File[BIG_NUM_LINE / SMALL_FILE_LINE];
		for (int i = 0; i < tempFiles.length; i++) {
			tempFiles[i] = new File(SORT_FILE_PATH + "sortTempFile" + i
					+ ".temp");
			BufferedWriter smallWtite = new BufferedWriter(new FileWriter(
					tempFiles[i]));
			smallLine = new ArrayList<Integer>();
			for (int j = 0; j < SMALL_FILE_LINE; j++)
				smallLine.add(Integer.parseInt(bigDataFile.readLine()));
			Collections.sort(smallLine);
			for (Object num : smallLine.toArray())
				smallWtite.write(num + "\n");
			smallWtite.close();
		}
		bigDataFile.close();
	}

	/**
	 * Sort and merge small files using the sortBySmallFile function.
	 * @throws IOException
	 */
	public void unitFileToSort() throws IOException {
		File tempFile = null;
		for (int i = 1; i < tempFiles.length; i++) {
			tempFile = sortBySmallFile(tempFiles[0], tempFiles[i]);
			tempFiles[0].delete();
			tempFiles[0] = tempFile;

			// String tempName = tempFiles[0].getAbsolutePath();
			// tempName = tempName.substring(0, tempName.lastIndexOf("."));
			// tempName = tempName.substring(0, tempName.lastIndexOf("."));
			// System.out.println("To rename : " + tempName);
			// if(tempFiles[0].renameTo(new File(tempName))){
			// System.out.println("Rename succesful");
			// } else {
			// System.out.println("Rename failed");
			// }
			// System.out.println(tempFiles[0].getAbsolutePath());
		}
		tempFile.renameTo(new File(ORIG_FILE_PATH + "sortResult.txt"));
	}

	/**
	 * If file A has n elements and B has m elements,
	 * - this function gets element 0 from A, then elements 0, 1, ... from B
	 * - if element 0 from A > element n from B, it writes element n from B to the new file, until
	 *   element 0 from A < element n from B, then it writes element 0 of A to new file, and this
	 *   loop continues until the end.
	 * @param fromFile
	 * @param toFile
	 * @return New temporary file
	 * @throws IOException
	 */
	public static File sortBySmallFile(File fromFile, File toFile)
			throws IOException {
		BufferedReader fromRd = new BufferedReader(new FileReader(fromFile));
		BufferedReader toTempRd = new BufferedReader(new FileReader(toFile));
		File newSortFile;
		if(countOccurrence(fromFile.getName(), ".temp")>1){
			newSortFile = new File(SORT_FILE_PATH 
					+ fromFile.getName().substring(0, fromFile.getName().length()-5));
		} else {
			newSortFile = new File(SORT_FILE_PATH + fromFile.getName()
					+ ".temp");
		}
		System.out.println("From : " + fromFile.getName() + " To : "
				+ toFile.getName() + " NewSortFile : " + newSortFile.getName());
		BufferedWriter newSortFileWt = new BufferedWriter(new FileWriter(
				newSortFile));
		int index = -1;
		int toPoint = -1;
		while (fromRd.ready()) {
			index = Integer.parseInt(fromRd.readLine());
			if (index < toPoint) {
				newSortFileWt.write(String.valueOf(index));
				newSortFileWt.newLine();
				continue;
			} else {
				if (toPoint != -1) {
					newSortFileWt.write(String.valueOf(toPoint));
					newSortFileWt.newLine();
				}
			}
			while (toTempRd.ready()) {
				toPoint = Integer.parseInt(toTempRd.readLine());
				if (toPoint < index) {
					newSortFileWt.write(String.valueOf(toPoint));
					newSortFileWt.newLine();
				} else {
					newSortFileWt.write(String.valueOf(index));
					newSortFileWt.newLine();
					break;
				}
			}
		}
		newSortFileWt.write(String.valueOf(index > toPoint ? index : toPoint));
		newSortFileWt.newLine();
		newSortFileWt.close();
		fromRd.close();
		toTempRd.close();
		toFile.delete();
		return newSortFile;
	}

	/**
	 * Counts the number of occurrence of a pattern in a string.
	 * @param mStr
	 * @param pattern
	 * @return the number
	 */
	private static int countOccurrence(String mStr, String pattern) {
		int lastIndex = 0;
		int count = 0;
		while (lastIndex != -1) {
			lastIndex = mStr.indexOf(pattern, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += pattern.length();
			}
		}
		return count;
	}
}