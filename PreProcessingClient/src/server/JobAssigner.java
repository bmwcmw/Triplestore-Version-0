package server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import localIOUtils.IOUtils;
import dataComparator.FilePair;

/**
 * 
 * @author CMWT420
 *
 */
public class JobAssigner {
	
	private static ServerConfig myConfig;

	/**
	 * <p>Split all source files into specified number of groups, then for example 
	 * assign them to multiple threads</p>
	 * <p>For PS, POS, Compress, Pre-Compare</p>
	 * @param allFiles all source files
	 * @param averageSize false to assign tasks only according to number of files(faster),
	 * true if you want to have more average size between each group of files(slower)
	 * 
	 * @return List of grouped files (sub-lists)
	 */
	public static ArrayList<ArrayList<File>> assignJobs(ArrayList<File> allFiles
			, boolean averageSize){
		myConfig = ServerConfig.getInstance();
		ArrayList<ArrayList<File>> outputLists = new ArrayList<ArrayList<File>>();
		if(!averageSize){ //Random plan only according to the number of files
			//Distribute all input files to threads, as average as possible
			int partitionSize = allFiles.size()/myConfig._nbThreads;
			int remainder = allFiles.size()%myConfig._nbThreads;
			int i = 0;
			if(remainder > 0){
				partitionSize++;
				while (remainder > 0) {
					System.out.println("Adding "+Math.min(partitionSize, 
							allFiles.size() - i));
					outputLists.add(new ArrayList<File>(allFiles.subList(i,
							i + Math.min(partitionSize, allFiles.size() - i))));
					i += partitionSize;
					remainder--;
				}
				partitionSize--;
			}
			while (i < allFiles.size()) {
				System.out.println("Adding "+Math.min(partitionSize, allFiles.size() - i));
				outputLists.add(new ArrayList<File>(allFiles.subList(i,
			            i + Math.min(partitionSize, allFiles.size() - i))));
				i += partitionSize;
			}
			//Show result
			for (i = 0; i < outputLists.size(); i++) {
				IOUtils.logLog("Sub task " + i + " with " + 
						outputLists.get(i).size() + "file(s)");
	//			for (int j = 0; j < inputLists.get(i).size(); j++) {
	//				IOUtils.logLog(inputLists.get(i).get(j).getName());
	//			}
			}
		}
		else { //To approximately equal-sized blocks
			/* Find the target group size - the sum of all sizes divided by n. */
			double sizeAll = 0;
			for(File f : allFiles){
				sizeAll += f.length();
			}
			double expected = sizeAll / myConfig._nbThreads;
			
			/* Sort the files decreasing in size. */
			Collections.sort(allFiles, new Comparator<File>() {
				@Override
				public int compare(final File f1, final File f2) {
					return Long.valueOf(f1.length()).compareTo(f2.length());
				}
	        });
			//TODO TEST
			/* Creates groups */
			double tempsize = expected;
			int currentGroup = 0;
			while(allFiles.size() > 0 && currentGroup < myConfig._nbThreads){
				/* for each group, while the remaining space in your group is 
				 * bigger than the first element of the list take the first element 
				 * of the list and move it to the group
				 */
				for(int i=0;i<allFiles.size();i++){
					/* for each element, find the element for which the difference 
					 * between group size and target group size is minimal move this 
					 * element to the group(decreasing size loop)
				     */
					File f = allFiles.get(i);
					if(tempsize > f.length()){
						outputLists.get(currentGroup).add(f);
						tempsize = tempsize - f.length();
						allFiles.remove(i);
						i--;
					}
					currentGroup++;
				}
			}
			/* This shouldn't happen */
			if((currentGroup == myConfig._nbThreads) && (allFiles.size()>0)){
				IOUtils.logLog("Error : ???");
				return null;
			}
			    

		}
		IOUtils.logLog("Sub tasks assigned");
		return outputLists;
	}
	
	/**
	 * <p>Split all source files into specified number of groups, then for example 
	 * assign them to multiple threads</p>
	 * <p>For Compare</p>
	 * @param allFiles all source files
	 * @param averageSize false to assign tasks only according to number of files(faster),
	 * true if you want to have more average size between each group of files(slower)
	 * @return
	 */
	public static LinkedList<LinkedList<FilePair>> assignJobs(LinkedList<FilePair> allPairs, 
			boolean averageSize){
		myConfig = ServerConfig.getInstance();
		LinkedList<LinkedList<FilePair>> inputLists = new LinkedList<LinkedList<FilePair>>();
		if(!averageSize){ //Random plan only according to the number of files
			//Distribute all input files to threads, as average as possible
			int partitionSize = allPairs.size()/myConfig._nbThreads;
			int remainder = allPairs.size()%myConfig._nbThreads;
			int i = 0;
			if(remainder > 0){
				partitionSize++;
				while (remainder > 0) {
					System.out.println("Adding "+Math.min(partitionSize, allPairs.size() - i));
					inputLists.addFirst(new LinkedList<FilePair>(allPairs.subList(i,
							i + Math.min(partitionSize, allPairs.size() - i))));
					i += partitionSize;
					remainder--;
				}
				partitionSize--;
			}
			while (i < allPairs.size()) {
				System.out.println("Adding "+Math.min(partitionSize, allPairs.size() - i));
				inputLists.addFirst(new LinkedList<FilePair>(allPairs.subList(i,
			            i + Math.min(partitionSize, allPairs.size() - i))));
				i += partitionSize;
			}
			//Show result
			for (i = 0; i < inputLists.size(); i++) {
				IOUtils.logLog("Sub task " + i + " with " + 
						inputLists.get(i).size() + "file(s)");
	//			for (int j = 0; j < inputLists.get(i).size(); j++) {
	//				IOUtils.logLog(inputLists.get(i).get(j).getName());
	//			}
			}
		}
		else { //To approximately equal-sized blocks
			//TODO Once the other tested, follow the method
		}
		IOUtils.logLog("Sub tasks assigned");
		return inputLists;
	}
	
}
