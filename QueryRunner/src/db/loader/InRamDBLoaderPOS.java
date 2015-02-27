package db.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import constants.AppConstants;
import data.cleaner.RDFPairStr;
import data.reader.PairReader;
import db.utils.DBImpl;
import db.utils.InRamDBUtilsPOS;
import localIOUtils.IOUtils;

/**
 * Only for the connection using POS files.
 * @author CMWT420
 *
 */
public class InRamDBLoaderPOS implements DBLoaderImpl {

	@Override
	public HashMap<String, DBImpl> getDBList(String path, MODE mode) throws Exception {
		HashMap<String, DBImpl> resultList = new HashMap<String, DBImpl>();
		File folder = new File(path);
		if(!folder.canRead() && !folder.isDirectory()) return null;
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		switch(mode){
			case POS : 
				//ConcurrentHashMap Collections
				for(File f : listOfFiles){
					if(f.getName().startsWith(AppConstants.rdfTypeHeader)) {
						try {
							PairReader reader = new PairReader(f.getAbsolutePath());
							String str = null;
							DBImpl dbu = new InRamDBUtilsPOS();
							while ((str = reader.nextLine()) != null) {
								dbu.put(str, "");
							}
							resultList.put(f.getName(), dbu);
							IOUtils.logLog("File(T) " + f.getName() 
									+ " charged. Current size of table : " 
									+ dbu.fetchLoadedSize());
						} catch (IOException e) {
							e.printStackTrace();
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} finally {
						}
					} else {
						try {
							PairReader reader = new PairReader(f.getAbsolutePath());
							RDFPairStr pair = null;
							InRamDBUtilsPOS dbu = new InRamDBUtilsPOS();
							while ((pair = reader.nextStr()) != null) {
								dbu.put(pair.getSubject(), 
										pair.getObject());
							}
							resultList.put(f.getName(), dbu);
							IOUtils.logLog("File(NT) " + f.getName() 
									+ " charged. Current size of table : " 
									+ dbu.fetchLoadedSize());
						} catch (IOException e) {
							e.printStackTrace();
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} finally {
						}
					}
				}
				break;
			case COMPRESSED : 
				break;
			default:
				break;
		}
		return resultList;
	}

}
