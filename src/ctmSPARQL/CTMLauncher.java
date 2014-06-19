package ctmSPARQL;

public class CTMLauncher {

	public static void main(String[] args) {
		String query = 
				"SELECT ?X, ?Y, ?Z"
				+ "WHERE"
				+ "{?X rdf:type ub:GraduateStudent ."
				+ "  ?Y rdf:type ub:University ."
				+ "  ?Z rdf:type ub:Department ."
				+ "  ?X ub:memberOf ?Z ."
				+ "  ?Z ub:subOrganizationOf ?Y ."
				+ "  ?X ub:undergraduateDegreeFrom ?Y}";
	}

}