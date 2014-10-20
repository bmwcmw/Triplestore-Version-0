package ctmNode;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.TreeMap;

import matrixLoader.LocalMatrixLoader;
import metaLoader.LocalMetaLoader;
import metaLoader.LocalMetaLoader.MODE;
import metaLoader.MetaInfoTriple;

public class TesterYu {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub

		String _testDir = System.getProperty("user.dir") + File.separator + ".." + File.separator
				+ "CtmDataSet" + File.separator + "-headOf";
		File testDir = new File(_testDir);
		System.out.println(_testDir + " " + testDir.isDirectory());
		TreeMap<Long, MetaInfoTriple> sMap = LocalMetaLoader.loadMetadataFromFile(testDir, MODE.S);
		TreeMap<Long, MetaInfoTriple> oMap = LocalMetaLoader.loadMetadataFromFile(testDir, MODE.O);
		
		boolean checkTest = LocalMatrixLoader.checkAPoint(sMap, "-headOf", MODE.S, 10000, 1);
		System.out.println(checkTest);
		
		String returnLineTest = LocalMatrixLoader.returnLine(sMap, "-headOf", MODE.S, 19999);
		System.out.println(returnLineTest);
	}

}
