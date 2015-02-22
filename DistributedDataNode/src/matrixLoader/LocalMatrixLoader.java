package matrixLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

import queryObjects.BiList;
import ctmRdf.CTMConstants;
import localIOUtils.IOUtils;
import metaLoader.MetaInfoTriple;
import metaLoader.LocalMetaLoader.MODE;

/**
 * Loads numerical data from matrix files.
 * @author CEDAR
 */
public class LocalMatrixLoader {
	
	
	public static boolean checkAPoint(BiList<Long, MetaInfoTriple> bList, String predName, MODE mode, long line, long col) throws IOException{
		
		String _dir = System.getProperty("user.dir") + File.separator + ".." + File.separator
				+ "CtmDataSet" + File.separator + predName;
		File dir = new File(_dir);
		boolean result = false;
		
		File sf = null;
		String myFileExt = null;
		String tempFileExt = null;
		String lineContent = null;
		BufferedReader reader = null;
		long tempLineNumber = 0;
		long myFirstLineNumber = 0;
		long nextFirstLineNumber = 0;
		boolean fromHead = true;
		
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(dir.listFiles()));
		
		for(int i = 0; i < bList.size(); i++){
			if(line < bList.get(i).elem1()){
				myFileExt = tempFileExt;
				myFirstLineNumber = tempLineNumber;
				nextFirstLineNumber = bList.get(i).elem1();
				break;
			}else{
				tempFileExt = String.valueOf(bList.get(i).elem2().id);
				tempLineNumber = bList.get(i).elem1();
			}
		}

		if((line - myFirstLineNumber) > (nextFirstLineNumber - line)){
			fromHead = false;
		}
		
		//System.out.println("FileId found in MetaData: " + tempFileExt);
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
		Boolean beginBool = null;
		long total = 0;
		if(fromHead){
			firstLoop:
				while ((lineContent = reader.readLine()) != null) {
					//10005-1-0:[0]10005,1,230003
					String lineNumber = lineContent.substring(0, lineContent.indexOf("-"));
					if(lineNumber.length() != 0 && Long.parseLong(lineNumber) == line){
						String begin = lineContent.substring(0, lineContent.indexOf("]"))
								.substring(lineContent.indexOf("[") + 1);
						beginBool = Boolean.getBoolean(begin);
						String data = lineContent.substring(lineContent.indexOf("]") + 1);
						String[] arr = data.split(",");
						for(int i = 0; i < arr.length; i++){
							total = total + Long.parseLong(arr[i]);
							if(col <= total){
								result = beginBool;
								break firstLoop;
							}else{
								beginBool = !beginBool;
							}
						}
						while ((lineContent = reader.readLine()) != null) {
							lineNumber = lineContent.substring(0, lineContent.indexOf("-"));
							if(lineNumber.length() == 0){
								data = lineContent.substring(lineContent.indexOf("]") + 1);
								arr = data.split(",");
								
								for(int i = 0; i < arr.length; i++){
									total = total + Long.parseLong(arr[i]);
									if(col <= total){
										result = beginBool;
										break firstLoop;
									}else{
										beginBool = !beginBool;
									}
								}
							}else{
								break firstLoop;
							}
						}
					}
				}
			secondLoop:
				while(total < col){
					myFileExt = "" + (Integer.parseInt(myFileExt) + 1);
					//System.out.println(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt);
					reader = new BufferedReader(new FileReader(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt));
					while ((lineContent = reader.readLine()) != null) {
						String lineNumber = lineContent.substring(0, lineContent.indexOf("-"));
						if(lineNumber.length() == 0){
							String data = lineContent.substring(lineContent.indexOf("]") + 1);
							String[] arr = data.split(",");
							
							for(int i = 0; i < arr.length; i++){
								total = total + Long.parseLong(arr[i]);
								if(col <= total){
									result = beginBool;
									break secondLoop;
								}else{
									beginBool = !beginBool;
								}
							}
						}else{
							break secondLoop;
						}
					}
				}
		}else{
			RandomAccessFile fileHandler = null;
		    fileHandler = new RandomAccessFile(sf, "r");
	        long fileLength = fileHandler.length() - 1;
	        long filePointer = 0;
	        int readByte = 0;
	        StringBuffer sb = new StringBuffer();
	        
			firstLoop:
	        for(filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            readByte = fileHandler.readByte();
	            if( readByte == 0xA || readByte == 0xD) {
	            	sb.setLength(0);
	                continue;
	            }else{
	            	if(( char )readByte == '-'){
	            		filePointer--;
	            		fileHandler.seek( filePointer );
	    	            readByte = fileHandler.readByte();
	    	            while(readByte != 0xA && readByte != 0xD && ( char )readByte != '-'){
	    	            	sb.append(( char )readByte);
	    	            	if(filePointer != 0){
	    	            		filePointer--;
	    	            	}else{
	    	            		break;
	    	            	}
		            		fileHandler.seek( filePointer );
		    	            readByte = fileHandler.readByte();
	    	            }
    	            	if(sb.reverse().toString().length() != 0){
    	            		if(Long.parseLong(sb.toString()) == line){
    	            			break firstLoop;
    	            		}
	    	            }
	    	            sb.setLength(0);
	    	            filePointer++;
	            	}
	            }
	        }
			
			sb.setLength(0);
			filePointer++;
			fileHandler.seek(filePointer);
            readByte = fileHandler.readByte();
            
            secondLoop:
	            while(filePointer <= fileLength){
	            	
	            	while(readByte != 0xA && readByte != 0xD){
	            		sb.append(( char )readByte);
	            		if(filePointer < fileLength){
	            			filePointer++;
	            			fileHandler.seek(filePointer);
	            			readByte = fileHandler.readByte();
	            		}else{
	            			break;
	            		}
	            		
	            	}
	            	
	            	lineContent = sb.toString();
	            	sb.setLength(0);
	            	//System.out.println(lineContent);
	            	String begin = lineContent.substring(0, lineContent.indexOf("]"))
							.substring(lineContent.indexOf("[") + 1);
	            	if(beginBool == null){
	            		beginBool = Boolean.getBoolean(begin);
	            	}
					
					String data = lineContent.substring(lineContent.indexOf("]") + 1);
					String[] arr = data.split(",");
					for(int i = 0; i < arr.length; i++){
						total = total + Long.parseLong(arr[i]);
						if(col <= total){
							result = beginBool;
							break secondLoop;
						}else{
							beginBool = !beginBool;
						}
					}
					if(filePointer < fileLength){
						filePointer++;	  
						fileHandler.seek(filePointer);
			            readByte = fileHandler.readByte();
					}else{
						break secondLoop;
					}
	            }
			//System.out.println(total);

            thirdLoop:
				while(total < col){
					myFileExt = "" + (Integer.parseInt(myFileExt) + 1);
					//System.out.println(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt);
					reader = new BufferedReader(new FileReader(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt));
					while ((lineContent = reader.readLine()) != null) {
						String lineNumber = lineContent.substring(0, lineContent.indexOf("-"));
						if(lineNumber.length() == 0){
							String data = lineContent.substring(lineContent.indexOf("]") + 1);
							String[] arr = data.split(",");
							
							for(int i = 0; i < arr.length; i++){
								total = total + Long.parseLong(arr[i]);
								if(col <= total){
									result = beginBool;
									break thirdLoop;
								}else{
									beginBool = !beginBool;
								}
							}
						}else{
							break thirdLoop;
						}
					}
				}
	        fileHandler.close();
		}
		
