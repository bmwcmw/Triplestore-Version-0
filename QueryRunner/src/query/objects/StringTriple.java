package query.objects;

import java.util.StringTokenizer;

import query.utils.InvalidPatternException;
import query.utils.QueryUtils;
import query.utils.QueryUtils.VarType;

public class StringTriple {
	private String s;
	private String p;
	private String o;
	private VarType type;
	
	
	/**
	 * Creates a pattern with literal expressions of S, P and O
	 * @param S literal expression
	 * @param P literal expression
	 * @param O literal expression
	 */
	public StringTriple(String S, String P, String O, VarType type){
		this.s = S;
		this.p = P;
		this.o = O;
		this.type = type;
	}
	
	/**
	 * Creates a pattern with a string sub-query
	 * @param something string of a whole sub-query 
	 * @throws InvalidPatternException
	 */
	public StringTriple(String something) throws InvalidPatternException{
		StringTokenizer tokens = new StringTokenizer(something);
		int varType = 0;
		if (!tokens.hasMoreTokens()) {
			throw new InvalidPatternException("Only " + tokens.countTokens() 
					+ " token(s), must have 3.");
		} else {
			s = tokens.nextToken();
			if (QueryUtils.isVariable(s))
				varType = varType + 1;
		}
		if (!tokens.hasMoreTokens()) {
			throw new InvalidPatternException("Only " + tokens.countTokens() 
					+ " token(s), must have 3.");
		} else {
			p = tokens.nextToken();
			if (QueryUtils.isVariable(p))
				varType = varType + 2;
		}
		if (!tokens.hasMoreTokens()) {
			throw new InvalidPatternException("Only " + tokens.countTokens() 
					+ " token(s), must have 3.");
		} else {
			o = tokens.nextToken();
			if (QueryUtils.isVariable(o))
				varType = varType + 4;
		}
		switch(varType){
			case 0:
				type = VarType.NON;
				break;
			case 1:
				type = VarType.S;
				break;
			case 2:
				type = VarType.P;
				break;
			case 3:
				type = VarType.SP;
				break;
			case 4:
				type = VarType.O;
				break;
			case 5:
				type = VarType.SO;
				break;
			case 6:
				type = VarType.PO;
				break;
			case 7:
				type = VarType.SPO;
				break;
		}
	}
	
	public VarType getType(){
		return type;
	}

	public String getS(){
		return s;
	}

	public String getP(){
		return p;
	}

	public String getO(){
		return o;
	}
	
	public String toString(){
		return this.s + " " + this.p + " " + this.o;
	}
}
