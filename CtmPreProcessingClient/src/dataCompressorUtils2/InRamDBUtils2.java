package dataCompressorUtils2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import localIOUtils.IOUtils;

import ctmRdf.CTMConstants;
import ctmRdf.CTMServerConfig;

import dataCompressor.MetaInfoArray;
import dataCompressor.MetaInfoQuadruple;
import dataReader.PairReader;

public class InRamDBUtils2 {
	private final int ModeSO = 0;
	private final int ModeOS = 1;
	private int MyMode;
	private File mainInputFile;
	private String InputFileNameWithoutPred;
	private File auxInputFile;
	private String outPath;

	private HashMap<String, Integer> idNode;
	private CTMServerConfig myConfig = CTMServerConfig.getInstance();

	/*
	 * For every predicate, we store for matrix SO and OS (using SOArrayPair) a
	 * list of #file and Offset (using SOLongPair inside).
	 */
	private MetaInfoArray metaList = new MetaInfoArray();


	/**
	 * 
	 * @param toProcessFilePath : The file to compress
	 * @param outputPath : The folder for the output
	 * @throws Exception
	 */
	public InRamDBUtils2(String toProcessFilePath, String outputPath) throws Exception{
		mainInputFile = new File(toProcessFilePath);
		if(!mainInputFile.exists())
			throw new Exception("Main input file error : must be " 
					+ CTMConstants.SOSortedExt + " or " + CTMConstants.OSSortedExt 
					+ ", here is " + toProcessFilePath);

		InputFileNameWithoutPred = IOUtils.filenameWithoutExt(new File(toProcessFilePath).getName());
		String mainFileFolderPath = mainInputFile.getParent() + File.separator;
		if (IOUtils.getExtension(toProcessFilePath).equals(CTMConstants.SOSortedExt)) {
				MyMode = ModeSO;
				String auxFilePath = mainFileFolderPath + 
						InputFileNameWithoutPred + CTMConstants.OSSortedExt;
				auxInputFile = new File(auxFilePath);
				if(!auxInputFile.exists())
					throw new Exception("Aux input file does not exist : " + auxFilePath);
		} else if (IOUtils.getExtension(toProcessFilePath).equals(CTMConstants.OSSortedExt)) {
				MyMode = ModeOS;
				String auxFilePath = mainFileFolderPath + 
						InputFileNameWithoutPred + CTMConstants.SOSortedExt;
				auxInputFile = new File(auxFilePath);
				if(!auxInputFile.exists())
					throw new Exception("Aux input file does not exist : " + auxFilePath);
		} else {
				throw new Exception("Input file type error : must be " + CTMConstants.SOSortedExt
						+ " or " + CTMConstants.OSSortedExt + ", here is " 
						+ IOUtils.getExtension(toProcessFilePath));
		}
		outPath = outputPath + File.separator + InputFileNameWithoutPred + File.separator;
		File outPutFolder = new File(outPath);
		if (!outPutFolder.exists())
			outPutFolder.mkdir();
		idNode = new HashMap<String, Integer>(100000,0.8f);
	}

	public int fetchIndexSize() {
		return idNode.size();
	}

	public Integer fetchIdByNode(String node) {
		return idNode.get(node);
	}

	/**
	 * Loads the auxiliary index (O for SO, S for OS) to the K-V list. 
	 * @param path
	 */
	public void loadAuxIndexFromFile() {
		try {
			PairReader reader = new PairReader(auxInputFile);
			List<String> pair;
			String tempS;
			String currentEntry = " ";
			int currentIndex = 0;
			if (MyMode == 0) {
				while ((tempS = reader.nextLine()) != null) {
					pair = MyStringTokenizer.tokenize(CTMConstants.delimiter, tempS);
					if(!pair.get(ModeOS).equals(currentEntry)) {
						idNode.put(pair.get(ModeOS), currentIndex);
						currentEntry = pair.get(ModeOS);
						currentIndex++;
					}
				}
			} else {
				while ((tempS = reader.nextLine()) != null) {
					pair = MyStringTokenizer.tokenize(CTMConstants.delimiter, tempS);
					if(!pair.get(ModeSO).equals(currentEntry)) {
						idNode.put(pair.get(ModeSO), currentIndex);
						currentEntry = pair.get(ModeSO);
						currentIndex++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		IOUtils.logLog("Aux index charged from file. Current size of key-value pair(s) : "
				+ fetchIndexSize());
	}

	/**
	 * Compresses the predicate split file.
	 * @param writeIndex : opt to generate the index of auxiliary column or not.
	 * @throws IOException
	 */
	public void compress(boolean writeIndex) throws IOException {
		writeMat(writeIndex);
		if (myConfig._blockLineNb > 0) {
			writeMeta();
		}
	}
	
	private final void writeMat(boolean writeIndex) throws IOException {
		//mainInputFile, outPath
		BufferedWriter outMatWriter;
		BufferedWriter outIndWriter = null;
		int otherColumn;
		if(MyMode == ModeSO) {
//			outMatWriter = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream(outPath + InputFileNameWithoutPred 
//							+ CTMConstants.SOMatrixExt, true)));
			if(writeIndex)
				outIndWriter = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outPath + InputFileNameWithoutPred 
								+ CTMConstants.IndexSExt, true)));
			otherColumn = ModeOS;
		} else {
//			outMatWriter = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream(outPath + InputFileNameWithoutPred 
//							+ CTMConstants.OSMatrixExt, true)));
			if(writeIndex)
				outIndWriter = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(outPath + InputFileNameWithoutPred 
								+ CTMConstants.IndexOExt, true)));
			otherColumn = ModeSO;
		}
		
