package query.objects;

import query.utils.QueryUtils.VarType;

/**
 * The variable string and its type (S, P or O)
 * @author Cedar
 */
public class QueryVariable {
	private VarType type;
	private String var;

	public QueryVariable(VarType t, String v) {
		type = t;
		var = v;
	}

	public void setType(VarType toSet) {
		type = toSet;
	}

	public VarType getType() {
		return type;
	}

	public void setVariable(String toSet) {
		var = toSet;
	}

	public String getVariable() {
		return var;
	}

	public String toString() {
		return type + " " + var;
	}

	//hashCode() and equals() should work together!!
	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + (type == null ? 0 : type.hashCode());
		hash = hash * 31 + (var == null ? 0 : var.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryVariable other = (QueryVariable) obj;
		if ( (other.type == type) && (other.var.equals(var)) )
			return true;
		else return false;
	}
}
