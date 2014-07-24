package queryExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Read lines from matrix files in local file system.
 * @author CMWT420
 *
 */
public class MatrixLineReaderLocalFS {

	/**
	 * Read lines from matrix files in local file system.
	 * @param id : ID of S or O, also number of line
	 * @param f : File
	 * @return A string containing the wanted information
	 * @throws IOException
	 */
	public static String readLocalLine(long id, File f) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(f)));
		for (long i = 0; i < id; i++)
			br.readLine();
		String line = br.readLine();
		if (br != null)
			br.close();
		return line;
	}
	
}
