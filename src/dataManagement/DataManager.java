package dataManagement;

import indexNodesDBUtils.DBImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import commandRunner.PerlPreComparator;
import ctmRdf.CTMConstants;
import localIOUtils.IOUtils;
import dataCleaner.CTMPairStr;
import dataCleaner.CTMTriple;
import dataCleaner.CTMPair;
import dataComparator.FilePair;
import dataComparator.JavaComparator;
import dataCompressor.DgapCompressor;
import dataCompressor.SOLongPair;
import dataDistributor.DataDistributor;
import dataDistributor.DestInfo;
import dataDistributor.FileSenderCN;
import dataDistributor.SSHExecutor;
import dataReader.N3Reader;
import dataReader.PairReader;

/**
 * <p>
 * RDF(N3) data manager :
 * </p>
 * <p>
 * Reader / Cleaner / Partitioner / ... for N3 data files
 * </p>
 * 
 * @author Cedar
 * 
 */
public class DataManager {

	private String threadId;
	private PrefixManager _prefixManager = new PrefixManager();
	
	/* For Output */
	private HashMap<String,BufferedWriter> _predicateWriterList 
		= new HashMap<String,BufferedWriter>();
	private BufferedWriter _invalidLogWriter;

	/**
	 * Constructor - needs id of the caller thread
	 */
	public DataManager(String thread) {
		threadId = thread;
	}

	/**
	 * Prints the prefix mapping table - not used in current version
	 */
	public void printPrefix() {
		_prefixManager.print();
	}

	/**
	 * Adds a prefix-URI pair into the mapping table - not used in current version
	 */
	public synchronized void addPrefix(String prefix, String uri) {
		_prefixManager.addPrefix(prefix, uri);
	}

	/**
	 * <p>Gets data from a specified file list of N3 files, eliminates 
	 * invalid tokens, then splits and stores data into files named with each 
	 * predicate</p>
	 * @param n3Src : List of source files
	 * @param outputPath : Destination folder to output results
	 * @param nsPath : Folder to store namespace information
	 * @param invalidPath : Folder to store invalid lines
	 * @throws IOException
	 * @throws ParseException
	 */
	public void psSplit(ArrayList<File> n3Src, String outputPath, 
			String nsPath, String invalidPath) throws IOException, ParseException {
	    String inFilePath;
	    String outFilePath;
	    String inFileName;
	    for (File f : n3Src){	    	
	    	inFilePath = f.getAbsolutePath();
	    	inFileName = f.getName();
			N3Reader reader = new N3Reader(this, inFilePath, 
					invalidPath + "invalid.log");
        	CTMTriple triple = null;
        	IOUtils.logLog("Thread "+ threadId + " Ps : " + inFileName);
        	String line;
        	while ((triple = reader.next()) != null) {
        		outFilePath = outputPath + File.separator
        				+ triple.getPredicate().toString().replace(":", "-");
        		line = triple.getSubject() + " " + triple.getObject();
        		writeToBigFile(outFilePath,line);
        	}
//        	IOUtils.logLog("Save Ns");
//        	this._prefixManager.save(nsPath + File.separator
//        			+ inFileName + ".ns");
	    }
		IOUtils.logLog("Thread " + threadId + " Ps all done");
	}

