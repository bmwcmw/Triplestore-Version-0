package indexNodesDBUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import localIOUtils.IOUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import ctmRdf.CTMConstants;
import ctmRdf.CTMServerConfig;

import dataCleaner.RDFPairStr;
import dataCompressor.MetaInfoArray;
import dataCompressor.MetaInfoQuadruple;
import dataCompressor.SOLongPair;
import dataReader.PairReader;

public class InRamDBUtils2 implements DBImpl{
	private HashMap<Long, String> nodesLongStr;
	private HashMap<String, Long> nodesStrLong;
	private ArrayList<SOLongPair> soList = new ArrayList<SOLongPair>();
	private CTMServerConfig myConfig = CTMServerConfig.getInstance();
	
	/*
	 * For every predicate, we store for matrix SO and OS (using SOArrayPair) a list of 
	 * #file and Offset (using SOLongPair inside).
	 */
	private MetaInfoArray metaList = new MetaInfoArray();
	
	public InRamDBUtils2(){
		nodes = HashBiMap.create();
	}
	
	@Override
	public void addSO(SOLongPair so){
		soList.add(so);
	}
	
	@Override
	public Long fetchSOSize(){
		return new Long(soList.size());
	}
	
	@Override
    public Long fetchIndexSize(){
    	return new Long(nodes.size());
    }

	@Override
    public void insertNode(String node){
		if(fetchIdByNode(node)==null){
			long newid = nodes.size();
	    	nodes.put(newid, node);
		}
    }

	@Override
	public Long fetchIdByNode(String node){
		return nodes.inverse().get(node);
	}

	@Override
	public String fetchNodeById(Long index){
		return nodes.get(index);
	}
	
	@Override
	public void cleanAll(){
		nodes = HashBiMap.create();
		soList = new ArrayList<SOLongPair>();
	}
	
//	@Override
//	public BiMap<Long, String> fetchIndex(){
//		return nodes;
//	}
//	
//	@Override
//	public ArrayList<SOLongPair> fetchSOList(){
//		return soList;
//	}

	@Override
	public void closeAll() {}

	@Override
	public void loadIndexFromFile(String path) {
		try {
			PairReader reader = new PairReader(path);
			RDFPairStr pair = null;
			while ((pair = reader.nextStr()) != null) {
				nodes.put(Long.valueOf(pair.getSubject()), pair.getObject());
			}
		} catch (IOException e) {
			e.printStackTrace();
			cleanAll();
		} catch (ParseException e) {
			e.printStackTrace();
			cleanAll();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			cleanAll();
		} finally {
		}
		IOUtils.logLog("File charged. Current size of key-value pair(s) : " + fetchLoadedSize());
	}

