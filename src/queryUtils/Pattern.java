package queryUtils;

import java.util.StringTokenizer;

public abstract class Pattern {
	private String s;
	private String p;
	private String o;
	
	public Pattern(String S, String P, String O){
		this.s = S;
		this.p = P;
		this.o = O;
	}
	
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
