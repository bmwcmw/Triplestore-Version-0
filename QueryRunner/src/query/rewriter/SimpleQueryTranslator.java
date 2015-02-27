package query.rewriter;

import db.utils.DBImpl;
import query.objects.LongTriple;
import query.objects.StringTriple;

/**
 * This is the query rewriter which translates queries to convenient format, in order
 * to perform them on data stored in file system
 * @author CEDAR
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
	public static LongTriple toCompressed(DBImpl dbu, StringTriple input) throws Exception{
		return new LongTriple(
				dbu.fetchIdByNode(input.getS()),
				dbu.fetchIdByNode(input.getP()),
				dbu.fetchIdByNode(input.getO()));
	}

}
