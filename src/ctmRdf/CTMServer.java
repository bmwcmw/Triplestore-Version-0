package ctmRdf;

import indexNodesDBUtils.DBImpl;
import indexNodesDBUtils.InRamDBUtils;
import indexNodesDBUtils.MonetDBUtils;
import indexNodesDBUtils.MongoDBUtils;
import indexNodesDBUtils.MySQLUtils;
import indexNodesDBUtils.OracleUtils;
import indexNodesDBUtils.PostgreSQLUtils;
import indexNodesDBUtils.RedisUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import commandRunner.FormatConverter;
import dataCleaner.CTMPairLong;
import dataCleaner.CTMPairStr;
import dataComparator.FilePair;
import dataDistributor.ConnectorDN;
import dataDistributor.DestInfo;
import localIOUtils.IOUtils;

/**
 * Preprocessor and predicate distribution calculator
 * @author Cedar
 */
public class CTMServer {

	// TODO possibility to add arguments while launching the program
	// TODO possibility to use configuration file

	private static int _nbThreads;
	private static int _compressMode;
	private static boolean _writeprecompare;
	private static int _precompareMode;
	private static int _compareMode;
	private static int _distributeMode;
	private static int _indicatorMode;
	private static Map<String, String> _ctlParams;
	private final static String _workingDir = System.getProperty("user.dir");
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("---------------------------------------");
		System.out.println("|--------         CTM         --------|");
		System.out.println("---------------------------------------");

		// XXX SETUP : Logs on/off
		IOUtils.setLogging(true, false);
		
		// XXX SETUP : Compressor mode, perhaps needs external DB support
		CTMServer._compressMode = CTMConstants.CTMCOMPRESS_INRAM;
		// XXX SETUP : Compressor writes sorted S/O files of each predicate or not (InRam only)
		CTMServer._writeprecompare = true;
		
		// XXX SETUP : Pre-Comparator mode, perhaps needs PERL executable in PATH
		CTMServer._precompareMode = CTMConstants.CTMPRECOMPARE_JAVA;
		
		// XXX SETUP : Comparator mode, perhaps needs PERL or GNU executable in PATH
		CTMServer._compareMode = CTMConstants.CTMCOMPARE_JAVA_INRAM;
		
		// XXX SETUP : Distributor mode, to various distributed environments
		CTMServer._distributeMode = CTMConstants.CTMDISTRIBUTE_HDFS;
		
		// XXX SETUP : Use only S/O or S and O for indicator in the distribution part
		CTMServer._indicatorMode = CTMConstants.CTMINDICATORSO;
		
		// XXX SETUP : Global in/out paths
		CTMServer._ctlParams = new HashMap<String, String>();
		CTMServer._ctlParams.put("rdfPath", _workingDir + File.separator + ".." 
				+ File.separator + "CtmDataSet" + File.separator + "__rdf");
		CTMServer._ctlParams.put("n3Path", _workingDir + File.separator + ".." 
				+ File.separator + "CtmDataSet" + File.separator + "__n3(26M)");
		CTMServer._ctlParams.put("invalidPath", _workingDir + File.separator + "_invalidTriple");
		CTMServer._ctlParams.put("psPath", _workingDir + File.separator + "_ps");
		CTMServer._ctlParams.put("posPath", _workingDir + File.separator + "_pos");
		CTMServer._ctlParams.put("nsPath", _workingDir + File.separator + "_ns");
		CTMServer._ctlParams.put("compressedPath", _workingDir + File.separator + "_compressed");
		CTMServer._ctlParams.put("comparePath", _workingDir + File.separator + "_compare");
		CTMServer._ctlParams.put("indicatorPath", _workingDir + File.separator + "_indicator");

		for (String key : CTMServer._ctlParams.keySet())
			IOUtils.logLog("Using parameter : " + key
					+ " - " + CTMServer._ctlParams.get(key));

		System.out.println("---------------------------------------");
		
