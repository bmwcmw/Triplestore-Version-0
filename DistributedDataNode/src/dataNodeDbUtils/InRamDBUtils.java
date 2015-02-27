package dataNodeDbUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import queryObjects.BiList;
import localIOUtils.IOUtils;
import metaLoader.MetaInfoTriple;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import constants.CTMConstants;
import data.cleaner.RDFPairStr;
import data.compressor.SOLongPair;
import data.reader.PairReader;

/**
 * 
 * @author CMWT420
 *
 */
public class InRamDBUtils implements DBImpl{
	private HashMap<String, BiList<Integer, MetaInfoTriple>> metaList;
	private BiMap<Long, String> nodes;
	private ArrayList<SOLongPair> soList = new ArrayList<SOLongPair>();
	
	public InRamDBUtils(){
		cleanAll();
	}
	
	@Override
	public void cleanAll(){
		nodes = HashBiMap.create();
		soList = new ArrayList<SOLongPair>();
		metaList = new HashMap<String, BiList<Integer, MetaInfoTriple>>();
	}

	@Override
	public void closeAll() {}
	
	
	
	/* * * * * * *
	 * * Index * *
	 * * * * * * */
	@Override
    public Long fetchIndexSize(){
    	return new Long(nodes.size());
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
		IOUtils.logLog("File charged. Current size of key-value pair(s) : " + fetchSOSize());
	}
	
	
	
	/* * * * * * *
	 * * Matrix  *
	 * * * * * * */
	@Override
	public Long fetchSOSize(){
		return new Long(soList.size());
	}

	@Override
	public void loadMatrixFromFile(String path) throws IOException {
		// TODO Load matrix files
	}
	
	
	
	/* * * * * * *
	 * Metadata  *
	 * * * * * * */
	/**
	 * {@inheritDoc}
	 * <p>Remember : in each line of metadata file,
	 * 1)Number of file, 2)First occured S/O's ID, 3)Offset(ID) and 4)offset(line number).</p>
	 * <p>Here we have, for each predicate, zero to two BTrees (SO and/or OS or neither), containing
	 * which ID is in which file block. What's more, since an S or O line can be cut due to its 
	 * length, the O/S offsets of these lines are also noted, so that : 
	 * <br>
	 * - for the SPARQL queries with variable(s)(?x pred ?y), each time we can load a whole S or O 
	 * line from SO or OS matrix
	 * <br>
	 * - for the SPARQL queries without variable(X pred Y), 
	 * </p>
	 */
	@Override
	public void loadMetaFromFile(String folderPath) throws IOException, ParseException {
		IOUtils.logLog("Loading " + folderPath);
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		BufferedReader reader = null;
		if (listOfFiles != null) {
			for (File f : listOfFiles) {
				if(f.isFile() && 
						IOUtils.getExtension(f.getName()).equals(CTMConstants.MetadataExt)){
					//System.out.println(f.getName());
					String elementName = IOUtils.filenameWithoutExt(f.getName());
					IOUtils.logLog(elementName);
					if(!metaList.containsKey(elementName)){
						metaList.put(elementName, new BiList<Integer, MetaInfoTriple>());
					}
					BiList<Integer, MetaInfoTriple> b = metaList.get(elementName);
					reader = new BufferedReader(new FileReader(f));
					String line;
					int lineNb = 0;
					while ((line = reader.readLine()) != null) {
						StringTokenizer itr = new StringTokenizer(line);
						//At least 4 tokens
						if (!itr.hasMoreTokens()) {
							if (reader != null) reader.close();
							throw new ParseException(f.getPath(), lineNb);
						}
						String fIdStr = itr.nextToken();
						int fId = Integer.valueOf(fIdStr);
						if (!itr.hasMoreTokens()) {
							if (reader != null) reader.close();
							throw new ParseException(f.getPath(), lineNb);
						}
						String soIdStr = itr.nextToken();
						long soId = Long.valueOf(soIdStr);
						if (!itr.hasMoreTokens()) {
							if (reader != null) reader.close();
							throw new ParseException(f.getPath(), lineNb);
						}
						String soOffsetIDStr = itr.nextToken();
						long soOffsetID = Long.valueOf(soOffsetIDStr);
						if (!itr.hasMoreTokens()) {
							if (reader != null) reader.close();
							throw new ParseException(f.getPath(), lineNb);
						}
						String soOffsetLineStr = itr.nextToken();
						int soOffsetLine = Integer.valueOf(soOffsetLineStr);
						IOUtils.logLog("File " + elementName + "." + fId + " begins from ID " + soId 
								+ ", offset " + soOffsetID + " = line " + soOffsetLine 
								+ " of current ID");
						b.add(fId, new MetaInfoTriple(soId, soOffsetID, soOffsetLine));
						lineNb++;
					}
					if (reader != null) reader.close();
				}
			}
		} else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
			throw new IOException("Nothing in the folder.");
		}
	}

	@Override
	public int fetchLoadedMetaSize() {
		return metaList.size();
	}

	@Override
	public Entry<String, Integer> getFileLineNumber(String predName, String matName, Long lineId, 
			Long colId) {
		if(metaList == null){
			IOUtils.logLog("Metalist is NULL. This shouldn't happen.");
			System.exit(-1);
		} else {
			if(metaList.size()>0){
				BiList<Integer, MetaInfoTriple> b = metaList.get(predName + "." +matName);
				if(b != null){
					//TODO
				} else {
					IOUtils.logLog("Couldn't find data for " + predName + "." +matName);
				}
			} else {
				IOUtils.logLog("Metalist is empty.");
			}
		}
		return null;
	}
}
