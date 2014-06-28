package queryRewriter;

import queryObjects.LongPattern;
import queryObjects.StringPattern;
import indexNodesDBUtils.DBImpl;

/**
 * This is the query reriter which translates queries to convenient format, in order
 * to perform them on data stored in file system
 * @author Cedar
 *
 */
public class SimpleQueryTranslator {
	
	/**
	 * Converts literal pattern in numerical format
	 * @param dbu : DB unit
	 * @param input : pattern with literal data
	 * @return pattern using numerical format
	 * @throws Exception
	 */
	public static LongPattern toCompressed(DBImpl dbu, StringPattern input) throws Exception{
		return new LongPattern(
				dbu.fetchIdByNode(input.getS()),
				dbu.fetchIdByNode(input.getP()),
				dbu.fetchIdByNode(input.getO()));
	}

}