	/**
	 * <p>Gets data from a specified file list of PS files then splits and 
	 * stores them into files named by each predicate</p>
	 * @param psSrc : List of source files
	 * @param outputPath : Destination folder to output results
	 * @throws IOException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public void posSplit(ArrayList<File> psSrc, String outputPath) 
					throws IOException, ParseException, SQLException {
	    String inFilePath;
    	String inFileName;
	    String outFilePath;
		String line;
	    for (File f : psSrc){	    	
	    	inFilePath = f.getAbsolutePath();
	    	inFileName = f.getName();
	    	PairReader reader = new PairReader(inFilePath);
			IOUtils.logLog("Thread " + threadId + " Pos : "	+ inFileName);
			CTMPair so = null;
			while ((so = reader.next()) != null) {
				// POST : line = (Subject)
				if ((inFileName.compareTo("a") == 0)
						|| (inFileName.compareTo("rdf-type") == 0)) {
					outFilePath = outputPath + File.separator + "rdf-type_"
						+ so.getObject().toString().replace(":", "-");
					line = so.getSubject().toString();
				}
				// POSNT : line = (Subject Object)
				else {
					outFilePath = outputPath + File.separator + inFileName;
					if (so.getObject ().isVariable()) {
						outFilePath += "_Variable";
					} else if (so.getObject().isAnonymous()) {
						outFilePath += "_Anonymous";
					} else if (so.getObject().isIRI()) {
						outFilePath += "_IRI";
					} else {
						outFilePath += "_Litteral";
					}
					line = so.getSubject().toString() + " "
							+ so.getObject().toString();
				}
				writeToBigFile(outFilePath, line);
			}
	    }
		IOUtils.logLog("Thread " + threadId + " Pos all done");
	}

	/**
	 * <p>Gets data from a specified file list of PS files then compresses each 
	 * predicate using Dgap with an index</p>
	 * <p>Writes a BitMat-like matrix to a text file, with the index of all nodes
	 * which appeared in current predicate's file</p>
	 * @param psSrc : List of source files
	 * @param outputPath : Destination folder to output results
	 * @param dbu : Chosen database tools unit
	 * @param comparePath : Folder to store pre-comparison data. 
	 * 			Set it to null if we don't need to do that in this step.
	 * @return 0 if OK, -1 if error.
	 * @throws Exception 
	 */
	public int indexedCompress(ArrayList<File> psSrc, String outputPath, 
			DBImpl dbu, String comparePath) throws Exception {
	    String inFilePath;
    	String inFileName;
	    for (File f : psSrc){
	    	inFilePath = f.getAbsolutePath();
	    	inFileName = f.getName();
	    	PairReader reader = new PairReader(inFilePath);
			IOUtils.logLog("Thread " + threadId + " Compression : "	+ inFileName);
			CTMPair so = null;
			while ((so = reader.next()) != null) {
				// Predicate's index and pairs list
				insertOrIgnorePredicateNodes(dbu, so);
			}
			DgapCompressor.writeCompressedFormat(inFileName, outputPath, dbu, comparePath);
		    dbu.cleanAll();
	    }
		IOUtils.logLog("Thread " + threadId + " Compression all done");
		return 0;
	}
	
	/**
	 * Pre-comparison : Prepares predicate files(using java function) to facilitate comparison
	 * @param psSrc : List of source files
	 * @param outputPath : Destination folder to output results
	 * @throws IOException, ParseException 
	 */
	public void prepareCompareJava(ArrayList<File> psSrc, String outputPath) 
			throws IOException, ParseException{
	    String inFilePath;
    	String inFileName;
    	String outFileSPath;
    	String outFileOPath;
	    for (File f : psSrc){
	    	inFilePath = f.getAbsolutePath();
	    	inFileName = f.getName();
	    	outFileSPath = outputPath + File.separator + inFileName + ".S";
	    	outFileOPath = outputPath + File.separator + inFileName + ".O";
	    	PairReader reader = new PairReader(inFilePath);
			IOUtils.logLog("Thread " + threadId + " Pre-compare : "	+ inFileName);
			CTMPairStr so = null;
			while ((so = reader.nextStr()) != null) {
				writeToBigFile(outFileSPath, so.getSubject());
				writeToBigFile(outFileOPath, so.getObject());
			}
	    }
		IOUtils.logLog("Thread " + threadId + " Pre-compare all done");
	}

	/**
	 * Pre-comparison : Prepares predicate files(using Perl script) to facilitate comparison
	 * @param psSrc : List of source files
	 * @param outputPath : Destination folder to output results
	 * @throws IOException, ParseException 
	 */
	public void prepareComparePerl(ArrayList<File> psSrc, String outputPath) 
			throws IOException{
	    String inFilePath;
	    PerlPreComparator ppc = new PerlPreComparator();
	    for (File f : psSrc){
	    	inFilePath = f.getAbsolutePath();
	    	IOUtils.logLog(ppc.execute(inFilePath, outputPath));
	    }
		IOUtils.logLog("Thread " + threadId + " Preparation all done");
	}
	

