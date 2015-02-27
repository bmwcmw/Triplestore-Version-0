package metaLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import constants.CTMConstants;
import queryObjects.BiList;
import localIOUtils.IOUtils;

/**
 * In block mode, loads metadata files to a specific database(normally inRam).
 * @author CMWT420
 */
public class LocalMetaLoader {
	public static enum MODE {S, O};
	
	//BiList<Long, MetaInfoTriple> b = new BiList<Long, MetaInfoTriple>();
	public static BiList<Long, MetaInfoTriple> loadMetadataFromFile(File folder, MODE mode) 
			throws IOException, ParseException {

		BiList<Long, MetaInfoTriple> bl = new BiList<Long, MetaInfoTriple>();
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		
		File md = null;
		BufferedReader reader = null;
		String line;
		int lineNumber = 0;
		
		String fileExt = null;
		String id = null;
		String offsetId = null;
		String offsetLine = null;
		String filePath = null;
		
		switch(mode){
			case S : 
				for(File f:listOfFiles){
					if(IOUtils.getExtension(f.getName()).equals(CTMConstants.MetadataSExt)){
						if(md != null){
							System.out.println("MetadataSExt duplicata.");
							System.exit(-1);
						}
						md = f;
					}
				}
				reader = new BufferedReader(new FileReader(md.getAbsolutePath()));
				break;
			case O : 
				for(File f:listOfFiles){
					if(IOUtils.getExtension(f.getName()).equals(CTMConstants.MetadataOExt)){
						if(md != null){
							System.out.println("MetadataOExt duplicata.");
							System.exit(-1);
						}
						md = f;
					}
				}
				reader = new BufferedReader(new FileReader(md.getAbsolutePath()));
				break;
			default : 
				return null;
		}
		
		filePath = md.getAbsolutePath();
		
		while ((line = reader.readLine()) != null) {
			++lineNumber;
			StringTokenizer itr = new StringTokenizer(line);
			// at least Four tokens
			// One - key
			if (itr.hasMoreTokens()) {
				fileExt = itr.nextToken();
				if (!fileExt.startsWith("#")) {
					// Two - id
					if (!itr.hasMoreTokens()) {
						throw new ParseException(filePath, lineNumber);
					}
					id = itr.nextToken();
					// Three - offsetId
					if (!itr.hasMoreTokens()) {
						throw new ParseException(filePath, lineNumber);
					}
					offsetId = itr.nextToken();
					// Four - offsetLine
					if (!itr.hasMoreTokens()) {
						throw new ParseException(filePath, lineNumber);
					}
					offsetLine = itr.nextToken();
				}
			}
			bl.add(Long.parseLong(id), new MetaInfoTriple(Long.parseLong(fileExt),Long.parseLong(offsetId),Integer.parseInt(offsetLine)));
		}
		
		if(reader != null){
			reader.close();
		}
		
		return bl;
	}

}