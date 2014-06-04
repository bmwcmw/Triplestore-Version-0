package commandRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import localIOUtils.IOUtils;


/**
 * <p>Format converter using RDF2RDF</p>
 * 
 * @author Cedar
 *
 */
public class FormatConverter extends BasicRunner{
	private final static String RDF2RDF_JAR_FILENAME 
		= "exec" + File.separator + "rdf2rdf-1.0.1-2.3.1.jar";
	
	private Process _proc;
	private InputStream _in;
	private InputStream _err;
	
	public FormatConverter(){}
	
	/**
	 * <p>Runs rdf2rdf in a separate system process</>
	 * <p>Specifies an input file and an output file</>
	 * <p>Then retrieves the process output/error messages</p>
	 * 
	 * @return -1 if IOException, 0 if it works
	 */
	public Integer execute(String inputFile, String outputFile){
		try {
			//_proc = Runtime.getRuntime().exec("java -jar rdf2rdf-1.0.1-2.3.1.jar");
			_proc = Runtime.getRuntime().exec(
					new String[]{"java","-jar",RDF2RDF_JAR_FILENAME,inputFile,outputFile});
			_proc.waitFor();
			
			_in = _proc.getInputStream();
			byte byteIn[] = new byte[_in.available()];
			_in.read(byteIn,0,byteIn.length);
	        System.out.println(new String(byteIn));
	        
			_err = _proc.getErrorStream();
			byte byteErr[] = new byte[_err.available()];
			_err.read(byteErr,0,byteErr.length);
	        System.out.println(new String(byteErr));
	        
		} catch (IOException e) {
			IOUtils.logLog(e.getMessage());
			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {
			IOUtils.logLog(e.getMessage());
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
}
