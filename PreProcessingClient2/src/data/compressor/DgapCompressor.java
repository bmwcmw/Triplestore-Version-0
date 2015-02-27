package data.compressor;


import localIOUtils.IOUtils;
import data.compressor.utils.DBImpl2;


/**
 * <p>Data compressor using D-gap</p>
 * <p>Calls the BitMat compressor of different DB units</p>
 * 
 * @author CMWT420
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
