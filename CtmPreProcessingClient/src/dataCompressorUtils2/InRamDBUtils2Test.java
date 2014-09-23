package dataCompressorUtils2;

import java.io.File;

import ctmRdf.CTMConstants;

public class InRamDBUtils2Test {
	public final static String _workingDir = System.getProperty("user.dir");
	
	public static void main(String[] args) throws Exception {
		String outputPath = _workingDir + File.separator + "_compressed";
		
//		String inputFilePath = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
//				+ File.separator + "_psFake_sorted" + File.separator + "hasAuthor" + CTMConstants.SOSortedExt;
//		InRamDBUtils2 dbu = new InRamDBUtils2(inputFilePath, outputPath);
//		dbu.loadAuxIndexFromFile();
//		dbu.compress(true);
//		
//		String inputFilePath2 = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
//				+ File.separator + "_psFake_sorted" + File.separator + "hasAuthor" + CTMConstants.OSSortedExt;
//		InRamDBUtils2 dbu2 = new InRamDBUtils2(inputFilePath2, outputPath);
//		dbu2.loadAuxIndexFromFile();
//		dbu2.compress(false);
		
		String inputFilePath = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
				+ File.separator + "_ps20G_sorted" + File.separator + "-telephone" + CTMConstants.SOSortedExt;
		InRamDBUtils2 dbu = new InRamDBUtils2(inputFilePath, outputPath);
		dbu.loadAuxIndexFromFile();
		dbu.compress(true);

		String inputFilePath2 = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
				+ File.separator + "_ps20G_sorted" + File.separator + "-telephone" + CTMConstants.OSSortedExt;
		InRamDBUtils2 dbu2 = new InRamDBUtils2(inputFilePath2, outputPath);
		dbu2.loadAuxIndexFromFile();
		dbu2.compress(false);
		
	}

}
