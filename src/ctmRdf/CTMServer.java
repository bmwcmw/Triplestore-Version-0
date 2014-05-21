package ctmRdf;

import indexNodesDBUtils.InRamDBUtils;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import commandRunner.FormatConverter;
import dataDistributor.DNConnection;
import localIOUtils.IOUtils;

/**
 * Preprocessor and predicate distribution calculator
 * @author Cedar
 */
public class CTMServer {

	private static int _nbThreads;
	private static Map<String, String> _ctlParams;
	private final static String _workingDir = System.getProperty("user.dir");
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// TODO possibility to add arguments while launching the program
		System.out.println("---------------------------------------");
		System.out.println("|--------         CTM         --------|");
		System.out.println("---------------------------------------");

		// XXX SETUP : Logs on/off
		IOUtils.setLogging(true, false);
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
			System.out.println("\tCompressor for PS files(Needs PERL executable in PATH) - "
					+ CTMConstants.CTMCOMPRESS);
			System.out.println("\tComparator for PS files(Pre-distributor) - "
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
			}
		}

	}

	/**
	 * Main method
	 * 
	 * @param programInd
	 * @return 0 if successfully finished or -1 if error
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static int processAllFiles(int programInd) throws IOException, ParseException {
		String rdfPath;
		String n3Path;
		String invalidPath;
		String psPath;
		String nsPath;
		String posPath;
		String comparePath;
		String compressedPath;
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
				}
				IOUtils.logLog("\nOK. ");
				break;
			case CTMConstants.CTMCONVERTER:
				setNbThreads();
				rdfPath = _ctlParams.get("rdfPath");
				IOUtils.checkOrCreateFolder(rdfPath);
				n3Path = _ctlParams.get("n3Path");
				IOUtils.checkOrCreateFolder(n3Path);
				IOUtils.logLog("\nConverting rdf to n3 : ");
				IOUtils.logLog("Input : " + rdfPath);
				IOUtils.logLog("Output : " + n3Path);
				/* Conversion */
				startTime = System.currentTimeMillis();
				File folder;
				folder = new File(rdfPath);
				ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
				File temp;
				//Check all input files
				for (int i = 0; i < listOfFiles.size(); i++) {
					temp = listOfFiles.get(i);
					if (!temp.isFile() || !isRdf(temp.getName())) {
						IOUtils.logLog("Input folder contains error : " + temp.getName());
						return -1;
					}
				}
				
				String inputFile, outputFile;
				FormatConverter fc = new FormatConverter();
				folder = new File(rdfPath);
				File[] listOfRdfFiles = folder.listFiles();
				for (int i = 0; i < listOfRdfFiles.length; i++) {
					if (listOfRdfFiles[i].isFile()) {
						if (isRdf(listOfRdfFiles[i].getName())) {
							inputFile = rdfPath + File.separator
									+ listOfRdfFiles[i].getName();
							outputFile = n3Path + File.separator
									+ IOUtils.changeExtension(listOfRdfFiles[i].getName(),".n3");
							IOUtils.logLog(fc.execute(inputFile, outputFile));
						}
					}
				}
				
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
			case CTMConstants.CTMCOMPARE:
				setNbThreads();
				startTime = System.currentTimeMillis();
				CTMServer.compare(programInd);
				IOUtils.logLog(CTMServer._nbThreads+" threads terminated");
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				IOUtils.logLog("---------------------------------------");
				IOUtils.logLog("| Compression finished (Time elapsed : "
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
		    		nsPath + File.separator + String.valueOf(i), null);
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
            Runnable thread = new CTMThread(String.valueOf(i), 
            		programInd, inputLists.get(i), posPath, null, null, null);
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
		IOUtils.logLog("\nCompressing PS files : ");
		IOUtils.logLog("Input : " + psPath);
		IOUtils.logLog("Output : " + compressedPath);

		File folder = new File(psPath);
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		ArrayList<ArrayList<File>> inputLists = assignJobs(listOfFiles, false);
		
		//Create and execute threads with assigned sub task
		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
        for (int i = 0; i < CTMServer._nbThreads; i++) {
            Runnable thread = new CTMThread(String.valueOf(i), 
            		programInd, inputLists.get(i), compressedPath, null, null, 
            		new InRamDBUtils());
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
	static int compare(int programInd){
		String psPath = _ctlParams.get("psPath");
		IOUtils.checkOrCreateFolder(psPath);
		String comparePath = _ctlParams.get("comparePath");
		IOUtils.deleteDirectory(new File(comparePath));
		IOUtils.checkOrCreateFolder(comparePath);
		IOUtils.logLog("\nComparing PS files : ");
		IOUtils.logLog("Input : " + psPath);
		IOUtils.logLog("Output : " + comparePath);
		
		File folder = new File(psPath);
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		ArrayList<ArrayList<File>> inputLists = assignJobs(listOfFiles, false);
		
		//Create and execute threads with assigned sub task
		ExecutorService executor = Executors.newFixedThreadPool(CTMServer._nbThreads);
        for (int i = 0; i < CTMServer._nbThreads; i++) {
            Runnable thread = new CTMThread(String.valueOf(i), 
            		programInd, inputLists.get(i), comparePath, null, null, null);
            executor.execute(thread);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
		System.out.println("Now, please execute the external script...");
		//TODO
		return 0;
	}
	
	/**
	 * Internal function for distribution
	 * @param programInd
	 * @return 0 if OK
	 * @throws IOException
	 * @throws ParseException
	 */
	static int distribute(int programInd) throws IOException, ParseException{		
		/* Check compressed files */
		String compressedPath = _ctlParams.get("compressedPath");
		File folder = new File(compressedPath);
		File[] listOfCompressedFiles = folder.listFiles();
		HashMap<String,HashSet<String>> noRepNames = new HashMap<String,HashSet<String>>();
		File temp;
		String tempName;
		//Each compressed predicate should have three components : .index, .matrixSO, .matrixOS
		for (int i = 0; i < listOfCompressedFiles.length; i++) {
			if (listOfCompressedFiles[i].isFile()) {
				temp = listOfCompressedFiles[i];
				tempName = filenameWithoutExt(temp.getName());
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
		int [][] simMat = loadPredicateSimilarities();
		if(listOfCompressedFiles.length!=simMat.length){
			IOUtils.logLog("Indicator file contains error number of file : using random plan.");
			simMat = null;
		}

		/* Contact DN and get the number of CNs with their available space */
		String ipDN = "134.214.142.58";
		int portDN = 7474;
		DNConnection directoryNode = new DNConnection(ipDN, portDN);
		directoryNode.sendMessage("HELLO");
		String jsonRespStr = directoryNode.receiveMessage();
//		String jsonRespStr = "[{\"address\":\"192.168.0.1\",\"port\":\"8888\",\"free_space\":\"20480\",\"ratio\":\"0.5\"},"
//			+ "{\"address\":\"192.168.0.2\",\"port\":\"8686\",\"free_space\":\"200000\",\"ratio\":\"0.3\"}]";
		JSONParser parser = new JSONParser();
		JSONArray jsonResp = (JSONArray) parser.parse(jsonRespStr);
		int nbNodes = jsonResp.size();
		ArrayList<JSONObject> jsonRespArray = new ArrayList<JSONObject>();
		IOUtils.logLog("Available compute nodes information : ");
		for (Object o : jsonResp){
			JSONObject newJO = (JSONObject) o;
			jsonRespArray.add(newJO);
			String address = (String) newJO.get("address");
    		//Use directory node's address if returned IP == LOCALHOST
        	if(address.equals("127.0.0.1")) {
        		address = ipDN;
        	}
			Integer port = Integer.valueOf( (String) newJO.get("port") );
			Integer free_space = Integer.valueOf( (String) newJO.get("free_space") );
			Float ratio = Float.valueOf( (String) newJO.get("ratio") );
		    IOUtils.logLog(address+":"+port+"|"+free_space+"Mb|"+ratio);
		}
		
		//TODO
        //System.out.println(directoryNode.receiveMessage());
		return 0;
	}
	
	/**
	 * Tries to load predicates' similarities from a predefined file.
	 * Returns null if the file doesn't exist or is unreadable.
	 * 
	 * @return a matrix
	 * @throws IOException 
	 */
	static int[][] loadPredicateSimilarities() throws IOException{
		String distIndPath = _workingDir +  File.separator + ".dist.ind";
		File distIndFile = new File(distIndPath);
		if(distIndFile.exists() && distIndFile.isFile() && distIndFile.canRead()){
			int[][] similarArray = new int[IOUtils.countLines(distIndPath)][IOUtils.countLines(distIndPath)];
			return similarArray;
		} else 
			return null;
	}
	
	/**
	 * Returns a filename without extension
	 * 
	 * @param name
	 * @return filename
	 */
	static String filenameWithoutExt(String name){
		return name.replaceFirst("[.][^.]+$", "");
	}

	/**
	 * Check if a file has the extension rdf
	 * 
	 * @param fileName
	 * @return true if yes
	 */
	static boolean isRdf(String fileName) {
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
	 * Split all source files into specified number of groups, then for example assign them to multiple threads
	 * @param allFiles all source files
	 * @param nbThreads number of groups
	 * @param averageSize false to assign tasks only according to number of files(faster),
	 * true if you want to have more average size between each group of files(slower)//TODO
	 * @return
	 */
	static ArrayList<ArrayList<File>> assignJobs(ArrayList<File> allFiles, boolean averageSize){
		ArrayList<ArrayList<File>> inputLists = new ArrayList<ArrayList<File>>();
		//Distribute all input files to threads, as average as possible
		int partitionSize = allFiles.size()/CTMServer._nbThreads;
		int remainder = allFiles.size()%CTMServer._nbThreads;
		int i = 0;
		if(remainder > 0){
			partitionSize++;
			while (remainder > 0) {
				System.out.println("Adding "+Math.min(partitionSize, allFiles.size() - i));
				inputLists.add(new ArrayList<File>(allFiles.subList(i,
						i + Math.min(partitionSize, allFiles.size() - i))));
				i += partitionSize;
				remainder--;
			}
			partitionSize--;
		}
		while (i < allFiles.size()) {
			System.out.println("Adding "+Math.min(partitionSize, allFiles.size() - i));
			inputLists.add(new ArrayList<File>(allFiles.subList(i,
		            i + Math.min(partitionSize, allFiles.size() - i))));
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
		IOUtils.logLog("Sub tasks assigned");
		return inputLists;
		
	}

}
