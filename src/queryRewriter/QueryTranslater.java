package queryRewriter;

import queryUtils.Pattern;
import indexNodesDBUtils.DBUtils;

public class QueryTranslater {
	
	public static Pattern toCompressed(DBUtils dbu, Pattern input){
		return new Pattern(
				dbu.fetchIdByNode(input.getS()),
				dbu.fetchIdByNode(input.getP()),
				dbu.fetchIdByNode(input.getO()) );
	}

}
