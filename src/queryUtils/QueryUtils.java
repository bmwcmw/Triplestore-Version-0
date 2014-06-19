package queryUtils;

public class QueryUtils {
	
	public static enum VarType {
		NON, S, P, O, SP, SO, PO, SPO
	}

	public static boolean isVariable(String elem){
		return elem.startsWith("?");
	}

}
