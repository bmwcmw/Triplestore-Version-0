package ctmNode;

import java.io.File;
import java.util.HashMap;

import metaLoader.LocalMetaLoader;
import metaLoader.LocalMetaLoader.MODE;
import metaLoader.MetaInfoTriple;

public class TesterYu {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String _testDir = System.getProperty("user.dir") + File.separator + ".." + File.separator
				+ "CtmDataSet" + File.separator + "-headOf";
		File testDir = new File(_testDir);
		System.out.println(_testDir + " " + testDir.isDirectory());
		HashMap<String, MetaInfoTriple> sMap = LocalMetaLoader.loadMetadataFromFile(testDir, MODE.S);
		HashMap<String, MetaInfoTriple> oMap = LocalMetaLoader.loadMetadataFromFile(testDir, MODE.O);
	}

}
