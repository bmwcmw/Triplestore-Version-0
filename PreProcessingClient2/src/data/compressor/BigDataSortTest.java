package data.compressor;

import java.io.IOException;

import data.compressor.BigDataSort;

public class BigDataSortTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("BEGIN");
		BigDataSort bds = new BigDataSort();
		
		long begin, end;
		begin = System.currentTimeMillis();
		bds.createBigsortNums();
		end = System.currentTimeMillis();
		System.out.println("CREATION " + (end - begin));

		begin = System.currentTimeMillis();
		bds.beSmallFileAndSort();
		end = System.currentTimeMillis();
		System.out.println("SMALL AND SORT " + (end - begin));

		begin = System.currentTimeMillis();
		bds.unitFileToSort();
		end = System.currentTimeMillis();
		System.out.println("MERGE IN ORDER " + (end - begin));
	}

}
