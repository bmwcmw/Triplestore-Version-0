package localIOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Static IO tools for the preprocessing program.
 * 
 * @author CMWT420
 */
public class IOUtils {
	private static final String WORKINGDIR = System.getProperty("user.dir");
	private static String _logFileName;
	private static boolean _loggingConsole = true;
	private static boolean _loggingFile = true;
	
	/**
	 * Tells StandAloneIOTools which messages to output
	 * @param setConsole : into the console or not
	 * @param setFile : into an automatically created log file or not
	 */
	public static void setLogging(boolean setConsole, boolean setFile){
		_loggingConsole = setConsole;
		_loggingFile = setFile;
	}
	
	/**
	 * Once setLoggingFile(true) is set, it prints the message not only in the console but also in 
	 * a log file.
	 * @param msg : message to print
	 */
	public static void logLog(Object msg){
		if(_loggingConsole){
			System.out.println(msg);
		}
		if(_loggingFile){
			_logFileName = WORKINGDIR + File.separator 
					+ new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()) 
					+ ".log";
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(_logFileName, true)));
				out.println(
						new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) 
						+ " " + msg);
			} catch (IOException e) {
				System.out.println("UNABLE TO WRITE INTO LOG FILE");
			} finally {
				if(out != null){
					out.close();
			    }
			}
		}
	}
	
	/**
	 * Deletes recursively a folder.
	 * @param directory : the object
	 * @return true if and only if the file or directory is successfully deleted; false otherwise.
	 */
	public static boolean deleteDirectory(File directory) {
	    if(directory.exists()){
	    	System.out.println("Processing : "+directory.getAbsolutePath());
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(directory.delete());
	}
	
	/**
	 * Replace the extension of a file name
	 * 
	 * @param originalName
	 * @return filename with changed extension or filename with added
	 *         extension(if it doesn't have one before)
	 */
	public static String changeExtension(String originalName, String newExt) {
		int lastDot = originalName.lastIndexOf(".");
		if (lastDot != -1) {
			return originalName.substring(0, lastDot) + newExt;
		} else {
			return originalName + newExt;
		}
	}
	
	/**
	 * Get the extension of a file name
	 * 
	 * @param originalName
	 * @return the extension (including the dot) or nothing (if the file doesn't have any extension)
	 */
	public static String getExtension(String originalName) {
		int lastDot = originalName.lastIndexOf(".");
		if ((lastDot == -1) || (lastDot == (originalName.length()-1))) {
			return "";
		} else {
			return originalName.substring(lastDot, originalName.length());
		}
	}
	
	/**
	 * Returns a filename without extension
	 * 
	 * @param name
	 * @return filename
	 */
	public static String filenameWithoutExt(String name){
		return name.replaceFirst("[.][^.]+$", "");
	}
	
	/**
	 * Returns an ArrayList of all files in a specified folder
	 * @return list of File if no error, otherwise null
	 * @param foldername
	 * @throws IOException 
	 */
	public static ArrayList<File> loadFolder(String foldername) throws IOException {
		File folder = new File(foldername);
		ArrayList<File> result = new ArrayList<File>();

		if (folder.isDirectory() && folder.canRead()) {
			for (File file : folder.listFiles()) {
				if (file.isFile()) {
					result.add(file);
				}
			}
		} else {
			IOUtils.logLog("Error folder doesn't exist or cannot be read : "
					+ foldername);
			return null;
		}
		return result;
	}
	
	/**
	 * Merges a list of files into one
	 * @param files : input
	 * @param mergedFile : output
	 * @throws IOException 
	 */
	public static void mergeFiles(File[] files, File mergedFile) throws IOException {
		System.out.println(files.length);
		FileWriter fstream = new FileWriter(mergedFile, true);
		BufferedWriter out = new BufferedWriter(fstream);
		for (File f : files) {
			IOUtils.logLog("Merging: " + f.getAbsolutePath());
			FileInputStream fis;
			fis = new FileInputStream(f);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 			String aLine;
			while ((aLine = in.readLine()) != null) {
				out.write(aLine);
				out.newLine();
			}
			in.close();
		}
		out.close();
	}
	

	/**
	 * Checks if a folder exists, if not, creates one
	 * @param path
	 */
	public static void checkOrCreateFolder(String path) {
		File checkFolder = new File(path);
		if (!checkFolder.exists()) {
			checkFolder.mkdirs();
		}
	}
	
	/**
	 * Count the number of lines in a text file
	 * @param filename
	 * @return int Number of lines
	 * @throws IOException
	 */
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
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
	 * Reads all text from a file 
	 * @param path
	 * @param encoding
	 * @return String
	 * @throws IOException
	 */
	public static String fileToString(String filePath, Charset encoding) 
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(filePath));
		return new String(encoded, encoding);
	}
}
