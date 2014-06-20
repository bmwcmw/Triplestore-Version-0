package queryObjects;

import queryUtils.QueryUtils.VarType;

public class QueryVariable {
	private VarType type;
	private String var;
	
	public QueryVariable(VarType t, String v){
		type = t;
		var = v;
	}
	
	public void setType(VarType toSet){
		type = toSet;
	}
	
	public VarType getType(){
		return type;
	}
	
	public void setVariable(String toSet){
		var = toSet;
	}
	
	public String getVariable(){
		return var;
	}
	
	public String toString(){
		return type + " " + var;
	}
}