		PairReader mainReader = new PairReader(mainInputFile);
		List<String> pair;
		String lineFromMain = mainReader.nextLine();
		if(lineFromMain == null)
			throw new IOException("File empty???");
		
		pair = MyStringTokenizer.tokenize(CTMConstants.delimiter, lineFromMain);
		String currentEntry = pair.get(MyMode);
		int currentIndex = 0;
		if(writeIndex) {
			outIndWriter.write(currentIndex + " " + currentEntry);
			outIndWriter.newLine();
		}
		int fileBlockCount = 0;
		if(MyMode == ModeSO) {
			outMatWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outPath + InputFileNameWithoutPred 
							+ CTMConstants.SOMatrixExt + "." + fileBlockCount, true)));
		} else {
			outMatWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outPath + InputFileNameWithoutPred 
							+ CTMConstants.OSMatrixExt + "." + fileBlockCount, true)));
		}
		// Begins from the first "main column": adds first element to metainfo list
		metaList.getList().add(
				new MetaInfoQuadruple(fileBlockCount, currentIndex, 0, 0));
		// Temporary set of objects of one subject
		List<Integer> lineSet = new ArrayList<Integer>();
		lineSet.add(idNode.get(pair.get(otherColumn)));
		//String lineSetOutStr = "";
		StringBuilder lineSetOutBuilder = new StringBuilder();
		lineSetOutBuilder.toString();
		// Temporary indicators of columns
		int last;
		int blockSize = 1;
		int lineCount = 0;
		// For each pair the main sorted predicate file
		while ((lineFromMain = mainReader.nextLine()) != null) {
			pair = MyStringTokenizer.tokenize(CTMConstants.delimiter, lineFromMain);
			if (pair.get(MyMode).equals(currentEntry)) {
				// The K,V store's get is time consuming
				lineSet.add(idNode.get(pair.get(otherColumn)));
			} else {
				/*
				 * When it sees the first "other column" with the next "main column", it
				 * outputs current lineSet of the current "main column", and adds this
				 * "other column" to a new listSet.
				 */
				Collections.sort(lineSet);
				blockSize = 1;
				last = lineSet.get(0);
				// "id-nbentry-offset:[0/1]"
				if (last==0) {
					lineSetOutBuilder.append(currentIndex + "-" + lineSet.size() + "-0:[1]");
				} else {
					lineSetOutBuilder.append(currentIndex + "-" + lineSet.size() + "-0:[0]");
					lineSetOutBuilder.append(last + ",");
				}
				lineSet.remove(0);
				// For a line set of "main column", initializes the number of lines
				int lineMainCol = 0;
				for (int i : lineSet) {
					if (i - last != 0) {
						// If continuous 0 or 1
						if (i - last == 1) {
							blockSize++;
						} else {
							// Adds "1" block
							lineSetOutBuilder.append(blockSize + ",");
							blockSize = i - (last + 1);
							// Adds "0" block
							lineSetOutBuilder.append(blockSize + ",");
							// initialize "1" block
							blockSize = 1;
							// Avoid too large lines by cutting lines
							// with _blockLineLength
							if (lineSetOutBuilder.length() >= myConfig._blockLineLength) {
								// Remove the last virgule
								lineSetOutBuilder.setLength(lineSetOutBuilder.length()-1);
								outMatWriter.write(lineSetOutBuilder.toString());
								outMatWriter.newLine();
								lineCount++;
								// Always begin from 1 (line>=2),
								// "-offset:[0/1]"
								lineSetOutBuilder.setLength(0);
								lineSetOutBuilder.append("-" + (last + 1) + ":[1]");
								// Line counter for current "main column" ++
								lineMainCol++;
							}
							// Avoid too large file by cutting with
							// _blockLineNb
							if (lineCount >= myConfig._blockLineNb) {
								fileBlockCount++;
								outMatWriter.close();
								if(MyMode == ModeSO) {
									outMatWriter = new BufferedWriter(new OutputStreamWriter(
											new FileOutputStream(outPath + InputFileNameWithoutPred 
													+ CTMConstants.SOMatrixExt + "." 
													+ fileBlockCount, true)));
								} else {
									outMatWriter = new BufferedWriter(new OutputStreamWriter(
											new FileOutputStream(outPath + InputFileNameWithoutPred 
													+ CTMConstants.OSMatrixExt + "." 
													+ fileBlockCount, true)));
								}
								// Add meta information of new file
								metaList.getList().add(new MetaInfoQuadruple(fileBlockCount,
										currentIndex, i, lineMainCol));
								// Reset line count
								lineCount = 0;
							}
						}
						last = i;
					}
					// Resets the counter of current(new) "main column"
					lineMainCol = 0;
				}
				// Gets the last entry
				lineSetOutBuilder.append(blockSize);
				if (last < fetchIndexSize() - 1) {
					blockSize = fetchIndexSize() - 1 - last;
					lineSetOutBuilder.append("," + blockSize);
				}
				// When current "main column" finished, reset temp variables
				outMatWriter.write(lineSetOutBuilder.toString());
				outMatWriter.newLine();
				lineCount++;
				lineSet = new ArrayList<Integer>();
				// Increases the id of "main column" if it does not equal to the current one
				//idNode.put(pair.get(MyMode), currentIndex);
				currentEntry = pair.get(MyMode);
				currentIndex++;

				if(writeIndex) {
					outIndWriter.write(currentIndex + " " + currentEntry);
					outIndWriter.newLine();
				}
				
				lineSet.add(idNode.get(pair.get(otherColumn)));
				lineSetOutBuilder.setLength(0);
				/*
				 * Avoid too large file by cutting with _blockLineNb
				 * (While only 1 entry in lineSet or just ran out lineSet)
				 */
				if (lineCount >= myConfig._blockLineNb) {
					fileBlockCount++;
					outMatWriter.close();
					if(MyMode == ModeSO) {
						outMatWriter = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream(outPath + InputFileNameWithoutPred 
										+ CTMConstants.SOMatrixExt + "." + fileBlockCount, true)));
					} else {
						outMatWriter = new BufferedWriter(new OutputStreamWriter(
								new FileOutputStream(outPath + InputFileNameWithoutPred 
										+ CTMConstants.OSMatrixExt + "." + fileBlockCount, true)));
					}
					// Add meta information of new file
					metaList.getList().add(
							new MetaInfoQuadruple(fileBlockCount,
									currentIndex, 0, 0));
					// Reset line count
					lineCount = 0;
				}
				/* CONTINUE WHILE BLOCK */
			}
		}
		if (outMatWriter != null)
			outMatWriter.close();
		if (outIndWriter != null)
			outIndWriter.close();

		if(writeIndex) {
			IOUtils.logLog("Main index file written in file. Current size of key-value pair(s) : "
					+ currentIndex + "+/-1");
			writeAuxIndex();
			IOUtils.logLog("Matrix and main&aux index written to file in block mode");
		} else {
			IOUtils.logLog("Matrix only written to file in block mode");
		}
	}

	/**
	 * <p>
	 * Writes the auxiliary index line by line if it does not exist yet.
	 * </p>
	 */
	private final void writeAuxIndex() throws IOException {
		IOUtils.logLog("Begin writting aux index file. Current size of key-value pair(s) : "
				+ fetchIndexSize());
		String outAuxFileName;
		if(MyMode == ModeSO) {
			outAuxFileName = outPath + File.separator 
					+ InputFileNameWithoutPred + CTMConstants.IndexOExt;
		} else {
			outAuxFileName = outPath + File.separator 
					+ InputFileNameWithoutPred + CTMConstants.IndexSExt;
		}
		BufferedWriter fInd  = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outAuxFileName, true), "UTF-8"));
		for (Entry<String, Integer> pairs : idNode.entrySet()) {
			fInd.write(pairs.getValue() + " " + pairs.getKey());
			fInd.newLine();
		}
		if (fInd != null) {
			fInd.flush();
			fInd.close();
		}
		IOUtils.logLog("Auxiliary Index " + outAuxFileName + " written to file");
	}

	/**
	 * <p>
	 * Writes the metadata of file blocks if block mode enabled.
	 * </p>
	 */
	private final void writeMeta() throws IOException {
		String outMetaFileName;
		if(MyMode == ModeSO) {
			outMetaFileName = outPath + File.separator 
					+ InputFileNameWithoutPred + CTMConstants.MetadataSExt;
		} else {
			outMetaFileName = outPath + File.separator 
					+ InputFileNameWithoutPred + CTMConstants.MetadataOExt;
		}
		BufferedWriter fMeta = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outMetaFileName, true), "UTF-8"));
		for (MetaInfoQuadruple m : metaList.getList()) {
			fMeta.write(m.nFile + " " + m.id + " " + m.offsetID + " "
					+ m.offsetLine);
			fMeta.newLine();
		}
		if (fMeta != null) {
			fMeta.flush();
			fMeta.close();
		}
		IOUtils.logLog("Metadata " + outMetaFileName + " written to file");
	}
}
