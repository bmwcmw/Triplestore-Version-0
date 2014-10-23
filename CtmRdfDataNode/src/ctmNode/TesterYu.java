package ctmNode;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import queryObjects.BiList;
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
		//System.out.println(_testDir + " " + testDir.isDirectory());
		BiList<Long, MetaInfoTriple> sList = LocalMetaLoader.loadMetadataFromFile(testDir, MODE.S);
		//BiList<Long, MetaInfoTriple> oList = LocalMetaLoader.loadMetadataFromFile(testDir, MODE.O);
		
		boolean checkTest = LocalMatrixLoader.checkAPoint(sList, "-headOf", MODE.S, 19994, 19996);
		System.out.println("The value of this point is " + checkTest);
		
		String returnLineTest = LocalMatrixLoader.returnLine(sList, "-headOf", MODE.S, 19999);
		System.out.println(returnLineTest);
		
	}

}
