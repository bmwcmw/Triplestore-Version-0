package ctmNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;

import queryObjects.BiList;

import ctmRdf.CTMConstants;

import localIOUtils.IOUtils;

/**
 * Try to load meta information file of compressed predicate (block mode).
 * 
 * @author CEDAR
 * 
 */
public class MetaFileLoader {
	/** 
	 * Detects and loads meta-data of ONE compressed predicate files (block mode)
	 * <p>Remember : in each line of metadata file,
	 * Number of file, First occured S/O's ID, Offset(ID) and offset(line number).</p>
	 * <p>Here we have, for each predicate, zero to two BTrees (SO and/or OS or neither), containing
	 * which ID is in which file block. What's more, since an S or O line can be cut due to its 
	 * length, the O/S offsets of these lines are also noted, so that : 
	 * <br>
	 * - for the SPARQL queries with variable(s)(?x pred ?y), each time we can load a whole S or O 
	 * line from SO or OS matrix
	 * <br>
	 * - for the SPARQL queries without variable(X pred Y), 
	 * </p>
	 */
	public static BiList<Long, MetaInfoTriple> load(String metaFileFolder) throws IOException, ParseException {
		File folder = new File(metaFileFolder);
		File[] listOfFiles = folder.listFiles();
		BufferedReader reader = null;
		if (listOfFiles != null) {
			BiList<Long, MetaInfoTriple> b = new BiList<Long, MetaInfoTriple>();
			for (File f : listOfFiles) {
				if(f.isFile() && 
						IOUtils.getExtension(f.getName()).equals(CTMConstants.MetadataExt)){
					//System.out.println(f.getName());
					reader = new BufferedReader(new FileReader(f));
					String line;
					int lineNb = 0;
					while ((line = reader.readLine()) != null) {
						StringTokenizer itr = new StringTokenizer(line);
						//At least 4 tokens
						if (!itr.hasMoreTokens()) {
							if (reader != null) reader.close();
							throw new ParseException(f.getPath(), lineNb);
						}
						String fIdStr = itr.nextToken();
						int fId = Integer.valueOf(fIdStr);
						if (!itr.hasMoreTokens()) {
							if (reader != null) reader.close();
							throw new ParseException(f.getPath(), lineNb);
						}
						String soIdStr = itr.nextToken();
						long soId = Long.valueOf(soIdStr);
						if (!itr.hasMoreTokens()) {
							if (reader != null) reader.close();
							throw new ParseException(f.getPath(), lineNb);
						}
						String soOffsetIDStr = itr.nextToken();
						long soOffsetID = Long.valueOf(soOffsetIDStr);
						if (!itr.hasMoreTokens()) {
							if (reader != null) reader.close();
							throw new ParseException(f.getPath(), lineNb);
						}
						String soOffsetLineStr = itr.nextToken();
						int soOffsetLine = Integer.valueOf(soOffsetLineStr);
//						System.out.println("In file " + f.getName() + " " + fId + " " + soId + " " 
//								+ soOffsetID + " " + soOffsetLine);
						b.add(soId, new MetaInfoTriple(fId, soOffsetID, soOffsetLine));
						
						lineNb++;
					}
					if (reader != null) reader.close();
				}
			}
			return b;
		} else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
			return null;
		}
	}

}