		int result;
		int userCmd;
		String inStr;
		long startTime, endTime, duration;
		while (true) {
		    System.out.println("---------------------------------------");
			System.out.println("Type number to execute : ");
			System.out.println("\tClean all existing processed data - "
							+ CTMConstants.CTMEMPTY);
			System.out.println("\tRDF to N3 converter - "
					+ CTMConstants.CTMCONVERTER);
			System.out.println("\tN3 Reader/Partitionner(PS) - "
							+ CTMConstants.CTMREADERPS);
			System.out.println("\tPredicate Reader/Splitter(POS) - "
							+ CTMConstants.CTMREADERPOS);
			System.out.println("\tCompressor for PS files(with optional Pre-Comparator for PS files) - "
					+ CTMConstants.CTMCOMPRESS);
			System.out.println("\tPre-Comparator for PS files - "
					+ CTMConstants.CTMPRECOMPARE);
			System.out.println("\tComparator for S/O arrays - "
					+ CTMConstants.CTMCOMPARE);
			System.out.println("\tDistributor of compressed PS files - "
					+ CTMConstants.CTMDISTRIBUTE);
			System.out.println("\tExit - " + CTMConstants.CTMEXIT);
			System.out.println("#");
			inStr = in.readLine();
			try {
				userCmd = Integer.valueOf(inStr);
				if (userCmd == CTMConstants.CTMEXIT) {
					System.out.println("Exit...");
					break;
				} else {
					startTime = System.currentTimeMillis();
					result = CTMServer.processAllFiles(userCmd);
					endTime = System.currentTimeMillis();
					duration = endTime - startTime;
					System.out.println("---------------------------------------");
					System.out.println("| " + userCmd
							+ " code=" + result
							+ " finished in " + duration + " ms)");
					System.out.println("---------------------------------------");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Main method
	 * 
	 * @param programInd
	 * @return 0 if successfully finished or -1 if error
	 * @throws Exception 
	 */
	public static int processAllFiles(int programInd) throws Exception {
		String rdfPath;
		String n3Path;
		String invalidPath;
		String psPath;
		String nsPath;
		String posPath;
		String comparePath;
		String compressedPath;
		String indicatorPath;
		long startTime, endTime, duration;
		switch (programInd) {
			case CTMConstants.CTMEMPTY:
				IOUtils.logLog("\nCleaning all except rdf and n3 files");
				if (_ctlParams != null) {
					invalidPath = _ctlParams.get("invalidPath");
					IOUtils.deleteDirectory(new File(invalidPath));
					IOUtils.checkOrCreateFolder(invalidPath);
					psPath = _ctlParams.get("psPath");
					IOUtils.deleteDirectory(new File(psPath));
					IOUtils.checkOrCreateFolder(psPath);
					posPath = _ctlParams.get("posPath");
					IOUtils.deleteDirectory(new File(posPath));
					IOUtils.checkOrCreateFolder(posPath);
					nsPath = _ctlParams.get("nsPath");
					IOUtils.deleteDirectory(new File(nsPath));
					IOUtils.checkOrCreateFolder(nsPath);
					comparePath = _ctlParams.get("comparePath");
					IOUtils.deleteDirectory(new File(comparePath));
					IOUtils.checkOrCreateFolder(comparePath);
					compressedPath = _ctlParams.get("compressedPath");
					IOUtils.deleteDirectory(new File(compressedPath));
					IOUtils.checkOrCreateFolder(compressedPath);
					indicatorPath = _ctlParams.get("indicatorPath");
					IOUtils.deleteDirectory(new File(indicatorPath));
					IOUtils.checkOrCreateFolder(indicatorPath);
				}
				IOUtils.logLog("\nOK. ");
				break;
			case CTMConstants.CTMCONVERTER:
				setNbThreads();
				startTime = System.currentTimeMillis();
				CTMServer.convert(programInd);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				IOUtils.logLog("---------------------------------------");
				IOUtils.logLog("| Conversion finished (Time elapsed : "
						+ duration + " ms)");
				IOUtils.logLog("---------------------------------------"
						+ "\n---------------------------------------");
				break;
			case CTMConstants.CTMREADERPS:
				setNbThreads();
				startTime = System.currentTimeMillis();
				CTMServer.ps(programInd);
				IOUtils.logLog(CTMServer._nbThreads+" threads terminated");
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				IOUtils.logLog("---------------------------------------");
				IOUtils.logLog("| PS finished (Time elapsed : "
						+ duration + " ms)");
				IOUtils.logLog("---------------------------------------"
						+ "\n---------------------------------------");
				
				//Much more faster using SHELL command
				System.out.println("Do you want to merge each predicate into single file RIGHT NOW?");
				System.out.println("(Not necessary for compression/distribution, and use SHELL script "
						+ "if you want to perform this faster) (yN)");
				psPath = _ctlParams.get("psPath");
				IOUtils.checkOrCreateFolder(psPath);
				if(in.readLine().equals("y")){
					//Merge results of each thread
					IOUtils.logLog("Begin merging");
					final HashMap<String, ArrayList<File>> predicateFiles = new HashMap<String, ArrayList<File>>();
					Path start = FileSystems.getDefault().getPath(psPath);
					try {
						Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
							@Override
							public FileVisitResult visitFile(Path file,
									BasicFileAttributes attrs) throws IOException {
								File f = file.toFile();
								if (f.isFile()) {
									String s = f.getName();
									if(predicateFiles.containsKey(s)){
										predicateFiles.get(s).add(f);
									} else {
										ArrayList<File> temp = new ArrayList<File>();
										temp.add(f);
										predicateFiles.put(s, temp);
									}
								}
								return FileVisitResult.CONTINUE;
							}
						});
						String finalFileName;
						for(Entry<String, ArrayList<File>> pairs : predicateFiles.entrySet()){
							finalFileName = psPath + File.separator + pairs.getKey();
							IOUtils.logLog("Merging... " + pairs.getKey() + "(" 
									+ pairs.getValue().size() + ") into " + finalFileName);
							IOUtils.mergeFiles(
									Arrays.copyOf(
											pairs.getValue().toArray(), 
											pairs.getValue().toArray().length, 
											File[].class),
									new File(finalFileName));
					    }
					} catch (Exception e) {
						IOUtils.logLog(e.getMessage());
						return -1;
					}
				}
				break;
			case CTMConstants.CTMREADERPOS:
				setNbThreads();
				startTime = System.currentTimeMillis();
				CTMServer.pos(programInd);
				IOUtils.logLog(CTMServer._nbThreads+" threads terminated");
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				IOUtils.logLog("---------------------------------------");
				IOUtils.logLog("| POS finished (Time elapsed : "
						+ duration + " ms)");
				IOUtils.logLog("---------------------------------------"
						+ "\n---------------------------------------");
				break;
			case CTMConstants.CTMCOMPRESS:
				setNbThreads();
				startTime = System.currentTimeMillis();
				CTMServer.compress(programInd);
				IOUtils.logLog(CTMServer._nbThreads+" threads terminated");
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				IOUtils.logLog("---------------------------------------");
				IOUtils.logLog("| Compression finished (Time elapsed : "
						+ duration + " ms)");
				IOUtils.logLog("---------------------------------------"
						+ "\n---------------------------------------");
				break;
			case CTMConstants.CTMPRECOMPARE:
				setNbThreads();
				startTime = System.currentTimeMillis();
				CTMServer.precompare();
				IOUtils.logLog(CTMServer._nbThreads+" threads terminated");
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				IOUtils.logLog("---------------------------------------");
				IOUtils.logLog("| Files for comparison prepared (Time elapsed : "
						+ duration + " ms)");
				IOUtils.logLog("---------------------------------------"
						+ "\n---------------------------------------");
				break;
			case CTMConstants.CTMCOMPARE:
				setNbThreads();
				startTime = System.currentTimeMillis();
				CTMServer.compare();
				IOUtils.logLog(CTMServer._nbThreads+" threads terminated");
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				IOUtils.logLog("---------------------------------------");
				IOUtils.logLog("| Comparison finished (Time elapsed : "
						+ duration + " ms)");
				IOUtils.logLog("---------------------------------------"
						+ "\n---------------------------------------");
				break;
			case CTMConstants.CTMDISTRIBUTE:
				setNbThreads();
				startTime = System.currentTimeMillis();
				CTMServer.distribute(programInd);
				IOUtils.logLog(CTMServer._nbThreads+" threads terminated");
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				IOUtils.logLog("---------------------------------------");
				IOUtils.logLog("| File distribution finished (Time elapsed : "
						+ duration + " ms)");
				IOUtils.logLog("---------------------------------------"
						+ "\n---------------------------------------");
				break;
			default:
				return -1;
		}
		return 0;
	}
	
	/**
	 * Internal function for conversion
	 * @param programInd
	 * @return 0 if OK
	 */
	static int convert(int programInd){
		String n3Path = _ctlParams.get("n3Path");
		IOUtils.checkOrCreateFolder(n3Path);
		String rdfPath = _ctlParams.get("rdfPath");
		IOUtils.logLog("\nConverting RDF/OWL/WML to N3 : ");
		IOUtils.logLog("Input : " + rdfPath);
		IOUtils.logLog("Output : " + n3Path);

		File folder = new File(rdfPath);
		if(!folder.canRead() && !folder.isDirectory()) return -1;
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		ArrayList<ArrayList<File>> inputLists = assignJobs(listOfFiles, false);
		
		File temp;
		//Check all input files
		for (int i = 0; i < listOfFiles.size(); i++) {
			temp = listOfFiles.get(i);
			if (!temp.isFile() || !isRdfOwl(temp.getName())) {
				IOUtils.logLog("Input folder contains error : " + temp.getName());
				return -1;
			}
		}
		
		//Create and execute threads with assigned sub task
		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
        for (int i = 0; i < CTMServer._nbThreads; i++) {
            Runnable thread = new CTMThread(String.valueOf(i), 
            		programInd, inputLists.get(i), 
            		n3Path + File.separator + String.valueOf(i));
            executor.execute(thread);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return 0;
	}
	
	/**
	 * Internal function for PS
	 * @param programInd
	 * @return 0 if OK
	 */
	static int ps(int programInd){
		String n3Path = _ctlParams.get("n3Path");
		IOUtils.checkOrCreateFolder(n3Path);
		String psPath = _ctlParams.get("psPath");
		IOUtils.checkOrCreateFolder(psPath);
		String nsPath = _ctlParams.get("nsPath");
		IOUtils.checkOrCreateFolder(nsPath);
		String invalidPath = _ctlParams.get("invalidPath");
		IOUtils.checkOrCreateFolder(invalidPath);
		IOUtils.logLog("\nPartitionning N3 : ");
		IOUtils.logLog("Input : " + n3Path);
		IOUtils.logLog("Output : " + psPath);
		IOUtils.logLog("Output : " + nsPath);
		IOUtils.logLog("Output : " + invalidPath);

		File folder = new File(n3Path);
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		ArrayList<ArrayList<File>> inputLists = assignJobs(listOfFiles, false);
		
		//Create and execute threads with assigned sub task
		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
        for (int i = 0; i < CTMServer._nbThreads; i++) {
            Runnable thread = new CTMThread(String.valueOf(i), 
            		programInd, inputLists.get(i), 
            		psPath + File.separator + String.valueOf(i),
		    		invalidPath + File.separator + String.valueOf(i), 
		    		nsPath + File.separator + String.valueOf(i));
            executor.execute(thread);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return 0;
	}
	
	/**
	 * Internal function for POS
	 * @param programInd
	 * @return 0 if OK
	 */
	static int pos(int programInd){
		String psPath = _ctlParams.get("psPath");
		IOUtils.checkOrCreateFolder(psPath);
		String posPath = _ctlParams.get("posPath");
		IOUtils.checkOrCreateFolder(posPath);
		IOUtils.logLog("\nPartitionning N3 : ");
		IOUtils.logLog("Input : " + psPath);
		IOUtils.logLog("Output : " + posPath);

		File folder = new File(psPath);
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		ArrayList<ArrayList<File>> inputLists = assignJobs(listOfFiles, false);
		
		//Create and execute threads with assigned sub task
		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
        for (int i = 0; i < CTMServer._nbThreads; i++) {
            Runnable thread = new CTMThread(String.valueOf(i), programInd, inputLists.get(i), 
            		posPath);
            executor.execute(thread);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        return 0;
	}
	
	/**
	 * Internal function for compression
	 * @param programInd
	 * @return 0 if OK
	 */
	static int compress(int programInd){
		String psPath = _ctlParams.get("psPath");
		IOUtils.checkOrCreateFolder(psPath);
		String compressedPath = _ctlParams.get("compressedPath");
		IOUtils.checkOrCreateFolder(compressedPath);
		String comparePath = null;
		if(_writeprecompare){
			comparePath = _ctlParams.get("comparePath");
			IOUtils.checkOrCreateFolder(comparePath);
		}
		IOUtils.logLog("\nCompressing PS files : ");
		IOUtils.logLog("Input : " + psPath);
		IOUtils.logLog("Output : " + compressedPath);
		IOUtils.logLog("Writting sorted S/O files : "+_writeprecompare);

		File folder = new File(psPath);
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		ArrayList<ArrayList<File>> inputLists = assignJobs(listOfFiles, false);
		
		//Create and execute threads with assigned sub task
		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
		try{
			DBImpl dbu = null;
			switch(_compressMode){
				case CTMConstants.CTMCOMPRESS_INRAM : 
					dbu = new InRamDBUtils();
					break;
				case CTMConstants.CTMCOMPRESS_MONET : 
					dbu = new MonetDBUtils();
					break;
				case CTMConstants.CTMCOMPRESS_MONGO : 
					dbu = new MongoDBUtils();
					break;
				case CTMConstants.CTMCOMPRESS_MYSQL : 
					dbu = new MySQLUtils();
					break;
				case CTMConstants.CTMCOMPRESS_ORACLE : 
					dbu = new OracleUtils();
					break;
				case CTMConstants.CTMCOMPRESS_POSTGRES : 
					dbu = new PostgreSQLUtils();
					break;
				case CTMConstants.CTMCOMPRESS_REDIS : 
					dbu = new RedisUtils();
					break;
				default : 
					IOUtils.logLog("Compression mode error :"+_compressMode);
			}
	        for (int i = 0; i < CTMServer._nbThreads; i++) {
	            Runnable thread = new CTMThread(String.valueOf(i), programInd, inputLists.get(i), 
	            		compressedPath, dbu, comparePath);
	            executor.execute(thread);
	        }
	        executor.shutdown();
	        while (!executor.isTerminated()) {
	        }
	        return 0;
		} catch (Exception e) {
			IOUtils.logLog("Error : ");
			IOUtils.logLog(e.getMessage());
			return -1;
		}
	}
	
	/**
	 * Internal function for pre-comparison
	 * @param programInd
	 * @return 0 if OK
	 */
	static int precompare(){
		String psPath = _ctlParams.get("psPath");
		IOUtils.checkOrCreateFolder(psPath);
		String comparePath = _ctlParams.get("comparePath");
		IOUtils.checkOrCreateFolder(comparePath);
		IOUtils.logLog("\nProcessing PS files : ");
		IOUtils.logLog("Input : " + psPath);
		IOUtils.logLog("Output : " + comparePath);
		
		File folder = new File(psPath);
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		ArrayList<ArrayList<File>> inputLists = assignJobs(listOfFiles, false);
		
		//Create and execute threads with assigned sub task
		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
        for (int i = 0; i < CTMServer._nbThreads; i++) {
            Runnable thread = new CTMThread(String.valueOf(i), _precompareMode, inputLists.get(i), 
            		comparePath);
            executor.execute(thread);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		return 0;
	}
	
	/**
	 * Internal function for comparison
	 * @param programInd
	 * @return 0 if OK
	 */
	static int compare(){
		String comparePath = _ctlParams.get("comparePath");
		IOUtils.checkOrCreateFolder(comparePath);
		String indicatorPath = _ctlParams.get("indicatorPath");
		IOUtils.checkOrCreateFolder(indicatorPath);
		IOUtils.logLog("\nComparing PS files : ");
		IOUtils.logLog("Input : " + comparePath);
		IOUtils.logLog("Output : " + indicatorPath);
		
		File folder = new File(comparePath);
		File[] listOfFiles = folder.listFiles();
		
		//Check number of files
		HashMap<String,HashSet<String>> noRepNames = new HashMap<String,HashSet<String>>();
		File temp;
		String tempName;
		//Each prepared predicate should have two components : .S, .O
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				temp = listOfFiles[i];
				if(!temp.getName().contains(".S") && !temp.getName().contains(".O")){
					IOUtils.logLog("Input folder contains error : "
							+ temp.getName() + " neither .S nor .O");
					return -1;
				}
				tempName = IOUtils.filenameWithoutExt(temp.getName());
				if(noRepNames.containsKey(tempName)){
					noRepNames.get(tempName).add(temp.getName());
				} else {
					HashSet<String> newhs = new HashSet<String>();
					newhs.add(temp.getName());
					noRepNames.put(tempName, newhs);
				}
			}
		}
		for(String key : noRepNames.keySet()){
			if(noRepNames.get(key).size()!=2){
				IOUtils.logLog("Input folder contains error : "
						+ key + " has only " + noRepNames.get(key).size() + " component(s)");
				return -1;
			}
		}
		IOUtils.logLog("Local prepared file checked");
		
		ArrayList<String> uniqueName = new ArrayList<String>();
		uniqueName.addAll(noRepNames.keySet());
		//sort in alphabetically order
		Collections.sort(uniqueName, new Comparator<String>() {
	        @Override
	        public int compare(String s1, String s2) {
	            return s1.compareToIgnoreCase(s2);
	        }
	    });

		Integer nbComparison = (uniqueName.size()) * (uniqueName.size()-1) / 2;
		IOUtils.logLog("Assigning for "+nbComparison+" comparison(s)");
		
		IOUtils.logLog("Number of predicates "+uniqueName.size());
		LinkedList<FilePair> toComparePairs = new LinkedList<FilePair>();
		for(int i = 0; i<uniqueName.size(); i++) {
			System.out.println(i+" ==> "+uniqueName.get(i));
			for(int j = i+1; j<uniqueName.size(); j++) {
				System.out.println("\t"+j+" : "+uniqueName.get(j));
				toComparePairs.add(new FilePair(
						new File(comparePath + File.separator + uniqueName.get(i) + ".S"),
						new File(comparePath + File.separator + uniqueName.get(i) + ".O"),
						new File(comparePath + File.separator + uniqueName.get(j) + ".S"),
						new File(comparePath + File.separator + uniqueName.get(j) + ".O")
					));
			}
		}
		LinkedList<LinkedList<FilePair>> inputLists = assignJobs(toComparePairs, false);
		//Create and execute threads with assigned sub task
		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
        for (int i = 0; i < CTMServer._nbThreads; i++) {
            Runnable thread = new CTMThread(String.valueOf(i), _compareMode, inputLists.get(i), 
            		indicatorPath);
            executor.execute(thread);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        
		return 0;
	}
	
	/**
	 * Internal function for distribution
	 * @param programInd
	 * @return 0 if OK
	 * @throws Exception 
	 */
	static int distribute(int programInd) throws Exception{	
		String indicatorPath = _ctlParams.get("indicatorPath");
		
		/* Check compressed files */
		String compressedPath = _ctlParams.get("compressedPath");
		File folder = new File(compressedPath);
		File[] listOfFiles = folder.listFiles();
		
		//Check number of files
		HashMap<String,HashSet<String>> noRepNames = new HashMap<String,HashSet<String>>();
		File temp;
		String tempName;
		//Each compressed predicate should have three components : .index, .matrixSO, .matrixOS
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				temp = listOfFiles[i];
				if(!temp.getName().contains(".index") && !temp.getName().contains(".matrixSO")
						&& !temp.getName().contains(".matrixOS")){
					IOUtils.logLog("Input folder contains error : "
							+ temp.getName() + " neither .index nor .matrixSO nor .matrixOS");
					return -1;
				}
				tempName = IOUtils.filenameWithoutExt(temp.getName());
				if(noRepNames.containsKey(tempName)){
					noRepNames.get(tempName).add(temp.getName());
				} else {
					HashSet<String> newhs = new HashSet<String>();
					newhs.add(temp.getName());
					noRepNames.put(tempName, newhs);
				}
			}
		}
		for(String key : noRepNames.keySet()){
			if(noRepNames.get(key).size()!=3){
				IOUtils.logLog("Input folder contains error : "
						+ key + " has only " + noRepNames.get(key).size() + " component(s)");
				return -1;
			}
		}
		IOUtils.logLog("Local compressed file checked");
		
		//Load if the indicator file exists(with check of file entries), otherwise, use a random plan 
		ArrayList<ArrayList<File>> groups = 
				groupBySimilarities(indicatorPath, compressedPath, noRepNames, false);

//		/* Contact DN and get the number of CNs with their available space */
//		String ipDN = "134.214.142.58";
//		int portDN = 7474;
//		ConnectDN directoryNode = new ConnectDN(ipDN, portDN);
//		directoryNode.sendMessage("HELLO");
//		String jsonRespStr = directoryNode.receiveMessage();
////		String jsonRespStr = "[{\"address\":\"192.168.0.1\",\"port\":\"8888\",\"free_space\":\"20480\",\"ratio\":\"0.5\"},"
////			+ "{\"address\":\"192.168.0.2\",\"port\":\"8686\",\"free_space\":\"200000\",\"ratio\":\"0.3\"}]";
//		JSONParser parser = new JSONParser();
//		JSONArray jsonResp = (JSONArray) parser.parse(jsonRespStr);
//		int nbNodes = jsonResp.size();
//		IOUtils.logLog("Available compute nodes : " + nbNodes);
//		ArrayList<JSONObject> jsonRespArray = new ArrayList<JSONObject>();
//		IOUtils.logLog("Available compute nodes information : ");
//		for (Object o : jsonResp){
//			JSONObject newJO = (JSONObject) o;
//			jsonRespArray.add(newJO);
//			String address = (String) newJO.get("address");
//    		//Use directory node's address if returned IP == LOCALHOST
//        	if(address.equals("127.0.0.1")) {
//        		address = ipDN;
//        	}
//			Integer port = Integer.valueOf( (String) newJO.get("port") );
//			Integer free_space = Integer.valueOf( (String) newJO.get("free_space") );
//			Float ratio = Float.valueOf( (String) newJO.get("ratio") );
//		    IOUtils.logLog(address+":"+port+"|"+free_space+"Mb|"+ratio);
//		}
//		//TODO Algorithm to distribute
//		
//		//Create and execute threads with assigned sub task
//		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
//	        for (int i = 0; i < CTMServer._nbThreads; i++) {
//	            Runnable thread = new CTMThread(String.valueOf(i), _distributeMode, 
//	            		new HashMap<File,DestInfo>());
//	            executor.execute(thread);
//	        }
//	        executor.shutdown();
//	        while (!executor.isTerminated()) {
//        }
//        //System.out.println(directoryNode.receiveMessage());
		return 0;
	}
	
	/**
	 * Tries to load predicates' similarities from a predefined file, 
	 * then to create predicate groups.
	 * Returns null if the file doesn't exist or is unreadable.
	 * 
	 * @return Grouped predicates
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static ArrayList<ArrayList<File>> groupBySimilarities(String compressedPath, 
			String indicatorPath, HashMap<String,HashSet<String>> noRepNames,
			boolean forceRandom) throws Exception{
		ArrayList<File> allPredFiles = IOUtils.loadFolder(compressedPath);
//		HashSet<String> predicateFilenames = new HashSet<String>();
//		for(int i=0;i<allPredFiles.size();i++){
//			predicateFilenames.add(IOUtils.filenameWithoutExt(allPredFiles.get(i).getName()));
//		}
		// USE random plan to group files of varying sizes into approximately EQUAL SIZED blocks 
		if(forceRandom) {
			IOUtils.logLog("Forced to use random plan...");
			ArrayList<ArrayList<File>> groups = assignJobs(allPredFiles, true);
			return groups;
		}
		// USE indicators
		else {
			IOUtils.logLog("Try to group by similarity");
			/* Calculate size of each group */
			int sizeOfGroup = noRepNames.size()/CTMServer._nbThreads;
			if (noRepNames.size()%CTMServer._nbThreads > 0) sizeOfGroup++;
			/* Get all indicator files */
			ArrayList<File> allIndFiles = IOUtils.loadFolder(compressedPath);
			if(allIndFiles != null){
				IOUtils.logLog("Found indicators, try to group by indicator");
				ArrayList<ArrayList<File>> groups = new ArrayList<ArrayList<File>>();
				/* Create structure <PredA, HashMap<PredB, Common(S,O)> */
				HashMap<String, HashMap<String, CTMPairLong>> indicators 
						= new HashMap<String, HashMap<String, CTMPairLong>>();
				BufferedReader reader = null;
				/* Load all indicators from files */
				for(File f : allIndFiles){
					try {
						reader = new BufferedReader(new FileReader(f));
						String s = reader.readLine();
						if(s==null) throw new ParseException(0);
						StringTokenizer nizer = new StringTokenizer(s);
						if (!nizer.hasMoreTokens()) throw new ParseException(1);
						String pred1name = nizer.nextToken();
						if (!nizer.hasMoreTokens()) throw new ParseException(2);
						String pred2name = nizer.nextToken();
						if (!nizer.hasMoreTokens()) throw new ParseException(3);
						Long Snumber = Long.valueOf(nizer.nextToken());
						if (!nizer.hasMoreTokens()) throw new ParseException(4);
						Long Onumber = Long.valueOf(nizer.nextToken());
						/* Add to predicate 1's list */
						HashMap<String, CTMPairLong> toAdd = indicators.get(pred1name);
						if (toAdd == null){
							toAdd = new HashMap<String, CTMPairLong>();
							toAdd.put(pred2name, new CTMPairLong(Snumber, Onumber));
							indicators.put(pred1name, toAdd);
						} else {
							if(toAdd.get(pred2name) != null) // This shouldn't happen
								throw new Exception("Error : repeated entry for "
										+ pred1name + " " + pred2name + " in file " + f.getName());
							toAdd.put(pred2name, new CTMPairLong(Snumber, Onumber));
						}

						/* Add to predicate 2's list */
						toAdd = indicators.get(pred2name);
						if (toAdd == null){
							toAdd = new HashMap<String, CTMPairLong>();
							toAdd.put(pred1name, new CTMPairLong(Snumber, Onumber));
							indicators.put(pred2name, toAdd);
						} else {
							if(toAdd.get(pred1name) != null) // This shouldn't happen
								throw new Exception("Error : repeated entry for "
										+ pred2name + " " + pred1name + " in file " + f.getName());
							toAdd.put(pred1name, new CTMPairLong(Snumber, Onumber));
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					} finally {
						if (reader != null) {
							reader.close();
						}
					}
				}
//				/* DEBUG */
//	        	for (Entry<String, HashMap<String, CTMPairLong>> entry : indicators.entrySet()) {
//	        		System.out.println("Indicators loaded Key: " + entry.getKey() + ". Value: " + entry.getValue());
//	       		}
				/* Calculate the sum of common subject and/or object */
				Map<String, Long> predsWithInd = new HashMap<String, Long>();
				switch (CTMServer._indicatorMode){
					case CTMConstants.CTMINDICATORS :
						for (Entry<String, HashMap<String, CTMPairLong>> e : indicators.entrySet()) {
							long temp = 0;
							for(Entry<String, CTMPairLong> e2 : e.getValue().entrySet()) {
								temp = temp + e2.getValue().getSubject();
							}
							predsWithInd.put(e.getKey(), temp);
						} 
						break;
					case CTMConstants.CTMINDICATORO : 
						for (Entry<String, HashMap<String, CTMPairLong>> e : indicators.entrySet()) {
							long temp = 0;
							for(Entry<String, CTMPairLong> e2 : e.getValue().entrySet()) {
								temp = temp + e2.getValue().getObject();
							}
							predsWithInd.put(e.getKey(), temp);
						}
						break;
					case CTMConstants.CTMINDICATORSO : 
						for (Entry<String, HashMap<String, CTMPairLong>> e : indicators.entrySet()) {
							long temp = 0;
							for(Entry<String, CTMPairLong> e2 : e.getValue().entrySet()) {
								temp = temp + e2.getValue().getSubject() + e2.getValue().getObject();
							}
							predsWithInd.put(e.getKey(), temp);
						}
						break;
					default : //This shouldn't happen
						return groupBySimilarities(indicatorPath, compressedPath, noRepNames, true);
				}
				/* Sort predicates by the sum of common subject and/or object */
				MapValueComparator bvc =  new MapValueComparator(predsWithInd);
		        TreeMap<String,Long> sortedPreds = new TreeMap<String,Long>(bvc);
		        sortedPreds.putAll(predsWithInd);
				
				/* DEBUG */
        		for (Entry<String,Long> entry : sortedPreds.entrySet()) {
        		     System.out.println("Sorted Key: " + entry.getKey() + ". Value: " + entry.getValue());
        		}
		        /* Group predicates */
		        int currentGroup=0;
		        while(sortedPreds.size()>0){
		        	groups.add(currentGroup, new ArrayList<File>());
		        	//Check if Comparator descending or not
		        	//Get first(best) element of sortedPreds to begin the group N
		        	String pred0 = sortedPreds.firstKey();
		        	IOUtils.logLog("Group "+currentGroup+" : beginning from "+pred0
		        			+" having "+sortedPreds.firstEntry().getValue()+" total common entries");
		        	HashMap<String, CTMPairLong> relatedPreds = indicators.get(pred0);
	        		/* Add it(index, matrix S, matrix O) to group N and remove it from temporary set */
	        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
	        				+ pred0 + ".index"));
	        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
	        				+ pred0 + ".matrixS"));
	        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
	        				+ pred0 + ".matrixO"));
	        		sortedPreds.remove(pred0);
	        		
		        	//Get first N elements related, put them in groups(n)
		        	//And remove these first N elements from sortedPreds
		        	int i=0;
		        	long maxValue=0;
		        	String maxPred="";
		        	while(i < sizeOfGroup){
		        		/* Find nearest predicate */
		        		switch (CTMServer._indicatorMode){
							case CTMConstants.CTMINDICATORS :
				        		for(Entry<String, CTMPairLong> e : relatedPreds.entrySet()){
				        			if(e.getValue().getSubject() >= maxValue){
				        				maxValue = e.getValue().getSubject();
				        				maxPred = e.getKey();
				        			}
				        		}
				        		break;
							case CTMConstants.CTMINDICATORO : 
								for(Entry<String, CTMPairLong> e : relatedPreds.entrySet()){
				        			if(e.getValue().getObject() >= maxValue){
				        				maxValue = e.getValue().getObject();
				        				maxPred = e.getKey();
				        			}
				        		}
				        		break;
							case CTMConstants.CTMINDICATORSO : 
								for(Entry<String, CTMPairLong> e : relatedPreds.entrySet()){
				        			if((e.getValue().getSubject()+e.getValue().getObject()) >= maxValue){
				        				maxValue = e.getValue().getSubject() + e.getValue().getObject();
				        				maxPred = e.getKey();
				        			}
				        		}
				        		break;
							default : //This shouldn't happen
								return groupBySimilarities(indicatorPath, compressedPath, noRepNames, true);
						}
		        		/* Add it(index, matrix S, matrix O) to group N and remove it from temporary sets */
			        	IOUtils.logLog("Add "+maxPred+" to group "+currentGroup
			        			+" having "+maxValue+" common entries with "+pred0);
		        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
		        				+ maxPred + ".index"));
		        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
		        				+ maxPred + ".matrixS"));
		        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
		        				+ maxPred + ".matrixO"));
		        		//TODO PROBLEM!
		        		relatedPreds.remove(maxPred);
		        		sortedPreds.remove(maxPred);
		        		i++;
		        	}
		        	currentGroup++;
		        }
				return groups;
			} else {
				return groupBySimilarities(indicatorPath, compressedPath, noRepNames, true);
			}
		}
	}

	/**
	 * Check if a file has the extension rdf
	 * 
	 * @param fileName
	 * @return true if yes
	 */
	static boolean isRdfOwl(String fileName) {
		return fileName.endsWith(".rdf");
	}
	

	/**
	 * Check if a file has the extension n3
	 * 
	 * @param fileName
	 * @return true if yes
	 */
	static boolean isN3(String fileName) {
		return fileName.endsWith(".n3");
	}
	
	/**
	 * Set the number of thread workers
	 * @throws IOException
	 */
	static void setNbThreads() throws IOException{
		try{
		    System.out.println("How many threads (>0) ?");
		    CTMServer._nbThreads = Integer.valueOf(in.readLine());
		} catch (NumberFormatException e) {
		    System.out.println("Input error, using 4 threads...");
		    CTMServer._nbThreads = 4;
		} finally {
		    if(CTMServer._nbThreads<=0){
			    System.out.println("Input error, using 4 threads...");
			    CTMServer._nbThreads = 4;
		    }
		}
	}
	
	/**
	 * <p>Split all source files into specified number of groups, then for example 
	 * assign them to multiple threads</p>
	 * <p>For PS, POS, Compress, Pre-Compare</p>
	 * @param allFiles all source files
	 * @param averageSize false to assign tasks only according to number of files(faster),
	 * true if you want to have more average size between each group of files(slower)
	 * 
	 * @return List of grouped files (sub-lists)
	 */
	static ArrayList<ArrayList<File>> assignJobs(ArrayList<File> allFiles, boolean averageSize){
		ArrayList<ArrayList<File>> outputLists = new ArrayList<ArrayList<File>>();
		if(!averageSize){ //Random plan only according to the number of files
			//Distribute all input files to threads, as average as possible
			int partitionSize = allFiles.size()/CTMServer._nbThreads;
			int remainder = allFiles.size()%CTMServer._nbThreads;
			int i = 0;
			if(remainder > 0){
				partitionSize++;
				while (remainder > 0) {
					System.out.println("Adding "+Math.min(partitionSize, allFiles.size() - i));
					outputLists.add(new ArrayList<File>(allFiles.subList(i,
							i + Math.min(partitionSize, allFiles.size() - i))));
					i += partitionSize;
					remainder--;
				}
				partitionSize--;
			}
			while (i < allFiles.size()) {
				System.out.println("Adding "+Math.min(partitionSize, allFiles.size() - i));
				outputLists.add(new ArrayList<File>(allFiles.subList(i,
			            i + Math.min(partitionSize, allFiles.size() - i))));
				i += partitionSize;
			}
			//Show result
			for (i = 0; i < outputLists.size(); i++) {
				IOUtils.logLog("Sub task " + i + " with " + 
						outputLists.get(i).size() + "file(s)");
	//			for (int j = 0; j < inputLists.get(i).size(); j++) {
	//				IOUtils.logLog(inputLists.get(i).get(j).getName());
	//			}
			}
		}
		else { //To approximately equal-sized blocks
			/* Find the target group size - the sum of all sizes divided by n. */
			double sizeAll = 0;
			for(File f : allFiles){
				sizeAll += f.length();
			}
			double expected = sizeAll / CTMServer._nbThreads;
			
			/* Sort the files decreasing in size. */
			Collections.sort(allFiles, new Comparator<File>() {
				@Override
				public int compare(final File f1, final File f2) {
					return Long.valueOf(f1.length()).compareTo(f2.length());
				}
	        });
			//TODO TEST
			/* Creates groups */
			double tempsize = expected;
			int currentGroup = 0;
			while(allFiles.size() > 0 && currentGroup < CTMServer._nbThreads){
				/* for each group, while the remaining space in your group is 
				 * bigger than the first element of the list take the first element 
				 * of the list and move it to the group
				 */
				for(int i=0;i<allFiles.size();i++){
					/* for each element, find the element for which the difference 
					 * between group size and target group size is minimal move this 
					 * element to the group(decreasing size loop)
				     */
					File f = allFiles.get(i);
					if(tempsize > f.length()){
						outputLists.get(currentGroup).add(f);
						tempsize = tempsize - f.length();
						allFiles.remove(i);
						i--;
					}
					currentGroup++;
				}
			}
			/* This shouldn't happen */
			if((currentGroup == CTMServer._nbThreads) && (allFiles.size()>0)){
				IOUtils.logLog("Error : ???");
				return null;
			}
			    

		}
		IOUtils.logLog("Sub tasks assigned");
		return outputLists;
	}
	
	/**
	 * <p>Split all source files into specified number of groups, then for example 
	 * assign them to multiple threads</p>
	 * <p>For Compare</p>
	 * @param allFiles all source files
	 * @param averageSize false to assign tasks only according to number of files(faster),
	 * true if you want to have more average size between each group of files(slower)
	 * @return
	 */
	static LinkedList<LinkedList<FilePair>> assignJobs(LinkedList<FilePair> allPairs, boolean averageSize){
		LinkedList<LinkedList<FilePair>> inputLists = new LinkedList<LinkedList<FilePair>>();
		if(!averageSize){ //Random plan only according to the number of files
			//Distribute all input files to threads, as average as possible
			int partitionSize = allPairs.size()/CTMServer._nbThreads;
			int remainder = allPairs.size()%CTMServer._nbThreads;
			int i = 0;
			if(remainder > 0){
				partitionSize++;
				while (remainder > 0) {
					System.out.println("Adding "+Math.min(partitionSize, allPairs.size() - i));
					inputLists.addFirst(new LinkedList<FilePair>(allPairs.subList(i,
							i + Math.min(partitionSize, allPairs.size() - i))));
					i += partitionSize;
					remainder--;
				}
				partitionSize--;
			}
			while (i < allPairs.size()) {
				System.out.println("Adding "+Math.min(partitionSize, allPairs.size() - i));
				inputLists.addFirst(new LinkedList<FilePair>(allPairs.subList(i,
			            i + Math.min(partitionSize, allPairs.size() - i))));
				i += partitionSize;
			}
			//Show result
			for (i = 0; i < inputLists.size(); i++) {
				IOUtils.logLog("Sub task " + i + " with " + 
						inputLists.get(i).size() + "file(s)");
	//			for (int j = 0; j < inputLists.get(i).size(); j++) {
	//				IOUtils.logLog(inputLists.get(i).get(j).getName());
	//			}
			}
		}
		else { //To approximately equal-sized blocks
			//TODO Once the other tested, follow the method
		}
		IOUtils.logLog("Sub tasks assigned");
		return inputLists;
	}

}
