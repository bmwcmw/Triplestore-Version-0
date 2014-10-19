package metaLoader;

import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;

import ctmRdf.CTMConstants;

/**
 * In block mode, loads metadata files to a specific database(normally inRam).
 * @author CEDAR
 */
public class LocalMetaLoader {
	
	public static enum MODE {S, O};
	
	public static HashMap<String, MetaInfoTriple> loadMetadataFromFile(File folder, MODE mode) {
		HashMap<String, MetaInfoTriple> newMap = new HashMap<String, MetaInfoTriple>();
		switch(mode){
		case S : 
			//TODO
			//CTMConstants.MetadataSExt
			break;
		case O : 
			//TODO
			//CTMConstants.MetadataOExt
			break;
		default : 
			return null;
		}
		return newMap;
	}

}