package data.compressor.utils;

public class InRamDBUtils2Runner {
	public final static String _workingDir = System.getProperty("user.dir");
	
	public static void main(String[] args) throws Exception {
		if(args.length!=3){
			System.out.println(" -[input file] -[output folder] "
					+ "-[true/false : write auxiliary index or not]");
			System.exit(-1);
		}
		
		/* STANDARD */
		System.setProperty("true","true");
		System.out.println("Input file : " + args[0]);
		System.out.println("Output path : " + args[1]);
		InRamDBUtils2 dbu = new InRamDBUtils2(args[0], args[1]);
		boolean auxIndex = Boolean.getBoolean(args[2]);
		System.out.println("Output auxiliary index ? " + auxIndex);
		dbu.loadAuxIndexFromFile();
		dbu.compress(auxIndex);
		
////		ServerConfig myconfig = ServerConfig.getInstance();
////		myconfig._nbThreads = 16;
//		String psPath = _workingDir + File.separator + ".." + File.separator + "DataSet" 
//				+ File.separator + "_psFake_sorted" + File.separator;
//		File folder = new File(psPath);
//		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
////		ArrayList<ArrayList<File>> inputLists = JobAssigner.assignJobs(listOfFiles, false);
//		
		
		/* TESTING SINGLE */
//		String outputPath = _workingDir + File.separator + "_compressed";
//		
//		String inputFilePath = _workingDir + File.separator + ".." + File.separator + "DataSet" 
//				+ File.separator + "_ps26M_sorted" + File.separator + "hasAuthor" + AppConstants.SOSortedExt;
//		InRamDBUtils2 dbu = new InRamDBUtils2(inputFilePath, outputPath);
//		dbu.loadAuxIndexFromFile();
//		dbu.compress(true);
//		
//		String inputFilePath2 = _workingDir + File.separator + ".." + File.separator + "DataSet" 
//				+ File.separator + "_ps26M_sorted" + File.separator + "hasAuthor" + AppConstants.OSSortedExt;
//		InRamDBUtils2 dbu2 = new InRamDBUtils2(inputFilePath2, outputPath);
//		dbu2.loadAuxIndexFromFile();
//		dbu2.compress(false);
		
//		String inputFilePath = _workingDir + File.separator + ".." + File.separator + "DataSet" 
//				+ File.separator + "_ps20G_sorted" + File.separator + "-telephone" + AppConstants.SOSortedExt;
//		InRamDBUtils2 dbu = new InRamDBUtils2(inputFilePath, outputPath);
//		dbu.loadAuxIndexFromFile();
//		dbu.compress(true);
//
//		String inputFilePath2 = _workingDir + File.separator + ".." + File.separator + "DataSet" 
//				+ File.separator + "_ps20G_sorted" + File.separator + "-telephone" + AppConstants.OSSortedExt;
//		InRamDBUtils2 dbu2 = new InRamDBUtils2(inputFilePath2, outputPath);
//		dbu2.loadAuxIndexFromFile();
//		dbu2.compress(false);
		

		/* TESTING MULTIPLE */
//		String outputPath = _workingDir + File.separator + "_compressed";
//		String psPath = _workingDir + File.separator + ".." + File.separator + "DataSet" 
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
//						+ AppConstants.SOSortedExt;
//				System.out.println(inFName1);
//				InRamDBUtils2 dbu1 = new InRamDBUtils2(inFName1, outputPath);
//				dbu1.loadAuxIndexFromFile();
//				dbu1.compress(true);
//				
//				String inFName2 = IOUtils.filenameWithoutExt(f.getAbsolutePath()) 
//						+ AppConstants.OSSortedExt;
//				System.out.println(inFName2);
//				InRamDBUtils2 dbu2 = new InRamDBUtils2(inFName2, outputPath);
//				dbu2.loadAuxIndexFromFile();
//				dbu2.compress(false);
//			}
//		}
		
	}

}
