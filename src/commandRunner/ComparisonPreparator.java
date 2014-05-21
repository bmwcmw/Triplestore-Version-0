package commandRunner;

import java.io.IOException;
import java.io.InputStream;

import localIOUtils.IOUtils;


/**
 * <p>Format converter using RDF2RDF</p>
 * 
 * @author Cedar
 *
 */
public class ComparisonPreparator {
	private final static String PERL_FILENAME = "ComparisonPrepare.pl";
	
	private Process _proc;
	private InputStream _in;
	private InputStream _err;
	
	public ComparisonPreparator(){}
	
	/**
	 * <p>Runs PERL script in a separate system process</>
	 * <p>Specifies an input file and an output path</>
	 * <p>Then retrieves the process output/error messages</p>
	 * 
	 * @return -1 if IOException, 0 if it works
	 */
	public int execute(String inputFile, String outputPath){
		try {
			_proc = Runtime.getRuntime().exec(
					new String[]{"perl",PERL_FILENAME,inputFile,outputPath});
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
