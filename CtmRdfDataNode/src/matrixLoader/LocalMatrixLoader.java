package matrixLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import ctmRdf.CTMConstants;
import localIOUtils.IOUtils;
import metaLoader.MetaInfoTriple;
import metaLoader.LocalMetaLoader.MODE;

/**
 * Loads numerical data from matrix files.
 * @author CEDAR
 */
public class LocalMatrixLoader {
	
	public static boolean checkAPoint(TreeMap<Long, MetaInfoTriple> tMap, String predName, MODE mode, long line, long col) throws IOException{
		
		String _dir = System.getProperty("user.dir") + File.separator + ".." + File.separator
				+ "CtmDataSet" + File.separator + predName;
		File dir = new File(_dir);
		boolean result = false;
		
		File sf = null;
		String myFileExt = null;
		String tempFileExt = null;
		String lineContent = null;
		BufferedReader reader = null;
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(dir.listFiles()));
		
		
		for(long id: tMap.keySet()){
            System.out.println(id  +" :: "+ tMap.get(id));
            
            if(line < id){
				myFileExt = tempFileExt;
				break;
			}else{
				tempFileExt = String.valueOf(tMap.get(id).id);
			}
        }
		
		System.out.println("FileId found in MetaData: " + tempFileExt);
		switch(mode){
			case S : 
				for(File f:listOfFiles){
					if(IOUtils.getExtension(IOUtils.filenameWithoutExt(f.getName())).equals(CTMConstants.SOMatrixExt)){
						if(IOUtils.getExtension(f.getName()).equals("." + myFileExt)){
							if(sf != null){
								System.out.println("MetadataSExt duplicata.");
								System.exit(-1);
							}
							sf = f;
						}
						
					}
				}
				reader = new BufferedReader(new FileReader(sf.getAbsolutePath()));
				break;
			case O : 
				for(File f:listOfFiles){
					if(IOUtils.getExtension(IOUtils.filenameWithoutExt(f.getName())).equals(CTMConstants.OSMatrixExt)){
						if(IOUtils.getExtension(f.getName()).equals("." + myFileExt)){
							if(sf != null){
								System.out.println("MetadataSExt duplicata.");
								System.exit(-1);
							}
							sf = f;
						}
						
					}
				}
				reader = new BufferedReader(new FileReader(sf.getAbsolutePath()));
				break;
			default : 
				return false;
		}
		while ((lineContent = reader.readLine()) != null) {
			//10005-1-0:[0]10005,1,230003
			String lineNumber = lineContent.substring(0,lineContent.indexOf("-"));
			if(Long.parseLong(lineNumber) == line){
				String begin = lineContent.substring(0, lineContent.indexOf("]"))
						.substring(lineContent.indexOf("[") + 1);
				boolean beginbool = Boolean.getBoolean(begin);
				String data = lineContent.substring(lineContent.indexOf("]") + 1);
				String[] arr = data.split(",");
				long total = 0;
				for(int i = 0; i < arr.length; i++){
					total = total + Long.parseLong(arr[i]);
					if(col <= total){
						result = beginbool;
						break;
					}else{
						beginbool = !beginbool;
					}
				}
			}
			
		}
		
		if(reader != null){
			reader.close();
		}
		return result;
	}
	
	public static String returnLine(TreeMap<Long, MetaInfoTriple> tMap, String predName, MODE mode, long line) throws IOException{
		String _dir = System.getProperty("user.dir") + File.separator + ".." + File.separator
				+ "CtmDataSet" + File.separator + predName;
		File dir = new File(_dir);
		
		File sf = null;
		String myFileExt = null;
		String tempFileExt = null;
		String lineReturned = null;
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(dir.listFiles()));
		
		
		for(long id: tMap.keySet()){
            //System.out.println(id  +" :: "+ tMap.get(id));
            if(line < id){
				myFileExt = tempFileExt;
				break;
			}else{
				tempFileExt = String.valueOf(tMap.get(id).id);
			}
        }
		
		System.out.println("FileId found in MetaData: " + tempFileExt);
		switch(mode){
			case S : 
				for(File f:listOfFiles){
					if(IOUtils.getExtension(IOUtils.filenameWithoutExt(f.getName())).equals(CTMConstants.SOMatrixExt)){
						if(IOUtils.getExtension(f.getName()).equals("." + myFileExt)){
							if(sf != null){
								System.out.println("MetadataSExt duplicata.");
								System.exit(-1);
							}
							sf = f;
						}
						
					}
				}
				break;
			case O : 
				for(File f:listOfFiles){
					if(IOUtils.getExtension(IOUtils.filenameWithoutExt(f.getName())).equals(CTMConstants.OSMatrixExt)){
						if(IOUtils.getExtension(f.getName()).equals("." + myFileExt)){
							if(sf != null){
								System.out.println("MetadataSExt duplicata.");
								System.exit(-1);
							}
							sf = f;
						}
						
					}
				}
				break;
			default : 
				return null;
		}
		lineReturned = MatrixLineReaderLocalFS.headOneLine(line - Long.parseLong(myFileExt) * 10000, sf);
		return lineReturned;
	}
}
