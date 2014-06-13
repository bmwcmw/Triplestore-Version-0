package queryRewriter;

import java.sql.SQLException;

import queryObjects.IntegerPattern;
import queryObjects.StringPattern;
import indexNodesDBUtils.DBUtils;

public class QueryTranslater {
	
	public static IntegerPattern toCompressed(DBUtils dbu, StringPattern input) throws SQLException{
		return new IntegerPattern(
				dbu.fetchIdByNode(input.getS()),
				dbu.fetchIdByNode(input.getP()),
				dbu.fetchIdByNode(input.getO()) );
	}

}
