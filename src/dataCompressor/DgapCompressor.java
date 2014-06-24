package dataCompressor;

import indexNodesDBUtils.DBImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.BiMap;

import localIOUtils.IOUtils;


/**
 * <p>Data compressor using D-gap</p>
 * <p>Calls the CTM BitMat compressor unit</p>
 * 
 * @author Cedar
 *
 */
public class DgapCompressor {
	
	//Since JVM will be very inefficient while a String become large, we flush it periodically.
	private static final int MAXSTRLENGTH = 40000;
	
	public static void writeCompressedFormat(String inFileName, String outputPath, 
			DBImpl dbu, String comparePath) throws IOException, SQLException{
		Integer indexSize;
		Integer soSize;
		String line = "";
		indexSize = dbu.fetchIndexSize();
		soSize = dbu.fetchSOSize();
		IOUtils.logLog("Start Compressing into matrix");
		IOUtils.logLog("All nodes : " + indexSize + " (S,O) pairs : "
				+ soSize);
		ArrayList<SOIntegerPair> arr = dbu.fetchSOList();
    	String outputFilePath = outputPath + File.separator + inFileName;
		
		/* SO Matrix */
		Collections.sort(arr, new Comparator<SOIntegerPair>() {
			@Override
			public int compare(final SOIntegerPair p1, final SOIntegerPair p2) {
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
				for (SOIntegerPair pair : arr){
					outSarray.write(dbu.fetchNodeById(pair.S));
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
		if(arr.size()>0){
			//Row
			Integer current = arr.get(0).S;
			List<Integer> lineSet = new ArrayList<Integer>();
			line = "";
			Integer count = 0;
			//Col
			Integer zero = new Integer(0);
			Integer last;
			int blockSize = 1;
			for (SOIntegerPair p : arr){
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
					for (Integer i : lineSet){
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
					lineSet = new ArrayList<Integer>();
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
		
		/* OS Matrix */
		Collections.sort(arr, new Comparator<SOIntegerPair>() {
			@Override
			public int compare(final SOIntegerPair p1, final SOIntegerPair p2) {
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
				for (SOIntegerPair pair : arr){
					outOarray.write(dbu.fetchNodeById(pair.O));
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
		if(arr.size()>0){
			//Row
			Integer current = arr.get(0).O;
			List<Integer> lineSet = new ArrayList<Integer>();
			line = "";
			Integer count = 0;
			//Col
			Integer zero = new Integer(0);
			Integer last;
			int blockSize = 1;
			for (SOIntegerPair p : arr){
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
					for (Integer i : lineSet){
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
					lineSet = new ArrayList<Integer>();
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
		
		/* Index of nodes */
		writeIndex(outputFilePath + ".index", dbu);
	}
	
	public static void writeIndex(String outputFilePath, DBImpl dbu) 
			throws IOException, SQLException{
		@SuppressWarnings("unchecked")
		BiMap<Integer, String> indexNodes = (BiMap<Integer, String>) dbu.fetchIndex();
		BufferedWriter fInd = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputFilePath,true),"UTF-8"));
		for(Entry<Integer, String> pairs : indexNodes.entrySet()){
			fInd.write(pairs.getKey() + " " + pairs.getValue());
			fInd.newLine();
		}
		if (fInd != null) {
			fInd.flush();
			fInd.close();
		}
		IOUtils.logLog("Index written to file");
	}
	
//	unsigned int dgap_compress(unsigned char *in, unsigned int size, unsigned char *out)
//	{
//		unsigned long long i = 0;
//		unsigned long count = 0;
//		unsigned int idx = 0;
//
//		bool flag = in[0] & 0x80;
////		printf("[%u] ", flag);
//		out[0] = flag;
//		idx += 1;
//
//		for (i = 0; i < size*8; i++) {
//			unsigned char c = 0x80;
//			c >>= (i%8);
//			if (flag) {
//				if (in[i/8] & c) {
//					count++;
//					if (count == pow(2, (sizeof(unsigned long) << 3)) ) {
//						printf("***** ERROR Count reached limit\n");
//						fflush(stdout);
//						exit (1);
//					}
//				} else {
//					flag = 0;
////					printf("%u ", count);
//					memcpy(&out[idx], &count, GAP_SIZE_BYTES);
//					idx += GAP_SIZE_BYTES;
//					count = 1;
//				}
//			} else {
//				if (!(in[i/8] & c)) {
//					count++;
//					if (count == pow(2, 8*sizeof(unsigned long))) {
//						printf("***** ERROR Count reached limit\n");
//						fflush(stdout);
//						exit (1);
//					}
//				} else {
//					flag = 1;
////					printf("%u ", count);
//					memcpy(&out[idx], &count, GAP_SIZE_BYTES);
//					idx += GAP_SIZE_BYTES;
//					count = 1;
//				}
//			}
//		}
//
////		printf("%u ", count);
//		memcpy(&out[idx], &count, GAP_SIZE_BYTES);
//		idx += GAP_SIZE_BYTES;
////		printf("\n");
//		if (idx >= pow(2, 8*ROW_SIZE_BYTES)) {
//			printf("**** dgap_compress: ERROR size is greater than 2^%u %u\n", 8*ROW_SIZE_BYTES, idx);
//			fflush(stdout);
////			exit(1);
//		}
//		return idx;
//	}
//	////////////////////////////////////////////////////////
//	unsigned int dgap_compress_new(unsigned char *in, unsigned int size, unsigned char *out)
//	{
//		unsigned int i = 0;
//		unsigned int count = 0;
//		unsigned int idx = 0;
//
//		bool flag = in[0] & 0x80;
//		out[0] = flag;
//		idx += 1;
//
//		for (i = 0; i < size; i++) {
//			if (in[i] == 0x00) {
//				//All 0 bits
//				if (!flag) {
//					count += 8;
//				} else {
//		//			printf("%u ", count);
//					memcpy(&out[idx], &count, GAP_SIZE_BYTES);
//					flag = 0;
//					idx += GAP_SIZE_BYTES;
//					count = 8;
//
//				}
//			} else if (in[i] == 0xff) {
//				if (flag) {
//					count += 8;
//				} else {
//					memcpy(&out[idx], &count, GAP_SIZE_BYTES);
//					flag = 1;
//					idx += GAP_SIZE_BYTES;
//					count = 8;
//				}
//			} else {
//				//mix of 0s and 1s byte
//				for (unsigned short j = 0; j < 8; j++) {
//					if (!(in[i] & (0x80 >> j))) {
//						//0 bit
//						if (!flag) {
//							count++;
//						} else {
//							memcpy(&out[idx], &count, GAP_SIZE_BYTES);
//							flag = 0;
//							idx += GAP_SIZE_BYTES;
//							count = 1;
//						}
//					} else {
//						//1 bit
//						if (flag) {
//							count++;
//						} else {
//							memcpy(&out[idx], &count, GAP_SIZE_BYTES);
//							flag = 1;
//							idx += GAP_SIZE_BYTES;
//							count = 1;
//						}
//
//					}
//					
//				}
//			}
//		}
//
//		memcpy(&out[idx], &count, GAP_SIZE_BYTES);
//		idx += GAP_SIZE_BYTES;
//		return idx;
//	}
//	
//	////////////////////////////////////////////////////////////
//	
//	void cumulative_dgap(unsigned char *in, unsigned int size, unsigned char *out)
//	{
//		unsigned int cnt = 0;
//		unsigned int total_cnt = (size-1)/GAP_SIZE_BYTES;
//		#ifdef USE_MORE_BYTES
//		unsigned long bitcnt = 0;
//		unsigned long tmpcnt = 0;
//		#else
//		unsigned int bitcnt = 0;
//		unsigned int tmpcnt = 0;
//		#endif	
//		
//		out[0] = in[0];
//		
//		while (cnt < total_cnt) {
//		memcpy(&tmpcnt, &in[(cnt)*GAP_SIZE_BYTES + 1], GAP_SIZE_BYTES);
//		bitcnt += tmpcnt;
//		
//		memcpy(&out[(cnt)*GAP_SIZE_BYTES + 1], &bitcnt, GAP_SIZE_BYTES);
//		
//		cnt++;
//		}
//		
//	}
//	
//	////////////////////////////////////////////////////////////
//	
//	void bitmat_cumulative_dgap(unsigned char **bitmat)
//	{
//		unsigned char *rowptr;
//		unsigned int rowsize = 0;
//		unsigned int i;
//
//		for (i = 0; i < gnum_subs; i++) {
//			if (bitmat[i] == NULL)
//				continue;
//
//			rowptr = bitmat[i] + ROW_SIZE_BYTES;
//			memcpy(&rowsize, bitmat[i], ROW_SIZE_BYTES);
//
//			cumulative_dgap(rowptr, rowsize, rowptr);
//		}
//	}
	
	
}
