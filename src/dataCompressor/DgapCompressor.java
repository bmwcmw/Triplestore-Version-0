package dataCompressor;

import indexNodesDBUtils.DBImpl;

import java.io.File;
import localIOUtils.IOUtils;


/**
 * <p>Data compressor using D-gap</p>
 * <p>Calls the BitMat compressor of different DB units</p>
 * 
 * @author Cedar
 *
 */
public class DgapCompressor {
	
	public static void writeCompressedFormat(String inFileName, String outputPath, 
			DBImpl dbu, String comparePath) throws Exception{
		Long indexSize = dbu.fetchIndexSize();
		Long soSize = dbu.fetchSOSize();
		IOUtils.logLog("Start Compressing into matrix");
		IOUtils.logLog("All nodes : " + indexSize + " (S,O) pairs : "
				+ soSize);
    	String outputFilePath = outputPath + File.separator + inFileName;
		
    	/* Write matrices */
		dbu.writeMatS(outputFilePath, comparePath, inFileName);
		dbu.writeMatO(outputFilePath, comparePath, inFileName);
		
		/* Write index */
		dbu.writeIndex(outputFilePath + ".index");
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
