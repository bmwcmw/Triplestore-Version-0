package matrixLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

/**
 * Read specified lines from matrix files in local file system.
 * @author CEDAR
 *
 */
public class MatrixLineReaderLocalFS {

	/**
	 * Reads one line from the beginning of matrix files in local file system.
	 * @param id : number of line
	 * @param f : File
	 * @return A string containing the wanted information
	 * @throws IOException
	 */
	public static String headOneLine(long id, File f) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(f)));
		for (long i = 0; i < id; i++)
			br.readLine();
		String line = br.readLine();
		if (br != null)
			br.close();
		return line;
	}
	

	/**
	 * Returns the Nth line of the file from the end.
	 * @param file
	 * @param lines : number of wanted line
	 * @return wanted line in one string
	 */
	public static String tailOneLine(long lines, File file) {
		lines = lines*2;
	    java.io.RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = new java.io.RandomAccessFile(file, "r");
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();
	        long line = 0;

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	            // LF - Line Feed
	            if( readByte == 0xA ) {
	                line = line + 1;
	            	//System.out.println("0xA"+line);
	                if (line == lines) {
	                    if (filePointer == fileLength) {
	                        continue;
	                    }
	                    break;
	                }
	            } 
	            // CR - Carriage Return : to check the last line is empty or not
	            else if( readByte == 0xD ) {
	                line = line + 1;
	            	//System.out.println("0xD"+line);
	                if (line == lines) {
	                    if (filePointer == fileLength - 1) {
	                        continue;
	                    }
	                    break;
	                }
	            }
	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        //String lastLine = "";
	        //Eliminate empty first line
	        if(lastLine.length()>0 && lastLine.substring(0, 1).matches("\n")) 
	        	lastLine = lastLine.substring(1, lastLine.length());
	        //Get first line then
	        if(lastLine.length()>0)
	        	lastLine = lastLine.substring(0, lastLine.indexOf("\n")-1);
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    }
	    finally {
	        if ( fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	                /* WTF */
	            }
	    }
	}
	
	
	/**
	 * Returns the last N lines of the file without stepping through the entire file
	 * @param file
	 * @param lines : number of lines
	 * @return lines in one string
	 */
	public static String tailNLines(long lines, File file) {
		lines = lines*2;
	    RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = new RandomAccessFile(file, "r");
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();
	        long line = 0;

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	            // LF - Line Feed
	            if( readByte == 0xA ) {
	                line = line + 1;
	            	//System.out.println("0xA"+line);
	                if (line == lines) {
	                    if (filePointer == fileLength) {
	                        continue;
	                    }
	                    break;
	                }
	            } 
	            // CR - Carriage Return : to check the last line is empty or not
	            else if( readByte == 0xD ) {
	                line = line + 1;
	            	//System.out.println("0xD"+line);
	                if (line == lines) {
	                    if (filePointer == fileLength - 1) {
	                        continue;
	                    }
	                    break;
	                }
	            }
	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        //String lastLine = "";
	        //Eliminate empty first line
	        if(lastLine.length()>0 && lastLine.substring(0, 1).matches("\n")) 
	        	lastLine = lastLine.substring(1, lastLine.length());
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	        e.printStackTrace();
	        return null;
	    }
	    finally {
	        if ( fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	                /* WTF */
	            }
	    }
	}
	
}
