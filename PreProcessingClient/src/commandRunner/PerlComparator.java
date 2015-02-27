package commandRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import localIOUtils.IOUtils;


/**
 *
 * <p>Compares two SORTED, UNIQUE OR NOT string files, returns the number of common entries.</p>
 * <p>Using Perl Script.</p>
 * 
 * @author CMWT420
 *
 */
public class PerlComparator extends BasicRunner {
	
	public PerlComparator(){}
	//TODO Out of memory error : >= 12000 occurrences
	/**
	 * <p>Runs PERL script in a separate system process</>
	 * <p>Specifies an input file and an output path</>
	 * <p>Then retrieves the process output/error messages</p>
	 * 
	 * @return -1 if IOException, 0 if it works
	 */
	@Override
	public Long execute(String inputFile1, String inputFile2){
		try {
			String line;
			String[] command = {};
			if(System.getProperty("os.name").contains("win") 
					|| System.getProperty("os.name").contains("Win")){
				command = new String[]{
						System.getProperty("user.dir") + File.separator 
						+ "script" + File.separator + "PerlComparison.bat "
						, inputFile1, inputFile2};
			} else if(System.getProperty("os.name").contains("nux") 
					|| System.getProperty("os.name").contains("nix")
					|| System.getProperty("os.name").contains("aix") ){
				command = new String[]{
						System.getProperty("user.dir") + File.separator 
						+ "script" + File.separator + "PerlComparison.sh"
						, inputFile1, inputFile2};
			} else {
				IOUtils.logLog("Unknown system : "+System.getProperty("os.name"));
				return null;
			}
			IOUtils.logLog(command);
			ProcessBuilder builder = new ProcessBuilder(command);
			builder.redirectErrorStream(true);
			Process process = builder.start();
	        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        Long outPutInt = null;
	        while ((line = br.readLine()) != null) {
	        	System.out.println(line);
		        try{
		        	outPutInt = Long.valueOf(line);
		        	break;
		        } catch (NumberFormatException e) {
		        	continue;
		        }
	        }
            System.out.println(line);
        	return outPutInt;
		} catch (IOException e) {
			IOUtils.logLog(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
}
