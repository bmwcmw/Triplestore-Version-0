package queryRewriter;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import queryObjects.IntegerPattern;
import queryObjects.StringPattern;
import queryUtils.QueryUtils.VarType;
import indexNodesDBUtils.DBUtils;

public class SimpleQueryTranslator {
	
	public static IntegerPattern toCompressed(DBUtils dbu, StringPattern input) throws SQLException{
		return new IntegerPattern(
				dbu.fetchIdByNode(input.getS()),
				dbu.fetchIdByNode(input.getP()),
				dbu.fetchIdByNode(input.getO()) );
	}

}
