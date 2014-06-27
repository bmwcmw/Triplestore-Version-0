package queryRewriter;

import queryObjects.LongPattern;
import queryObjects.StringPattern;
import indexNodesDBUtils.DBImpl;

public class SimpleQueryTranslator {
	
	public static LongPattern toCompressed(DBImpl dbu, StringPattern input) throws Exception{
		return new LongPattern(
				dbu.fetchIdByNode(input.getS()),
				dbu.fetchIdByNode(input.getP()),
				dbu.fetchIdByNode(input.getO()));
	}

}
