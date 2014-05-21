package ctmRdf;

import indexNodesDBUtils.DBUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.simple.JSONArray;

import localIOUtils.IOUtils;
import dataManagement.DataManager;

public class CTMThread implements Runnable {
    private String threadId;
    private int taskId;
    private Thread t;
    private DataManager dm;
    private ArrayList<File> inputFiles;
    private String outputFolder;
    private String invalidPath;
    private String nsPath;
    private DBUtils indexNodes;
    private JSONArray connoption;

    public CTMThread(String tid, int task, ArrayList<File> inputList, String outputPath,
    		String invalid, String ns, DBUtils dbu){
    	taskId = task;
		threadId = tid;
		dm = new DataManager(tid);
		inputFiles = inputList;
		outputFolder = outputPath;
		IOUtils.checkOrCreateFolder(outputFolder);
		invalidPath = invalid;
		if(invalid!=null)
			IOUtils.checkOrCreateFolder(invalidPath);
		nsPath = ns;
		if(ns!=null)
			IOUtils.checkOrCreateFolder(nsPath);
		indexNodes = dbu;
		System.out.println("Creating " + threadId);
    }
    
    /**
     * Only for distributor tasks
     * @param tid
     * @param inputList
     * @param connopt
     */
    public CTMThread(String tid, int task, ArrayList<File> inputList, JSONArray connopt){
		threadId = tid;
		dm = new DataManager(tid);
		inputFiles = inputList;
    	connoption = connopt;
		System.out.println("Creating " + threadId);
    }

    @Override
    public void run() {
		long startTime, endTime, duration;
		startTime = System.currentTimeMillis();
		System.out.println("Running " + threadId);
		try {
			switch (taskId) {
				case CTMConstants.CTMCONVERTER:
					//TODO
					break;
				case CTMConstants.CTMREADERPS:
					dm.psSplit(inputFiles, outputFolder, nsPath, invalidPath);
					break;
				case CTMConstants.CTMREADERPOS:
					dm.posSplit(inputFiles, outputFolder);
					break;
				case CTMConstants.CTMCOMPRESS:
					dm.indexedCompress(inputFiles, outputFolder, indexNodes);
					break;
				case CTMConstants.CTMCOMPARE:
					dm.prepareCompare(inputFiles, outputFolder);
					break;
				case CTMConstants.CTMDISTRIBUTE:
					dm.distribute(inputFiles, connoption);
					break;
				default:
					break;
			}
			dm.closeAllWriters();
		} catch (IOException e) {
			IOUtils.logLog("Error in thread " + threadId + " : " + e.getMessage());
		} catch (ParseException e) {
			IOUtils.logLog("Error in thread " + threadId + " : " + e.getMessage());
		} catch (SQLException e) {
			IOUtils.logLog("Error in thread " + threadId + " : " + e.getMessage());
		}
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		System.out.println("Thread " + threadId + " exiting : Task " + taskId 
				+ " finished in " + String.valueOf(duration) + "ms");
    }

    public void start() {
		System.out.println("Starting " + threadId);
		if (t == null) {
		    t = new Thread(this, threadId);
		    t.start();
		}
    }

}
