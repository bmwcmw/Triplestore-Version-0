package dataCompressorUtils2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import localIOUtils.IOUtils;
import ctmRdf.CTMConstants;

public class InRamDBUtils2Test {
	public final static String _workingDir = System.getProperty("user.dir");
	
	public static void main(String[] args) throws Exception {

		/* STANDARD */
		System.setProperty("true","true");
		System.out.println("Input file : " + args[0]);
		System.out.println("Output path : " + args[1]);
		InRamDBUtils2 dbu = new InRamDBUtils2(args[0], args[1]);
		boolean auxIndex = Boolean.getBoolean(args[2]);
		System.out.println("Output auxiliary index ? " + auxIndex);
		dbu.loadAuxIndexFromFile();
		dbu.compress(auxIndex);
		
////		CTMServerConfig myconfig = CTMServerConfig.getInstance();
////		myconfig._nbThreads = 16;
//		String psPath = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
//				+ File.separator + "_psFake_sorted" + File.separator;
//		File folder = new File(psPath);
//		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
////		ArrayList<ArrayList<File>> inputLists = CTMJobAssigner.assignJobs(listOfFiles, false);
//		
		
		/* TESTING SINGLE */
//		String outputPath = _workingDir + File.separator + "_compressed";
//		
//		String inputFilePath = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
//				+ File.separator + "_ps26M_sorted" + File.separator + "hasAuthor" + CTMConstants.SOSortedExt;
//		InRamDBUtils2 dbu = new InRamDBUtils2(inputFilePath, outputPath);
//		dbu.loadAuxIndexFromFile();
//		dbu.compress(true);
//		
//		String inputFilePath2 = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
//				+ File.separator + "_ps26M_sorted" + File.separator + "hasAuthor" + CTMConstants.OSSortedExt;
//		InRamDBUtils2 dbu2 = new InRamDBUtils2(inputFilePath2, outputPath);
//		dbu2.loadAuxIndexFromFile();
//		dbu2.compress(false);
		
//		String inputFilePath = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
//				+ File.separator + "_ps20G_sorted" + File.separator + "-telephone" + CTMConstants.SOSortedExt;
//		InRamDBUtils2 dbu = new InRamDBUtils2(inputFilePath, outputPath);
//		dbu.loadAuxIndexFromFile();
//		dbu.compress(true);
//
//		String inputFilePath2 = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
//				+ File.separator + "_ps20G_sorted" + File.separator + "-telephone" + CTMConstants.OSSortedExt;
//		InRamDBUtils2 dbu2 = new InRamDBUtils2(inputFilePath2, outputPath);
//		dbu2.loadAuxIndexFromFile();
//		dbu2.compress(false);
		

		/* TESTING MULTIPLE */
//		String outputPath = _workingDir + File.separator + "_compressed";
//		String psPath = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
//				+ File.separator + "_ps26M_sorted" + File.separator;
//		File folder = new File(psPath);
//		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
//		
//		HashSet<String> doneList = new HashSet<String>();
//		
//		for(File f : listOfFiles) {
//			String tempFName = IOUtils.filenameWithoutExt(f.getAbsolutePath());
//			if(!doneList.contains(tempFName)) {
//				doneList.add(tempFName);
//				String inFName1 = IOUtils.filenameWithoutExt(f.getAbsolutePath()) 
//						+ CTMConstants.SOSortedExt;
//				System.out.println(inFName1);
//				InRamDBUtils2 dbu1 = new InRamDBUtils2(inFName1, outputPath);
//				dbu1.loadAuxIndexFromFile();
//				dbu1.compress(true);
//				
//				String inFName2 = IOUtils.filenameWithoutExt(f.getAbsolutePath()) 
//						+ CTMConstants.OSSortedExt;
//				System.out.println(inFName2);
//				InRamDBUtils2 dbu2 = new InRamDBUtils2(inFName2, outputPath);
//				dbu2.loadAuxIndexFromFile();
//				dbu2.compress(false);
//			}
//		}
		
	}

}
