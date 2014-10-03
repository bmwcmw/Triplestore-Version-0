package dataCompressor;


import localIOUtils.IOUtils;
import dataCompressorUtils.DBImpl2;


/**
 * <p>Data compressor using D-gap</p>
 * <p>Calls the BitMat compressor of different DB units</p>
 * 
 * @author Cedar
 *
 */
public class DgapCompressor {
	
	public static void writeCompressedFormat(String inFileName, 
			String outputPath, DBImpl2 dbu) throws Exception{
		
//    	/* Write compressed predicate to file */
//		dbu.writePredToFile(inFileName, outputPath);
		IOUtils.logLog("Please use external script...");
	}
	
}
