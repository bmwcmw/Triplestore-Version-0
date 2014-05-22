package dataManagement;

import indexNodesDBUtils.DBUtils;

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
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;

import commandRunner.ComparisonPreparator;
import localIOUtils.IOUtils;
import dataCleaner.CTMDoubleStr;
import dataCleaner.CTMTriple;
import dataCleaner.CTMDouble;
import dataComparator.Comparator;
import dataCompressor.DgapCompressor;
import dataCompressor.SOIntegerPair;
import dataReader.N3Reader;
import dataReader.SOReader;

/**
 * <p>
 * RDF(N3) data manager :
 * </p>
 * <p>
 * Reader / Cleaner / Partitioner / ... of N3 data files
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
	 * Constructor - needs id of caller thread
	 */
	public DataManager(String thread) {
		threadId = thread;
	}

	public void printPrefix() {
		_prefixManager.print();
	}

	public synchronized void addPrefix(String prefix, String uri) {
		_prefixManager.addPrefix(prefix, uri);
	}

	/**
	 * <p>Gets data from a specified file list of N3 files, eliminates 
	 * invalid tokens, then splits and stores data into files named with each 
	 * predicate</p>
	 * @param n3Src
	 * @param outputPath
	 * @param nsPath
	 * @param invalidPath
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
	 * @param psSrc
	 * @param outputPath
	 * @param nsPath
	 * @param invalidPath
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
	    	SOReader reader = new SOReader(inFilePath);
			IOUtils.logLog("Thread " + threadId + " Pos : "	+ inFileName);
			CTMDouble so = null;
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
	 * @param psSrc
	 * @param outputPath
	 * @param nsPath
	 * @param invalidPath
	 * @param dbu
	 * @return 0 if OK, -1 if error.
	 * @throws IOException
	 * @throws ParseException
	 * @throws SQLException
	 */
	public int indexedCompress(ArrayList<File> psSrc, String outputPath, 
			DBUtils dbu, String comparePath) throws IOException, ParseException, SQLException {
	    String inFilePath;
    	String inFileName;
	    for (File f : psSrc){
	    	inFilePath = f.getAbsolutePath();
	    	inFileName = f.getName();
	    	SOReader reader = new SOReader(inFilePath);
			IOUtils.logLog("Thread " + threadId + " Compression : "	+ inFileName);
			CTMDouble so = null;
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
	 * Prepares predicate files(using Perl script) to facilitate comparison
	 * @param compressedSrc
	 * @param outputPath
	 * @throws IOException 
	 */
	public void prepareComparePerl(ArrayList<File> psSrc, String outputPath) throws IOException{
	    String inFilePath;
	    ComparisonPreparator cp = new ComparisonPreparator();
	    for (File f : psSrc){
	    	inFilePath = f.getAbsolutePath();
	    	IOUtils.logLog(cp.execute(inFilePath, outputPath));
	    }
		IOUtils.logLog("Thread " + threadId + " Preparation all done");
	}
	
	/**
	 * Prepares predicate files(using java function) to facilitate comparison
	 * @param compressedSrc
	 * @param outputPath
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
	    	SOReader reader = new SOReader(inFilePath);
			IOUtils.logLog("Thread " + threadId + " Pre-compare : "	+ inFileName);
			CTMDoubleStr so = null;
			while ((so = reader.nextStr()) != null) {
				writeToBigFile(outFileSPath, so.getSubject());
				writeToBigFile(outFileOPath, so.getObject());
			}
	    }
		IOUtils.logLog("Thread " + threadId + " Pre-compare all done");
	}
	

	
	/**
	 * Compares each two predicate S/O array, then output a similarity indicator
	 * @param compressedSrc
	 * @param outputPath
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public void compare(ArrayList<File> compressedSrc, String outputPath) 
			throws IOException, ParseException, InterruptedException, ExecutionException{
		int s = Comparator.compareTwoPredicates(null, null);
		int o = Comparator.compareTwoPredicates(null, null);
		//TODO
	}
	
	/**
	 * Distributes compressed SO/OS matrix of each predicate as well as their index
	 * @param compressedSrc
	 * @param outputList
	 */
	public void distribute(ArrayList<File> compressedSrc, JSONArray outputList){
		//TODO
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
	 * @throws SQLException 
	 */
	public void insertOrIgnorePredicateNodes(DBUtils dbu, CTMDouble so) throws SQLException {
		String S = so.getSubject().toString();
		String O = so.getObject().toString();
		Integer iS = dbu.fetchIdByNode(S);
		Integer iO = dbu.fetchIdByNode(O);
		if(iS == null){
		    iS = dbu.insertNode(S);
		} else {
		    iS = dbu.fetchIdByNode(S);
		}
		if(iO == null){
			iO = dbu.insertNode(O);
		} else {
			iO = dbu.fetchIdByNode(O);
		}
		dbu.addSO(new SOIntegerPair(iS,iO));
	}	
	
	/**
	 * Writes a line to the specified predicate file, during the PS phase.
	 * @param filepath : filename of the predicate
	 * @param line : line to write
	 * @throws IOException 
	 */
	private void writeToBigFile(String filepath, String line) throws IOException{
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
	        pairs.getValue().close();
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    if(_invalidLogWriter != null) _invalidLogWriter.close();
	}
	
	public String getThreadId(){
		return this.threadId;
	}

}
