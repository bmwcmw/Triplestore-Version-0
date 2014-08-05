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
import java.util.List;
import java.util.Map.Entry;

import localIOUtils.IOUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import dataCleaner.CTMPairStr;
import dataCompressor.SOLongPair;
import dataReader.PairReader;

public class InRamDBUtils implements COMMImpl{
	private BiMap<Long, String> nodes;
	private ArrayList<SOLongPair> soList = new ArrayList<SOLongPair>();

	
	public InRamDBUtils(){
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
	public void loadFromFile(String path) {
		try {
			PairReader reader = new PairReader(path);
			CTMPairStr pair = null;
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
	 * <p>In the InRam database, we will sort SOPair list first, then write it line by line</p>
	 */
	@Override
	public final void writeMatS(String outputFilePath, String comparePath, String inFileName) throws IOException {
		String line = "";
		Long indexSize = fetchIndexSize();
		/* SO Matrix */
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
				outSarray = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(comparePath + File.separator + inFileName + ".S", true)));
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
		
		BufferedWriter outArrSO = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFilePath + ".matrixSO", true)));
		if(soList.size()>0){
			//Row
			Long current = soList.get(0).S;
			//Set of a line
			List<Long> lineSet = new ArrayList<Long>();
			line = "";
			long count = 0;
			//Col
			Long zero = new Long(0);
			Long last;
			long blockSize = 1;
			for (SOLongPair p : soList){
				while(count < current){
					outArrSO.newLine();
					count++;
				}
				if(p.S.equals(current)){//lineSet has at least one entry
					lineSet.add(p.O);
				} else {//Output current lineSet(from 2nd entry if exists)
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
					//last entry
					line = line + blockSize;
					if(last < indexSize-1){
						blockSize = indexSize-1 - last;
						line = line + "," + blockSize;
					}
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

	/**
	 * {@inheritDoc}
	 * <p>In the InRam database, we will sort SOPair list first, then write it line by line</p>
	 */
	@Override
	public final void writeMatO(String outputFilePath, String comparePath, String inFileName) throws IOException {
		String line = "";
		Long indexSize = fetchIndexSize();
		/* OS Matrix */
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
				outOarray = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(comparePath + File.separator + inFileName + ".O", true)));
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
		
		BufferedWriter outArrOS = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFilePath + ".matrixOS", true)));
		if(soList.size()>0){
			//Row
			Long current = soList.get(0).O;
			//Set of a line
			List<Long> lineSet = new ArrayList<Long>();
			line = "";
			long count = 0;
			//Col
			Long zero = new Long(0);
			Long last;
			long blockSize = 1;
			for (SOLongPair p : soList){
				while(count < current){
					outArrOS.newLine();
					count++;
				}
				if(p.O.equals(current)){//lineSet has at least one entry
					lineSet.add(p.S);
				} else {//Output current lineSet(from 2nd entry if exists)
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

	/**
	 * {@inheritDoc}
	 * <p>In the InRam database, we will write the BiMap line by line</p>
	 */
	@Override
	public final void writeIndex(String outputFilePath) throws IOException {
		BufferedWriter fInd = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFilePath,true),"UTF-8"));
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

	@Override
	public void writeMeta(String path) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
