package queryObjects;

import java.util.ArrayList;

/**
 * Result set of an SPARQL query containing all selected variables and result 
 * lists in order.
 * 
 * @author CMWT420
 *
 */
public class QueryResult {
	
	private ArrayList<String> selectedVariables;
	
	private ArrayList<ArrayList<String>> resultSet;
	
}