		if(reader != null){
			reader.close();
		}
		return result;
	}
	
	public static String returnLine(BiList<Long, MetaInfoTriple> bList, String predName, MODE mode, long line) throws IOException{
		String _dir = System.getProperty("user.dir") + File.separator + ".." + File.separator
				+ "CtmDataSet" + File.separator + predName;
		File dir = new File(_dir);
		
		File sf = null;
		String myFileExt = null;
		String tempFileExt = null;
		String lineContent = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		long tempLineNumber = 0;
		long myFirstLineNumber = 0;
		long nextFirstLineNumber = 0;
		boolean fromHead = true;
		ArrayList<File> listOfFiles = new ArrayList<File>(Arrays.asList(dir.listFiles()));
		
		for(int i = 0; i < bList.size(); i++){
			if(line < bList.get(i).elem1()){
				myFileExt = tempFileExt;
				myFirstLineNumber = tempLineNumber;
				nextFirstLineNumber = bList.get(i).elem1();
				break;
			}else{
				tempFileExt = String.valueOf(bList.get(i).elem2().id);
				tempLineNumber = bList.get(i).elem1();
			}
		}

		if((line - myFirstLineNumber) > (nextFirstLineNumber - line)){
			fromHead = false;
		}
		
		//System.out.println("FileId found in MetaData: " + tempFileExt);
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
				return null;
		}
		
		if(fromHead){			
			firstLoop:
				while ((lineContent = reader.readLine()) != null) {
					//10005-1-0:[0]10005,1,230003
					String lineNumber = lineContent.substring(0, lineContent.indexOf("-"));
					if(lineNumber.length() != 0 && Long.parseLong(lineNumber) == line){
						sb.append(lineContent);
						
						while ((lineContent = reader.readLine()) != null) {
							lineNumber = lineContent.substring(0, lineContent.indexOf("-"));
							if(lineNumber.length() == 0){
								sb.append("," + lineContent.substring(lineContent.indexOf("]") + 1, lineContent.length()));
							}else{
								break firstLoop;
							}
						}
					}
				}
			if((lineContent = reader.readLine()) == null){
				myFileExt = "" + (Integer.parseInt(myFileExt) + 1);
				//System.out.println(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt);
				reader = new BufferedReader(new FileReader(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt));
				while ((lineContent = reader.readLine()) != null) {
					String lineNumber = lineContent.substring(0, lineContent.indexOf("-"));
					//System.out.println(lineNumber);
					if(lineNumber.length() == 0){
						sb.append("," + lineContent.substring(lineContent.indexOf("]") + 1, lineContent.length()));
					}else{
						break;
					}
					if((lineContent = reader.readLine()) == null){
						myFileExt = "" + (Integer.parseInt(myFileExt) + 1);
						reader = new BufferedReader(new FileReader(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt));
					}else{
						if(lineNumber.length() == 0){
							sb.append("," + lineContent.substring(lineContent.indexOf("]") + 1, lineContent.length()));
						}else{
							break;
						}
					}
				}
			}
			
				
		}else{
			RandomAccessFile fileHandler = null;
		    fileHandler = new RandomAccessFile(sf, "r");
	        long fileLength = fileHandler.length() - 1;
	        long filePointer = 0;
	        int readByte = 0;
			
			firstLoop:
	        for(filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            readByte = fileHandler.readByte();
	            if( readByte == 0xA || readByte == 0xD) {
	            	sb.setLength(0);
	                continue;
	            }else{
	            	if(( char )readByte == '-'){
	            		filePointer--;
	            		fileHandler.seek( filePointer );
	    	            readByte = fileHandler.readByte();
	    	            while(readByte != 0xA && readByte != 0xD && ( char )readByte != '-'){
	    	            	sb.append(( char )readByte);
	    	            	if(filePointer != 0){
	    	            		filePointer--;
	    	            	}else{
	    	            		break;
	    	            	}
		            		fileHandler.seek( filePointer );
		    	            readByte = fileHandler.readByte();
	    	            }
    	            	if(sb.reverse().toString().length() != 0){
    	            		if(Long.parseLong(sb.toString()) == line){
    	            			break firstLoop;
    	            		}
	    	            }
	    	            sb.setLength(0);
	    	            filePointer++;
	            	}
	            }
	        }
			
			sb.setLength(0);
			filePointer++;
			fileHandler.seek(filePointer);
            readByte = fileHandler.readByte();
            
            while(filePointer <= fileLength){
            	if(readByte != 0xA && readByte != 0xD){
            		sb.append(( char )readByte);
            	}else{
            		filePointer++;
            		fileHandler.seek(filePointer);
            		readByte = fileHandler.readByte();
            		if(( char )readByte != '-'){
            			break;
            		}else{
            			filePointer++;
            			fileHandler.seek(filePointer);
                        readByte = fileHandler.readByte();
                        while(( char )readByte != ']'){
                        	filePointer++;
                			fileHandler.seek(filePointer);
                            readByte = fileHandler.readByte();
                        }
                        sb.append(",");
                        filePointer++;
            			fileHandler.seek(filePointer);
                        readByte = fileHandler.readByte();
            			sb.append(( char )readByte);
            		}
            	}
            	filePointer++;
            	if(filePointer <= fileLength){
            		fileHandler.seek(filePointer);
            		readByte = fileHandler.readByte();
            	}            	
            }
            if(filePointer > fileLength){
				myFileExt = "" + (Integer.parseInt(myFileExt) + 1);
				//System.out.println(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt);
				reader = new BufferedReader(new FileReader(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt));
				while ((lineContent = reader.readLine()) != null) {
					String lineNumber = lineContent.substring(0, lineContent.indexOf("-"));
					//System.out.println(lineNumber);
					if(lineNumber.length() == 0){
						sb.append("," + lineContent.substring(lineContent.indexOf("]") + 1, lineContent.length()));
					}else{
						break;
					}
					if((lineContent = reader.readLine()) == null){
						myFileExt = "" + (Integer.parseInt(myFileExt) + 1);
						reader = new BufferedReader(new FileReader(_dir + File.separator + IOUtils.filenameWithoutExt(sf.getName()) + "." + myFileExt));
					}else{
						if(lineNumber.length() == 0){
							sb.append("," + lineContent.substring(lineContent.indexOf("]") + 1, lineContent.length()));
						}else{
							break;
						}
					}
				}
			}
	        fileHandler.close(); 
		}
		
		if(reader != null)
			reader.close();
		
		return sb.toString();
	}
}