	@Override
	public Long fetchLoadedSize() {
		return new Long(fetchIndexSize());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writePredToFile(String inFileName, String outputFilePath, String comparePath) 
			throws IOException {
		writeMatS(inFileName, outputFilePath, comparePath);
		if(myConfig._blockLineNb>0){
			writeMeta(outputFilePath + CTMConstants.SOMatrixExt);
			metaList.empty();
		}
		writeMatO(inFileName, outputFilePath, comparePath);
		writeIndex(outputFilePath);
		if(myConfig._blockLineNb>0){
			writeMeta(outputFilePath + CTMConstants.OSMatrixExt);
			metaList.empty();
		}
	}

	/**
	 * <p>In the InRam database, we will sort SOPair list first, then write it line by line</p>
	 */
	public final void writeMatS(String inFileName, String outputFilePath, String comparePath) 
			throws IOException {
		String line = "";
		Long indexSize = fetchIndexSize();
		/* SO Matrix */
		/* Sort the S-O pair list by S, so we will have : 
		 * S1 Ox
		 * S1 Ox
		 * S1 Ox
		 * S2 Ox
		 * S2 Ox
		 * ...
		 */
		Collections.sort(soList, new Comparator<SOLongPair>() {
			@Override
			public int compare(final SOLongPair p1, final SOLongPair p2) {
				return p1.S.compareTo(p2.S);
			}
        });
		IOUtils.logLog("SO sorted");
		
		/* Write sorted S array file if the parameter isn't null */
		if(comparePath!=null){
			BufferedWriter outSarray = null;
			try{
				outSarray = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
						comparePath + File.separator + inFileName + CTMConstants.SArrayExt, true)));
				for (SOLongPair pair : soList){
					outSarray.write(fetchNodeById(pair.S));
					outSarray.newLine();
				}
			} finally {
				if(outSarray!=null)
					outSarray.close();
			}
			IOUtils.logLog("S array written to Comparison Path");
		}
		
		/* If block mode disabled */
		if(myConfig._blockLineNb==0){
			BufferedWriter outArrSO = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFilePath + CTMConstants.SOMatrixExt, true)));
			if(soList.size()>0){
				//Begin from the first subject(first row)
				Long current = soList.get(0).S;
				//Temporary set of objects of one subject
				List<Long> lineSet = new ArrayList<Long>();
				line = "";
				long count = 0;
				//Temporary indicators of columns
				Long zero = new Long(0);
				Long last;
				long blockSize = 1;
				// For each pair P in the S-O list
				for (SOLongPair p : soList){
					// Add break line until we reach the right line (p.S)
					while(count < current){
						outArrSO.newLine();
						count++;
					}
					/* 
					 * When we reach the S, we begin to add all O with this S, until 
					 * the first O with the next S
					 */
					if(p.S.equals(current)){
						lineSet.add(p.O);
					} else {
						/* Here we see the first O with the next S, then we output 
						 * current lineSet of the current S, and add this O to a new
						 * listSet
						 */
						Collections.sort(lineSet);
						blockSize = 1;
						last = lineSet.get(0);
						if(last.equals(zero)){
							line += lineSet.size() + ":[1]";
						} else {
							line += lineSet.size() + ":[0]";
							line += last + ",";
						}
						lineSet.remove(0);
						/*DEBUG*/
	//					String temp = current + " has " + last + ",";
						for (Long i : lineSet){
	//						temp += i + ",";
							if(i - last != 0){
								if(i - last == 1){
									blockSize ++;
								} else {
									//add "1" block
									line += blockSize + ",";
									blockSize = i - last - 1;
									//add "0" block
									line += blockSize + ",";
									//initialize "1" block
									blockSize = 1;
									//Avoid too large lines
									if(line.length()>MAXSTRLENGTH){
										outArrSO.write(line);
										line = "";
									}
								}
								last = i;
							}
						}
						//Get last entry
						line = line + blockSize;
						if(last < indexSize-1){
							blockSize = indexSize-1 - last;
							line = line + "," + blockSize;
						}
						/*DEBUG*/
	//					System.out.println(temp);
	//					System.out.println(line);
						outArrSO.write(line);
						outArrSO.newLine();
						lineSet = new ArrayList<Long>();
						current = p.S;
						lineSet.add(p.O);
						line = "";
						count++;
					}
				}
				while(count < indexSize-1){
					outArrSO.newLine();
					count++;
				}
			}
			if(outArrSO!=null) outArrSO.close();
			IOUtils.logLog("SO written to file");
		}

		/* If block mode enabled */
		else {
			BufferedWriter outArrSO = null;
			int fileBlockCount = 0;
			if(soList.size()>0){
				outArrSO = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(outputFilePath + CTMConstants.SOMatrixExt 
									+ "." + fileBlockCount, true)));
				//Begin from the first subject(first row)
				Long current = soList.get(0).S;
				//Add first element to metainfo list
				metaList.getList().add(new MetaInfoQuadruple(fileBlockCount,current,0,0));
				//Temporary set of objects of one subject
				List<Long> lineSet = new ArrayList<Long>();
				line = "";
				//Temporary indicators of columns
				Long zero = new Long(0);
				Long last;
				long blockSize = 1;
				long lineCount = 0;
				// For each pair P in the S-O list
				for (SOLongPair p : soList){
					/* 
					 * When we reach the S, we begin to add all O with this S, until 
					 * the first O with the next S
					 */
					if(p.S.equals(current)){
						lineSet.add(p.O);
					} else {
						/* Here we see the first O with the next S, then we output 
						 * current lineSet of the current S, and add this O to a new
						 * listSet
						 */
						Collections.sort(lineSet);
						blockSize = 1;
						last = lineSet.get(0);
						//"id-nbentry-offset:[0/1]"
						if(last.equals(zero)){
							line += current + "-" + lineSet.size() + "-0:[1]";
						} else {
							line += current + "-" + lineSet.size() + "-0:[0]";
							line += last + ",";
						}
						lineSet.remove(0);
						//For a line set of S
						int lineS = 0;
						for (Long i : lineSet){
							if(i - last != 0){
								//If continuous 0 or 1
								if(i - last == 1){
									blockSize ++;
								} else {
									//add "1" block
									line += blockSize + ",";
									blockSize = i - (last + 1);
									//add "0" block
									line += blockSize + ",";
									//initialize "1" block
									blockSize = 1;
									//Avoid too large lines by cutting lines with _blockLineLength
									if(line.length() >= myConfig._blockLineLength){
										//Remove the last virgule
										line = line.substring(0, line.length()-1);
										outArrSO.write(line); outArrSO.newLine(); lineCount++;
										//Always begin from 1 (line>=2), "-offset:[0/1]"
										line = "-" + (last + 1) + ":[1]";
										//Line counter for current S ++
										lineS++;
									}
									//Avoid too large file by cutting with _blockLineNb
									if(lineCount >= myConfig._blockLineNb){
										fileBlockCount++;
										outArrSO.close();
										outArrSO = new BufferedWriter(new OutputStreamWriter(
													new FileOutputStream(outputFilePath 
															+ CTMConstants.SOMatrixExt 
															+ "." + fileBlockCount, true)));
										//Add meta information of new file
										metaList.getList().add(
												new MetaInfoQuadruple(fileBlockCount, current, i,
														lineS));
										//Reset line count
										lineCount = 0;
									}
								}
								last = i;
							}
							//Reset the counter of current S
							lineS = 0;
						}
						//Get last entry
						line = line + blockSize;
						if(last < indexSize-1){
							blockSize = indexSize-1 - last;
							line = line + "," + blockSize;
						}
						//Current S finished, reset temp variables
						outArrSO.write(line); outArrSO.newLine(); lineCount++;
						lineSet = new ArrayList<Long>();
						current = p.S;
						lineSet.add(p.O);
						line = "";
						/*Avoid too large file by cutting with _blockLineNb
						 *(While only 1 entry in lineSet or just ran out lineSet)
						 */
						if(lineCount >= myConfig._blockLineNb){
							fileBlockCount++;
							outArrSO.close();
							outArrSO = new BufferedWriter(new OutputStreamWriter(
										new FileOutputStream(outputFilePath 
												+ CTMConstants.SOMatrixExt 
												+ "." + fileBlockCount, true)));
							//Add meta information of new file
							metaList.getList().add(
									new MetaInfoQuadruple(fileBlockCount, current, 0, 0));
							//Reset line count
							lineCount=0;
						}
					}
				}
			}
			if(outArrSO!=null) outArrSO.close();
			IOUtils.logLog("SO written to file in block mode");
		}
	}

	/**
	 * <p>In the InRam database, we will sort SOPair list first, then write it line by line</p>
	 */
	public final void writeMatO(String inFileName, String outputFilePath, String comparePath) 
			throws IOException {
		String line = "";
		Long indexSize = fetchIndexSize();
		/* OS Matrix */
		/* Sort the O-S pair list by O, so we will have : 
		 * Sx O1
		 * Sx O2
		 * Sx O2
		 * Sx O2
		 * Sx O3
		 * ...
		 */
		Collections.sort(soList, new Comparator<SOLongPair>() {
			@Override
			public int compare(final SOLongPair p1, final SOLongPair p2) {
				return p1.O.compareTo(p2.O);
			}
        });
		IOUtils.logLog("OS sorted");

		/* Write sorted O array file if the parameter isn't null */
		if(comparePath!=null){
			BufferedWriter outOarray = null;
			try{
				outOarray = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
						comparePath + File.separator + inFileName + CTMConstants.OArrayExt, true)));
				for (SOLongPair pair : soList){
					outOarray.write(fetchNodeById(pair.O));
					outOarray.newLine();
				}
			} finally {
				if(outOarray!=null)
					outOarray.close();
			}
			IOUtils.logLog("O array written to Comparison Path");
		}

		/* If block mode disabled */
		if(myConfig._blockLineNb==0){
			BufferedWriter outArrOS = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFilePath + CTMConstants.OSMatrixExt, true)));
			if(soList.size()>0){
				//Begin from the first object(first row)
				Long current = soList.get(0).O;
				//Temporary set of objects of one subject
				List<Long> lineSet = new ArrayList<Long>();
				line = "";
				long count = 0;
				//Temporary indicators of columns
				Long zero = new Long(0);
				Long last;
				long blockSize = 1;
				// For each pair P in the S-O list
				for (SOLongPair p : soList){
					// Add break line until we reach the right line (p.O)
					while(count < current){
						outArrOS.newLine();
						count++;
					}
					/* 
					 * When we reach the O, we begin to add all S with this O, until 
					 * the first S with the next O
					 */
					if(p.O.equals(current)){
						lineSet.add(p.S);
					} else {
						/* Here we see the first S with the next O, then we output 
						 * current lineSet of the current O, and add this S to a new
						 * listSet
						 */
						Collections.sort(lineSet);
						blockSize = 1;
						last = lineSet.get(0);
						if(last.equals(zero)){
							line += lineSet.size() + ":[1]";
						} else {
							line += lineSet.size() + ":[0]";
							line += last + ",";
						}
						lineSet.remove(0);
						/*DEBUG*/
	//					String temp = current + " has " + last + ",";
						for (Long i : lineSet){
	//						temp += i + ",";
							if(i - last != 0){ 
								if(i - last == 1){
									blockSize ++;
								} else {
									//add "1" block
									line += blockSize + ",";
									blockSize = i - last - 1;
									//add "0" block
									line += blockSize + ",";
									//initialize "1" block
									blockSize = 1;
									//Avoid too large lines
									if(line.length()>MAXSTRLENGTH){
										outArrOS.write(line);
										line = "";
									}
								}
								last = i;
							}
						}
						//last entry
						line = line + blockSize;
						if(last < indexSize-1){
							blockSize = indexSize-1 - last;
							line = line + "," + blockSize;
						}
						/*DEBUG*/
	//					System.out.println(temp);
	//					System.out.println(line);
						outArrOS.write(line);
						outArrOS.newLine();
						lineSet = new ArrayList<Long>();
						current = p.O;
						lineSet.add(p.S);
						line = "";
						count++;
					}
				}
				while(count < indexSize-1){
					outArrOS.newLine();
					count++;
				}
			}
			if(outArrOS!=null) outArrOS.close();
			IOUtils.logLog("OS written to file");
		} 

		/* If block mode enabled */
		else {
			BufferedWriter outArrOS = null;
			int fileBlockCount = 0;
			if(soList.size()>0){
				outArrOS = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream(outputFilePath + CTMConstants.OSMatrixExt 
									+ "." + fileBlockCount, true)));
				//Begin from the first object(first row)
				Long current = soList.get(0).O;
				//Add first element to metainfo list
				metaList.getList().add(new MetaInfoQuadruple(fileBlockCount,current,0,0));
				//Temporary set of objects of one subject
				List<Long> lineSet = new ArrayList<Long>();
				line = "";
				//Temporary indicators of columns
				Long zero = new Long(0);
				Long last;
				long blockSize = 1;
				long lineCount = 0;
				// For each pair P in the S-O list
				for (SOLongPair p : soList){
					/* 
					 * When we reach the O, we begin to add all S with this O, until 
					 * the first S with the next O
					 */
					if(p.O.equals(current)){
						lineSet.add(p.S);
					} else {
						/* Here we see the first S with the next O, then we output 
						 * current lineSet of the current O, and add this S to a new
						 * listSet
						 */
						Collections.sort(lineSet);
						blockSize = 1;
						last = lineSet.get(0);
						//"id-nbentry-offset:[0/1]"
						if(last.equals(zero)){
							line += current + "-" + lineSet.size() + "-0:[1]";
						} else {
							line += current + "-" + lineSet.size() + "-0:[0]";
							line += last + ",";
						}
						lineSet.remove(0);
						//For a line set of O
						int lineO = 0;
						for (Long i : lineSet){
							if(i - last != 0){
								//If continuous 0 or 1
								if(i - last == 1){
									blockSize ++;
								} else {
									//add "1" block
									line += blockSize + ",";
									blockSize = i - (last + 1);
									//add "0" block
									line += blockSize + ",";
									//initialize "1" block
									blockSize = 1;
									//Avoid too large lines by cutting lines with _blockLineLength
									if(line.length() >= myConfig._blockLineLength){
										//Remove the last virgule
										line = line.substring(0, line.length()-1);
										outArrOS.write(line); outArrOS.newLine(); lineCount++;
										//Always begin from 1 (line>=2), "-offset:[0/1]"
										line = "-" + (last + 1) + ":[1]";
										//Line counter for current O ++
										lineO ++;
									}
									//Avoid too large file by cutting with _blockLineNb
									if(lineCount >= myConfig._blockLineNb){
										fileBlockCount++;
										outArrOS.close();
										outArrOS = new BufferedWriter(new OutputStreamWriter(
													new FileOutputStream(outputFilePath 
															+ CTMConstants.OSMatrixExt 
															+ "." + fileBlockCount, true)));
										//Add meta information of new file
										metaList.getList().add(
												new MetaInfoQuadruple(fileBlockCount, current, i,
														lineO));
										//Reset line count
										lineCount = 0;
									}
								}
								last = i;
							}
							//Reset the counter of current O
							lineO = 0;
						}
						//Get last entry
						line = line + blockSize;
						if(last < indexSize-1){
							blockSize = indexSize-1 - last;
							line = line + "," + blockSize;
						}
						//Current S finished, reset temp variables
						outArrOS.write(line); outArrOS.newLine(); lineCount++;
						lineSet = new ArrayList<Long>();
						current = p.O;
						lineSet.add(p.S);
						line = "";
						/*Avoid too large file by cutting with _blockLineNb
						 *(While only 1 entry in lineSet or just ran out lineSet)
						 */
						if(lineCount >= myConfig._blockLineNb){
							fileBlockCount++;
							outArrOS.close();
							outArrOS = new BufferedWriter(new OutputStreamWriter(
										new FileOutputStream(outputFilePath 
												+ CTMConstants.OSMatrixExt 
												+ "." + fileBlockCount, true)));
							//Add meta information of new file
							metaList.getList().add(
									new MetaInfoQuadruple(fileBlockCount, current, 0, 0));
							//Reset line count
							lineCount=0;
						}
					}
				}
			}
			if(outArrOS!=null) outArrOS.close();
			IOUtils.logLog("OS written to file in block mode");
		}
	}

	/**
	 * <p>In the InRam database, we will write the BiMap line by line</p>
	 */
	public final void writeIndex(String outputFilePath) throws IOException {
		BufferedWriter fInd = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				outputFilePath + CTMConstants.IndexExt, true), "UTF-8"));
		for(Entry<Long, String> pairs : nodes.entrySet()){
			fInd.write(pairs.getKey() + " " + pairs.getValue());
			fInd.newLine();
		}
		if (fInd != null) {
			fInd.flush();
			fInd.close();
		}
		IOUtils.logLog("Index written to file");
	}

	/**
	 * <p>If block mode enabled, writes the metadata of file blocks</p>
	 */
	public void writeMeta(String outFilePath) throws IOException {
		BufferedWriter fMeta = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				outFilePath + CTMConstants.MetadataExt, true), "UTF-8"));
		for(MetaInfoQuadruple m : metaList.getList()){
			fMeta.write(m.nFile + " " + m.id + " " + m.offsetID + " " + m.offsetLine);
			fMeta.newLine();
		}
		if (fMeta != null) {
			fMeta.flush();
			fMeta.close();
		}
		IOUtils.logLog("Metadata written to file");
	}
}
