package myoldtests;

@SuppressWarnings("unused")
public class Part3 {
	public static final String myFolder = System.getProperty("user.dir");

	public static void main(String[] args) throws Exception {
		JedisClient.setAndGet();
//		JedisClient.connSelectPipeline();
//		JedisClient.connSelect();
//		JedisClient.connSelect();
//		JedisClient.pipelineTransaction();
//		JedisClient.compareInRamRedisLoading(myFolder);
		
		/*SPARQL*/
//		String sparql = 
//				"SELECT ?X, ?Y, ?Z, ?Z"
//				+ "WHERE"
//				+ "{?X rdf:type ub:GraduateStudent ."
//				+ "  ?Y rdf:type ub:University ."
//				+ "  ?Y rdf:type ub:University ."
//				+ "  ?Z rdf:type ub:Department ."
//				+ "  ?X ub:memberOf ?Z ."
//				+ "  ?Z ub:subOrganizationOf ?Y ."
//				+ "  ?X ub:undergraduateDegreeFrom ?Y}";
//		
//		ParsedQuery parsed = new ParsedQuery();
//		
//		String select = sparql.substring(sparql.indexOf("SELECT") + 6, sparql.indexOf("WHERE"));
//		select = select.replace(" ", "");
//		StringTokenizer itrSelect = new StringTokenizer(select, ",");
//		while(itrSelect.hasMoreTokens()){
////			System.out.println(itrSelect.nextToken());
//			parsed.addVariable(itrSelect.nextToken());
//		}
//		
//		HashSet<String> forWhere = new HashSet<String>();
//		String where = sparql.substring(sparql.indexOf("{") + 1, sparql.indexOf("}"));
//		StringTokenizer itrWhere = new StringTokenizer(where, ".");
//		while(itrWhere.hasMoreTokens()){
//			forWhere.add(itrWhere.nextToken());
//		}
//		for(String str : forWhere){
//			parsed.putPattern(new StringPattern(str));
//		}
//		
//		System.out.println(parsed.toString());
//		
//		System.out.println(VarType.SP.toString()+" "+VarType.SP.toString().contains("P"));
	}

}
