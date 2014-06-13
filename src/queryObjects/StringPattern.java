package queryObjects;

import java.util.StringTokenizer;

import queryUtils.InvalidPatternException;

public abstract class StringPattern {
	private String s;
	private String p;
	private String o;
	
	
	/**
	 * Creates a pattern with literal expressions of S, P and O
	 * @param S literal expression
	 * @param P literal expression
	 * @param O literal expression
	 */
	public StringPattern(String S, String P, String O){
		this.s = S;
		this.p = P;
		this.o = O;
	}
	
	/**
	 * Creates a pattern with a string sub-query
	 * @param something string of a whole sub-query 
	 * @throws InvalidPatternException
	 */
	public StringPattern(String something) throws InvalidPatternException{
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