	/**
	 * Compares each two predicate S/O array, then output a similarity indicator
	 * using Java method
	 * @param pairs : pairs of files to compare by this worker thread
	 * @param outputPath : output path for indicator information
	 * @throws IOException
	 * @throws ParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void compareJava(LinkedList<FilePair> pairs, String outputPath) 
			throws IOException, ParseException, InterruptedException, ExecutionException {
	    JavaComparator jc = new JavaComparator();
	    FilePair fp = null;
		int count = 0;
		String outFileName = null;
	    while(true){
	    	try{
	    		fp = pairs.getFirst();
	    	} catch (NoSuchElementException e) {
	    		break;
	    	}
			count++;
			outFileName = outputPath + File.separator 
					+ IOUtils.filenameWithoutExt(fp.f1S.getName()) + "_" 
					+ IOUtils.filenameWithoutExt(fp.f2S.getName());
		    Long resultS = jc.compareTwoPredicates(fp.f1S, fp.f2S);
		    Long resultO = jc.compareTwoPredicates(fp.f1O, fp.f2O);
		    System.out.println(fp.f1S.getName()+" and "+fp.f2S.getName()+" have "+resultS+" common entries.");
		    System.out.println(fp.f1O.getName()+" and "+fp.f2O.getName()+" have "+resultO+" common entries.");
		    writeToBigFile(outFileName, resultS + " " + resultO);
		    pairs.removeFirst();
	    }
	    IOUtils.logLog("Thread " + threadId + " executed " + count + " comparisons.");
	}
	
	/**
	 * Compares each two predicate S/O array, then output a similarity indicator
	 * using GNU executable
	 * @param pairs : pairs of files to compare by this worker thread
	 * @param outputPath : output path for indicator information
	 * @throws IOException
	 * @throws ParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void compareGnu(LinkedList<FilePair> pairs, String outputPath) {
		//int s = Comparator.compareTwoPredicates(null, null);
		//int o = Comparator.compareTwoPredicates(null, null);
		//TODO
	}
	
	/**
	 * Compares each two predicate S/O array, then output a similarity indicator
	 * using Perl script
	 * @param pairs : pairs of files to compare by this worker thread
	 * @param outputPath : output path for indicator information
	 * @throws IOException
	 * @throws ParseException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void comparePerl(LinkedList<FilePair> pairs, String outputPath) {
		//int s = Comparator.compareTwoPredicates(null, null);
		//int o = Comparator.compareTwoPredicates(null, null);
		//TODO
	}
	
	/**
	 * Distributes compressed SO/OS matrix of each predicate as well as their index
	 * @param toSendSrcDst : relations between waiting files and destination nodes
	 * @param type : type of destination distributed system
	 * @throws IOException 
	 */
	public void distribute(HashMap<File, DestInfo> toSendSrcDst, int type) throws IOException{
		Iterator<Entry<File, DestInfo>> it = toSendSrcDst.entrySet().iterator();
		switch(type){
			case CTMConstants.CTMDISTRIBUTE_CEDAR:
			    while (it.hasNext()) {
			        Entry<File, DestInfo> pairs = it.next();
			        IOUtils.logLog("Sending via socket " + pairs.getKey() + " to " + pairs.getValue());

			        FileSenderCN fs = new FileSenderCN(pairs.getValue().addr, pairs.getValue().port);
			        fs.sendFile(pairs.getKey().getAbsolutePath(), pairs.getValue().size);//20 * 1024
			        fs.close();
					//TODO check distribution
			        it.remove();
			    }
				break;
			case CTMConstants.CTMDISTRIBUTE_HDFS:
			    while (it.hasNext()) {
			        Entry<File, DestInfo> pairs = it.next();
			        IOUtils.logLog("Sending via SSH " + pairs.getKey() + " to " + pairs.getValue());

//			        FileSenderCN fs = new FileSenderCN(pairs.getValue().addr, pairs.getValue().port);
//			        fs.sendFile(pairs.getKey().getAbsolutePath(), pairs.getValue().size);//20 * 1024
//			        fs.close();
			        
			        SSHExecutor.execute(pairs.getValue().addr, "cmw", "123xsd", "pwd");
					DataDistributor.sendFileSFTP(pairs.getValue().addr, pairs.getValue().port
							, "cmw", "123xsd", pairs.getKey().getAbsolutePath(), pairs.getKey().getName());
					SSHExecutor.execute(pairs.getValue().addr, "cmw", "123xsd", 
							"/home/cmw/Bureau/hadoop-1.2.1/bin/hadoop dfs "
							+ "-copyFromLocal " + pairs.getKey().getName()
							+ " hdfs://localhost:9000/user/cmw/tmp");
					SSHExecutor.execute(pairs.getValue().addr, "cmw", "123xsd", 
							"/home/cmw/Bureau/hadoop-1.2.1/bin/hadoop dfs "
							+ "-ls hdfs://localhost:9000/user/cmw/tmp");
					//TODO check dist
			        it.remove();
			    }
				break;
			default:
				IOUtils.logLog("Thread " + threadId + " wrong distribution type " + type);
				break;
		}
		IOUtils.logLog("Thread " + threadId + " distributed " + toSendSrcDst.size() + " file(s).");
	}
	
