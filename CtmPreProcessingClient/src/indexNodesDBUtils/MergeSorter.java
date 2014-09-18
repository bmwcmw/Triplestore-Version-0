package indexNodesDBUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 1.Split a big file into N small files(smaller than the RAM requirement) <br>
 * 2.Sort small files <br>
 * 3.Merge small files into a big file
 * 
 * @author CMWT420
 * 
 */
public class MergeSorter {

	private File BIG_FILE;
	private String SORT_FILE_PATH;
	private int BIG_NUM_LINE;
	private int SMALL_FILE_LINE = 100000; 
	// About 1M for 1 small file, each line about 100bytes
	private File tempFiles[];

	private int sortC;
	private char delim;

	/**
	 * Constructor with all parameters.
	 * 
	 * @param toProcess
	 *            : the input file
	 * @param smallFileLines
	 *            : the line number of small chunk files
	 * @param sortCol
	 *            : the column used to sort the input file
	 *            (split by the delimiter)
	 * @param delimiter
	 *            : the delimiter between each two colons
	 * @throws IOException
	 */
	public MergeSorter(File toProcess, int smallFileLines, int sortCol, 
			char delimiter) throws IOException {
		BIG_FILE = toProcess;
		SORT_FILE_PATH = BIG_FILE.getAbsolutePath() + "_Sorting" + File.separator;
		File file = new File(SORT_FILE_PATH);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Folder created.");
			} else {
				System.out.println("Failed to create folder.");
			}
		}
		BIG_NUM_LINE = countLines(BIG_FILE);
		SMALL_FILE_LINE = smallFileLines;

		sortC = sortCol;
		delim = delimiter;
	}

	/**
	 * Default constructor : split into 100 chunks, using space character as delimitor
	 * 
	 * @throws IOException
	 */
	public MergeSorter(File toProcess) throws IOException {
		this(toProcess, countLines(toProcess)/100, 0, ' ');
	}
	
	/**
	 * Just beat it.
	 * @throws IOException
	 */
	public void beatIt() throws IOException {
		cutAndSort();
		mergeSortedSmall();
		File f = new File(SORT_FILE_PATH);
		f.delete();
	}

	/**
	 * Cuts the big file into small files
	 * 
	 * @throws IOException
	 */
	public void cutAndSort() throws IOException {
		BufferedReader bigDataFile = new BufferedReader(
				new FileReader(BIG_FILE));
		List<String> smallLine = null;
		int nbChunks = BIG_NUM_LINE / SMALL_FILE_LINE;
		int mod = BIG_NUM_LINE % SMALL_FILE_LINE;
		if(mod==0)
			tempFiles = new File[nbChunks];
		else
			tempFiles = new File[nbChunks + 1];
		for (int i = 0; i < tempFiles.length; i++) {
			tempFiles[i] = new File(SORT_FILE_PATH + "sortTempFile" + i
					+ ".temp");
//			/* BO DEBUG */
//			System.out.println(SORT_FILE_PATH + "sortTempFile" + i
//					+ ".temp");
//			/* EO DEBUG */
			BufferedWriter smallWtite = new BufferedWriter(new FileWriter(
					tempFiles[i]));
			smallLine = new ArrayList<String>();
			for (int j = 0; j < SMALL_FILE_LINE; j++) {
				String s = bigDataFile.readLine();
				if(s==null) break;
				smallLine.add(s);
			}
			Collections.sort(smallLine.subList(1, smallLine.size()));//84252137
			for (Object num : smallLine.toArray())
				smallWtite.write(num + "\n");
			smallWtite.close();
		}
		bigDataFile.close();
	}

	/**
	 * Sort and merge small files using the sortBySmallFile function.
	 * 
	 * @throws IOException
	 */
	public void mergeSortedSmall() throws IOException {
		File tempFile = null;
		for (int i = 1; i < tempFiles.length; i++) {
			tempFile = sortBySmallFile(tempFiles[0], tempFiles[i]);
			tempFiles[0].delete();
			tempFiles[0] = tempFile;
		}
		tempFile.renameTo(new File(BIG_FILE.getAbsolutePath() + ".sorted"));
		tempFile.delete();
	}

	/**
	 * If file A has n elements and B has m elements, - this function gets
	 * element 0 from A, then elements 0, 1, ... from B - if element 0 from A >
	 * element n from B, it writes element n from B to the new file, until
	 * element 0 from A < element n from B, then it writes element 0 of A to new
	 * file, and this loop continues until the end.
	 * 
	 * @param fromFile
	 * @param toFile
	 * @return New temporary file
	 * @throws IOException
	 */
	public File sortBySmallFile(File fromFile, File toFile) throws IOException {
		BufferedReader fromRd = new BufferedReader(new FileReader(fromFile));
		BufferedReader toTempRd = new BufferedReader(new FileReader(toFile));
		File newSortFile;
		if (countOccurrence(fromFile.getName(), ".temp") > 1) {
			newSortFile = new File(SORT_FILE_PATH
					+ fromFile.getName().substring(0,
							fromFile.getName().length() - 5));
		} else {
			newSortFile = new File(SORT_FILE_PATH + fromFile.getName()
					+ ".temp");
		}
//		/* BO DEBUG */
//		System.out.println("From : " + fromFile.getName() + " To : "
//				+ toFile.getName() + " NewSortFile : " + newSortFile.getName());
//		/* EO DEBUG */
		BufferedWriter newSortFileWt = new BufferedWriter(new FileWriter(
				newSortFile));
		String index = null;
		String toPoint = "";
		List<String> indexLineSet = null;
		List<String> toPointLineSet = new ArrayList<String>();
		for(int i=0;i<=sortC;i++){
			toPointLineSet.add("");
		}
//		/* BO DEBUG */
//		int lnFrom = countLines(fromFile);
//		int lnTo = countLines(toFile);
//		System.out.println("From:" + lnFrom + " " + "To:" + lnTo + " ");
//		/* EO DEBUG */
		
		while (fromRd.ready()) {
			index = fromRd.readLine();
			indexLineSet = tokenize(index);
			if (indexLineSet.get(sortC).compareTo(toPointLineSet.get(sortC)) < 0) {
				newSortFileWt.write(index);
				newSortFileWt.newLine();
				continue;
			} else {
				if (toPoint != "") {
					newSortFileWt.write(toPoint);
					newSortFileWt.newLine();
				}
			}
			while (toTempRd.ready()) {
				toPoint = toTempRd.readLine();
				toPointLineSet = tokenize(toPoint);
				if (toPointLineSet.get(sortC).compareTo(indexLineSet.get(sortC)) < 0) {
					newSortFileWt.write(toPoint);
					newSortFileWt.newLine();
				} else {
					newSortFileWt.write(index);
					newSortFileWt.newLine();
					break;
				}
			}
		}
		if(indexLineSet!=null && toPointLineSet!=null){
			newSortFileWt.write(String.valueOf(
					indexLineSet.get(sortC).compareTo(toPointLineSet.get(sortC))>0 
					? index : toPoint));
			newSortFileWt.newLine();
		}
		/* Write the rest */
		if(fromRd.ready() || toTempRd.ready()){
			@SuppressWarnings("resource")//Will be closed later
			BufferedReader toRead = (toTempRd.ready()) ? toTempRd : fromRd;
			String s = null;
			while((s = toRead.readLine()) != null){
				newSortFileWt.write(s);
				newSortFileWt.newLine();
			}
		} 
		
		newSortFileWt.close();
		fromRd.close();
		toTempRd.close();
		toFile.delete();

//		/* BO DEBUG */
//		int lnNew = countLines(newSortFile);
//		System.out.println("New:" + lnNew);
//		/* EO DEBUG */
		return newSortFile;
	}

	/**
	 * Counts the number of occurrence of a pattern in a string.
	 * 
	 * @param mStr
	 * @param pattern
	 * @return the number
	 */
	private int countOccurrence(String mStr, String pattern) {
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

	/**
	 * Counts the line number of a file. But "wc -l" is quicker than this...
	 * 
	 * @param filename
	 * @return number
	 * @throws IOException
	 */
	public static int countLines(File file) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	/**
	 * Tokenizes a string into a list.
	 * @param input
	 * @return the list
	 */
	private List<String> tokenize(String input) {
		List<String> output = new ArrayList<String>();
		int pos = 0, end;
		while ((end = input.indexOf(delim, pos)) >= 0) {
			output.add(input.substring(pos, end));
			pos = end + 1;
		}
		String last = input.substring(pos, input.length());
		if(last.length()>0)
			output.add(last);
		return output;
	}

}