package ctmNode;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import dataNodeDbUtils.DBImpl;
import dataNodeDbUtils.InRamDBUtils;

/**
 * Listener program entry point
 * @author CMWT420
 *
 */
public class NodeListener {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParseException {
		/* TODO 
		 * - Socket function in while loop? to receive requests 
		 * - Detection of predicates and DBUtils to load files
		 * - Response message
		 */
		
		DBImpl dbu = new InRamDBUtils();
		String loadPath = NodeListenerConfig.getWorkingDir() + File.separator + ".." + File.separator + 
				"CtmPreProcessingClient2" + File.separator + "_compressed";
		dbu.loadMetaFromFile(loadPath);
		System.out.println(dbu.fetchLoadedMetaSize());
		
		dbu.getFileLineNumber("a", "SO", (long)0, (long)1);
	}

}
