package dataCompressorUtils2;

import java.io.File;

public class InRamDBUtils2Test {
	public final static String _workingDir = System.getProperty("user.dir");
	
	public static void main(String[] args) throws Exception {
		String inputFilePath = _workingDir + File.separator + ".." + File.separator + "CtmDataSet" 
				+ File.separator + "_ps(26M)" + File.separator + "a";
		
		String outputPath = _workingDir + File.separator + "_compressed2";
		InRamDBUtils2 dbu = new InRamDBUtils2(inputFilePath, outputPath);
		dbu.compress();
	}

}
