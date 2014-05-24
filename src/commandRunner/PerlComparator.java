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
public class PerlComparator extends BasicRunner {
	
	private Process _proc;
	private InputStream _in;
	private InputStream _err;
	
	public PerlComparator(){}
	
	/**
	 * <p>Runs PERL script in a separate system process</>
	 * <p>Specifies an input file and an output path</>
	 * <p>Then retrieves the process output/error messages</p>
	 * 
	 * @return -1 if IOException, 0 if it works
	 */
	@Override
	public Integer execute(String inputFile1, String inputFile2){
		try {
			_proc = Runtime.getRuntime().exec(
					new String[]{"perl -ne 'print if ($seen{$_} .= @ARGV) =~/10$/' "+inputFile1+" "+inputFile2+" | wc -l"});
			_proc.waitFor();
			
			_in = _proc.getInputStream();
			byte byteIn[] = new byte[_in.available()];
			_in.read(byteIn,0,byteIn.length);
			String outPutStr = new String(byteIn);
	        IOUtils.logLog(outPutStr);
	        
			_err = _proc.getErrorStream();
			byte byteErr[] = new byte[_err.available()];
			_err.read(byteErr,0,byteErr.length);
			IOUtils.logLog(new String(byteErr));
	        
	        Integer outPutInt = Integer.valueOf(outPutStr);
	        return outPutInt;
	        
		} catch (IOException e) {
			IOUtils.logLog(e.getMessage());
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			IOUtils.logLog(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
}
