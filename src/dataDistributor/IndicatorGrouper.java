package dataDistributor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import localIOUtils.IOUtils;

import org.json.simple.parser.ParseException;

import ctmRdf.CTMConstants;
import ctmRdf.CTMServer;
import dataCleaner.CTMPairLong;

public class IndicatorGrouper {
	

	/**
	 * Tries to load predicates' similarities from a predefined file, 
	 * then to create predicate groups.
	 * Returns null if the file doesn't exist or is unreadable.
	 * 
	 * @return Grouped predicates
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static ArrayList<ArrayList<File>> groupBySimilarities(String compressedPath, 
			String indicatorPath, HashMap<String,HashSet<String>> noRepNames,
			boolean forceRandom, int _indicatorMode, int _nbThreads) throws Exception{
		ArrayList<File> allPredFiles = IOUtils.loadFolder(compressedPath);
//		HashSet<String> predicateFilenames = new HashSet<String>();
//		for(int i=0;i<allPredFiles.size();i++){
//			predicateFilenames.add(IOUtils.filenameWithoutExt(allPredFiles.get(i).getName()));
//		}
		// USE random plan to group files of varying sizes into approximately EQUAL SIZED blocks 
		if(forceRandom) {
			IOUtils.logLog("Forced to use random plan...");
			ArrayList<ArrayList<File>> groups = CTMServer.assignJobs(allPredFiles, true);
			return groups;
		}
		// USE indicators
		else {
			IOUtils.logLog("Try to group by similarity");
			/* Calculate size of each group */
			int sizeOfGroup = noRepNames.size()/_nbThreads;
			if (noRepNames.size()%_nbThreads > 0) sizeOfGroup++;
			IOUtils.logLog(_nbThreads+"group(s), each group will have " 
					+ sizeOfGroup +" or "+(sizeOfGroup-1)+" predicate(s)");
			/* Get all indicator files */
			ArrayList<File> allIndFiles = IOUtils.loadFolder(compressedPath);
			if(allIndFiles != null){
				IOUtils.logLog("Found indicators, try to group by indicator");
				/* Create structure <PredA, HashMap<PredB, Common(S,O)> */
				HashMap<String, HashMap<String, CTMPairLong>> indicators 
						= new HashMap<String, HashMap<String, CTMPairLong>>();
				BufferedReader reader = null;
				/* Load all indicators from files */
				for(File f : allIndFiles){
					try {
						reader = new BufferedReader(new FileReader(f));
						String s = reader.readLine();
						if(s==null) throw new ParseException(0);
						StringTokenizer nizer = new StringTokenizer(s);
						if (!nizer.hasMoreTokens()) throw new ParseException(1);
						String pred1name = nizer.nextToken();
						if (!nizer.hasMoreTokens()) throw new ParseException(2);
						String pred2name = nizer.nextToken();
						if (!nizer.hasMoreTokens()) throw new ParseException(3);
						Long Snumber = Long.valueOf(nizer.nextToken());
						if (!nizer.hasMoreTokens()) throw new ParseException(4);
						Long Onumber = Long.valueOf(nizer.nextToken());
						/* Add to predicate 1's list */
						HashMap<String, CTMPairLong> toAdd = indicators.get(pred1name);
						if (toAdd == null){
							toAdd = new HashMap<String, CTMPairLong>();
							toAdd.put(pred2name, new CTMPairLong(Snumber, Onumber));
							indicators.put(pred1name, toAdd);
						} else {
							if(toAdd.get(pred2name) != null) // This shouldn't happen
								throw new Exception("Error : repeated entry for "
										+ pred1name + " " + pred2name + " in file " 
										+ f.getName());
							toAdd.put(pred2name, new CTMPairLong(Snumber, Onumber));
						}

						/* Add to predicate 2's list */
						toAdd = indicators.get(pred2name);
						if (toAdd == null){
							toAdd = new HashMap<String, CTMPairLong>();
							toAdd.put(pred1name, new CTMPairLong(Snumber, Onumber));
							indicators.put(pred2name, toAdd);
						} else {
							if(toAdd.get(pred1name) != null) // This shouldn't happen
								throw new Exception("Error : repeated entry for "
										+ pred2name + " " + pred1name + " in file " 
										+ f.getName());
							toAdd.put(pred1name, new CTMPairLong(Snumber, Onumber));
						}
					} catch (Exception e) {
						IOUtils.logLog("Parse error?");
						e.printStackTrace();
						throw e;
					} finally {
						if (reader != null) {
							reader.close();
						}
					}
				}
				/* DEBUG display */
	        	for (Entry<String, HashMap<String, CTMPairLong>> entry : indicators.entrySet()) {
	        		System.out.println("Indicators loaded. Key: " + entry.getKey() 
	        				+ ". Value: " + entry.getValue());
	       		}
				/* Calculate the sum of common subject and/or object */
				/* And sort the indicators */
				Map<Long, String> predsWithInd = new HashMap<Long, String>();
        		HashMap<String, TreeMap<String, CTMPairLong>> sortedIndicators 
        				= new HashMap<String, TreeMap<String, CTMPairLong>>();
				switch (_indicatorMode){
					case CTMConstants.CTMINDICATORS :
						for (Entry<String, HashMap<String, CTMPairLong>> e : indicators.entrySet()) {
							long temp = 0;
							for(Entry<String, CTMPairLong> e2 : e.getValue().entrySet()) {
								temp = temp + e2.getValue().getSubject();
							}
							MapValuePairLongSComparator rator = 
									new MapValuePairLongSComparator(e.getValue());
							TreeMap<String,CTMPairLong> sorted = 
									new TreeMap<String,CTMPairLong>(rator);
							sorted.putAll(e.getValue());
							sortedIndicators.put(e.getKey(), sorted);
							predsWithInd.put(temp, e.getKey());
						} 
						break;
					case CTMConstants.CTMINDICATORO : 
						for (Entry<String, HashMap<String, CTMPairLong>> e : indicators.entrySet()) {
							long temp = 0;
							for(Entry<String, CTMPairLong> e2 : e.getValue().entrySet()) {
								temp = temp + e2.getValue().getObject();
							}
							MapValuePairLongOComparator rator = 
									new MapValuePairLongOComparator(e.getValue());
							TreeMap<String,CTMPairLong> sorted = 
									new TreeMap<String,CTMPairLong>(rator);
							sorted.putAll(e.getValue());
							sortedIndicators.put(e.getKey(), sorted);
							predsWithInd.put(temp, e.getKey());
						}
						break;
					case CTMConstants.CTMINDICATORSO : 
						for (Entry<String, HashMap<String, CTMPairLong>> e : indicators.entrySet()) {
							long temp = 0;
							for(Entry<String, CTMPairLong> e2 : e.getValue().entrySet()) {
								temp = temp + e2.getValue().getSubject() + e2.getValue().getObject();
							}
							MapValuePairLongSOComparator rator = 
									new MapValuePairLongSOComparator(e.getValue());
							TreeMap<String,CTMPairLong> sorted = 
									new TreeMap<String,CTMPairLong>(rator);
							sorted.putAll(e.getValue());
							sortedIndicators.put(e.getKey(), sorted);
							predsWithInd.put(temp, e.getKey());
						}
						break;
					default : //This shouldn't happen
						return groupBySimilarities(indicatorPath, compressedPath,
								noRepNames, true, _indicatorMode, _nbThreads);
				}
				/* Sort predicates by the sum of common subject and/or object (descending order) */
				//MapValueComparator bvc =  new MapValueComparator(predsWithInd);
		        //TreeMap<String,Long> sortedPreds = new TreeMap<String,Long>(bvc);
		        //sortedPreds.putAll(predsWithInd);
				TreeMap<Long,String> sortedPreds = new TreeMap<Long,String>(Collections.reverseOrder());
				sortedPreds.putAll(predsWithInd);
				
				/* DEBUG display */
        		for (Entry<Long,String> entry : sortedPreds.entrySet()) {
        		     System.out.println("Sorted Key: " + entry.getKey() 
        		    		 + "(total common). Value: " + entry.getValue()+"(predicate)");
        		}
	        	for (Entry<String, TreeMap<String, CTMPairLong>> entry : sortedIndicators.entrySet()) {
	        		System.out.println("Indicators sorted. Key: " + entry.getKey() 
	        				+ ". Value: " + entry.getValue());
	       		}
		        /* Group predicates */
	        	ArrayList<ArrayList<File>> groups = new ArrayList<ArrayList<File>>();
	        	//int nbAllPreds = predsWithInd.keySet().size();
	        	HashSet<String> selectedPreds = new HashSet<String>();
	        	int groupN = 0;//Nth group of predicate(s)
	        	//Assign predicates from those having highest value indicator
	        	for (Entry<String, TreeMap<String, CTMPairLong>> entry : sortedIndicators.entrySet()) {
	        		if(selectedPreds.contains(entry.getKey())) continue;
	        		for (Entry<String, CTMPairLong> entry2 : entry.getValue().entrySet()) {
		        		if(!selectedPreds.contains(entry2.getKey())){
		        			if(groups.get(groupN)==null)
		        					groups.add(groupN, new ArrayList<File>());
		        			groups.get(groupN).add(new File(compressedPath + 
		        					File.separator + entry2.getKey() + "index"));
		        			groups.get(groupN).add(new File(compressedPath + 
		        					File.separator + entry2.getKey() + "matrixSO"));
		        			groups.get(groupN).add(new File(compressedPath + 
		        					File.separator + entry2.getKey() + "matrixOS"));
		        			selectedPreds.add(entry2.getKey());
		        		}
	        		}        	
		        	//TODO
	        		//Add sizeOfGroup or sizeOfGroup-1 entries (not yet selected) to group N
	        	}
	        	//_nbThreads	
	        	
//		        int currentGroup=0;
//		        HashSet<String> predsToFill = new HashSet<String>();
//		        while(sortedPreds.size()>predsToFill.size() && currentGroup<CTMServer._nbThreads){
//		        	groups.add(currentGroup, new ArrayList<File>());
//		        	//Check if Comparator descending or not
//		        	//Get first(best) element of sortedPreds to begin the group N
//		        	String pred0 = null;
////		        	predsToFill.add(pred0);
//		        	for(Entry<Long,String> entry : sortedPreds.entrySet()){
//		        		if(!predsToFill.contains(entry.getValue())){
//		        			System.out.println(entry.getValue());
//		        			pred0 = entry.getValue();
//		        			break;
//		        		}
//		        	}
//		        	IOUtils.logLog("Group " + currentGroup + " : beginning from " + pred0);
//		        	HashMap<String, CTMPairLong> relatedPreds = indicators.get(pred0);
//	        		/* Add it(index, matrix S, matrix O) to group N and remove it from temporary set */
//	        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
//	        				+ pred0 + ".index"));
//	        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
//	        				+ pred0 + ".matrixS"));
//	        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
//	        				+ pred0 + ".matrixO"));
//	        		predsToFill.add(pred0);
//	        		IOUtils.logLog(predsToFill.size() + " predicate(s) assigned");
//	        		
//		        	//Get first N elements related, put them in groups(n)
//		        	//And remove these first N elements from sortedPreds
//		        	int i=0;
//		        	long maxValue=0;
//		        	String maxPred="";
//		        	while(i < sizeOfGroup){
//		        		/* Find nearest predicate */
//		        		switch (CTMServer._indicatorMode){
//							case CTMConstants.CTMINDICATORS :
//				        		for(Entry<String, CTMPairLong> e : relatedPreds.entrySet()){
//				        			if(e.getValue().getSubject() >= maxValue 
//				        					&& !predsToFill.contains(e.getKey())){
//				        				maxValue = e.getValue().getSubject();
//				        				maxPred = e.getKey();
//				        			}
//				        		}
//				        		break;
//							case CTMConstants.CTMINDICATORO : 
//								for(Entry<String, CTMPairLong> e : relatedPreds.entrySet()){
//				        			if(e.getValue().getObject() >= maxValue 
//				        					&& !predsToFill.contains(e.getKey())){
//				        				maxValue = e.getValue().getObject();
//				        				maxPred = e.getKey();
//				        			}
//				        		}
//				        		break;
//							case CTMConstants.CTMINDICATORSO : 
//								for(Entry<String, CTMPairLong> e : relatedPreds.entrySet()){
//				        			if((e.getValue().getSubject()+e.getValue().getObject()) >= maxValue 
//				        					&& !predsToFill.contains(e.getKey())){
//				        				maxValue = e.getValue().getSubject() + e.getValue().getObject();
//				        				maxPred = e.getKey();
//				        			}
//				        		}
//				        		break;
//							default : //This shouldn't happen
//								return groupBySimilarities(indicatorPath, compressedPath, noRepNames, true);
//						}
//		        		/* Add it(index, matrix S, matrix O) to group N and remove it from temporary sets */
//			        	IOUtils.logLog("Add "+maxPred+" to group "+currentGroup
//			        			+" having "+maxValue+" common entries with "+pred0);
//		        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
//		        				+ maxPred + ".index"));
//		        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
//		        				+ maxPred + ".matrixS"));
//		        		groups.get(currentGroup).add(new File(compressedPath + File.pathSeparator 
//		        				+ maxPred + ".matrixO"));
//		        		predsToFill.add(maxPred);
//		        		IOUtils.logLog(predsToFill.size() + " predicate(s) assigned");
//		        		i++;
//		        	}
//		        	currentGroup++;
//		        }
				return groups;
			} else {
				return groupBySimilarities(indicatorPath, compressedPath
						, noRepNames, true, _indicatorMode, _nbThreads);
			}
		}
	}
	
//	/**
//	 * Return first N entries of a sorted map
//	 * @param max : N
//	 * @param source : source map
//	 * @return A sorted map
//	 */
//	private static TreeMap<String, CTMPairLong> getFirstEntries(int max, 
//			TreeMap<String, CTMPairLong> source) {
//		int count = 0;
//		TreeMap<String, CTMPairLong> target = new TreeMap<String, CTMPairLong>();
//		for (Entry<String, CTMPairLong> entry : source.entrySet()) {
//			if (count >= max)
//				break;
//
//			target.put(entry.getKey(), entry.getValue());
//			count++;
//		}
//		return target;
//	}
	
	/**
	 * Return from M to N entries of a sorted map
	 * @param min : M
	 * @param max : N
	 * @param source : source map
	 * @return An ArrayList
	 */
	private static ArrayList<String> getFirstEntries(int min, int max, 
			TreeMap<String, CTMPairLong> source) {
		int count = 0;
		ArrayList<String> target = new ArrayList<String>();
		for (Entry<String, CTMPairLong> entry : source.entrySet()) {
			if (count < min)
				continue;
			if (count >= max)
				break;

			target.add(entry.getKey());
			count++;
		}
		return target;
	}
}
