package ctmNode;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import dbUtils.DBImpl;
import dbUtils.InRamDBUtils;

public class NodeListener {
	
	private static NodeListenerConfig myConfig;

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
		myConfig = NodeListenerConfig.getInstance();
		
		DBImpl dbu = new InRamDBUtils();
		String loadPath = myConfig._workingDir + File.separator + ".." + File.separator + 
				"CtmPreProcessingClient" + File.separator + "_compressed";
		dbu.loadMetaFromFile(loadPath);
		System.out.println(dbu.fetchLoadedMetaSize());
		
		dbu.getFileLineNumber("a", "SO", (long)0, (long)1);
	}

}