	/**
	 * <p>
	 * - Insert the node(subject or object) into the predicate's subject
	 * ArrayList or object ArrayList.
	 * </p>
	 * <p>
	 * - Insert the node(subject or object) into the predicate's HashMap, in
	 * order to get a distinct list of nodes that the predicate has.
	 * </p>
	 * 
	 * @param _node : CTMSubject or CTMObject
	 * @return index of the given node
	 * @throws Exception 
	 */
	public void insertOrIgnorePredicateNodes(DBImpl dbu, CTMPair so) 
			throws Exception {
		//System.out.println("Inserting : "+so.getSubject().toString()+" "+so.getObject().toString());
		String S = so.getSubject().toString();
		String O = so.getObject().toString();
		dbu.insertNode(S);
		dbu.insertNode(O);
		Long iS = dbu.fetchIdByNode(S);
		Long iO = dbu.fetchIdByNode(O);
		//System.out.println("Inserting : "+iS+" "+iO);
		dbu.addSO(new SOLongPair(iS,iO));
	}
	
	/**
	 * Writes a line to the specified predicate file, during the PS phase.
	 * @param filepath : filename of the predicate
	 * @param line : line to write
	 * @throws IOException 
	 */
	private void writeToBigFile(String filepath, String line) 
			throws IOException{
		BufferedWriter writer;
		if(!_predicateWriterList.containsKey(filepath)){
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filepath,true),"UTF-8"));
			_predicateWriterList.put(filepath, writer);
		} else {
			writer = _predicateWriterList.get(filepath);
		}
		writer.write(line);
		writer.newLine();
	}
	
	/**
	 * Writes a line to the invalid triple log file, during the PS phase.
	 * @param filename : filename of the predicate
	 * @param line : line containing invalid triple to write
	 * @throws IOException 
	 */
	public void invalidTripleWriter(String logname, String filename, String line) 
			throws IOException{
		if(_invalidLogWriter == null){
			_invalidLogWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(logname,true),"UTF-8"));
		}
		_invalidLogWriter.write(filename + " " + line);
		_invalidLogWriter.newLine();
	}
	
	/**
	 * Once the whole PS phase is done, run this to close properly all writers
	 * @throws IOException
	 */
	public void closeAllWriters() throws IOException{
		Iterator<Entry<String,BufferedWriter>> it = _predicateWriterList.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String,BufferedWriter> pairs = it.next();
	        IOUtils.logLog("Close BufferedWriter : " + pairs.getKey());
	        if(pairs.getValue()!=null)
	        	pairs.getValue().close();
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    if(_invalidLogWriter != null) _invalidLogWriter.close();
	}
	
	public String getThreadId(){
		return this.threadId;
	}

}
