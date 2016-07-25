package myoldtests;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import server.ServerConfig;
import localIOUtils.IOUtils;
import data.compressor.SOLongPair;
import data.compressorUtils.DBImpl;
import data.compressorUtils.InRamDBUtils;
import data.compressorUtils.RedisUtils;
import data.manager.DataManager;

public class testWithoutThread {
	
	public static void main(String[] args) throws Exception {
		//test1();
		test2();
	}
	
	public static void test1() throws Exception{
		DBImpl dbu = null;
		try{
			dbu = new RedisUtils();
			dbu.addSO(new SOLongPair((long)1,(long)1));
			dbu.fetchSOSize();
			dbu.insertNode("ABC");
			
			IOUtils.setLogging(true, false);
			String currentPath = System.getProperty("user.dir");
			String psPath = currentPath + File.separator + ".." + File.separator
					+ "CtmRdf" + File.separator + "_psFake";
			File folder = new File(psPath);
			ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
			String compressedPath = currentPath + File.separator + ".." + File.separator
					+ "CtmRdf" + File.separator + "_compressedFake";
			DataManager dm = new DataManager("TEST");
			dm.indexedCompress(listOfFiles, compressedPath, dbu, "");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dbu.closeAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void test2() throws Exception{
		ServerConfig myConfig = ServerConfig.getInstance();
		myConfig._blockLineNb = 1000;
		myConfig._blockLineLength = 32768;
		IOUtils.setLogging(true, false);
		String currentPath = System.getProperty("user.dir");
		String psPath = currentPath + File.separator + ".." + File.separator
		+ "CtmRdf" + File.separator + "_ps";
		File folder = new File(psPath);
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		String compressedPath = currentPath + File.separator + ".." + File.separator
				+ "CtmRdf" + File.separator + "_compressed";
		DBImpl dbu = new InRamDBUtils();
		DataManager dm = new DataManager("TEST");
		dm.indexedCompress(listOfFiles, compressedPath, dbu, "");
	}

}
