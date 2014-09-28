package dataCompressorUtils2;

public class InRamDBUtils2Test {
	public final static String _workingDir = System.getProperty("user.dir");
	
	public static void main(String[] args) throws Exception {
		InRamDBUtils2 dbu = new InRamDBUtils2(args[0], args[1]);
		boolean auxIndex = Boolean.getBoolean(args[2]);
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
//		String outputPath = _workingDir + File.separator + "_compressed";
//		
//		HashSet<String> doneList = new HashSet<String>();
//		
//		for(File f : listOfFiles) {
//			String tempFName = IOUtils.filenameWithoutExt(f.getAbsolutePath());
//			if(!doneList.contains(tempFName)) {
//				doneList.add(tempFName);
////				System.out.println(IOUtils.filenameWithoutExt(f.getAbsolutePath()) 
////						+ CTMConstants.SOSortedExt);
////				System.out.println(IOUtils.filenameWithoutExt(f.getAbsolutePath()) 
////						+ CTMConstants.OSSortedExt);
//				
//			}
//		}
		
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
		
	}

}
