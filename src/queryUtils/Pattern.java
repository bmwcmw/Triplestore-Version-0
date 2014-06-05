package queryUtils;

import java.util.StringTokenizer;

public abstract class Pattern {
	private String s;
	private String p;
	private String o;
	
//	/**
//	 * Creates a pattern with numerical expressions(compressed) and variable informations
//	 * @param S numerical expression
//	 * @param P numerical expression
//	 * @param O numerical expression
//	 */
//	public Pattern(Integer S, Integer P, Integer O, VarType mode){
//		this.s = String.valueOf(S);
//		this.p = String.valueOf(P);
//		this.o = String.valueOf(O);
//	}
	
	/**
	 * Creates a pattern with literal expressions of S, P and O
	 * @param S literal expression
	 * @param P literal expression
	 * @param O literal expression
	 */
	public Pattern(String S, String P, String O){
		this.s = S;
		this.p = P;
		this.o = O;
	}
	
	/**
	 * Creates a pattern with a string sub-query
	 * @param something string of a whole sub-query 
	 * @throws InvalidPatternException
	 */
	public Pattern(String something) throws InvalidPatternException{
		StringTokenizer tokens = new StringTokenizer(something);
		if (!tokens.hasMoreTokens()) {
			throw new InvalidPatternException("Only " + tokens.countTokens() 
					+ " token(s), must have 3.");
		} else {
			s = tokens.nextToken();
		}
		if (!tokens.hasMoreTokens()) {
			throw new InvalidPatternException("Only " + tokens.countTokens() 
					+ " token(s), must have 3.");
		} else {
			p = tokens.nextToken();
		}
		if (!tokens.hasMoreTokens()) {
			throw new InvalidPatternException("Only " + tokens.countTokens() 
					+ " token(s), must have 3.");
		} else {
			o = tokens.nextToken();
		}
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
