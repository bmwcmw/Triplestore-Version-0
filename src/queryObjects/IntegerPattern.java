package queryObjects;

public class IntegerPattern {
	private Integer s;
	private Integer p;
	private Integer o;
	
	/**
	 * Creates a pattern with numerical expressions(compressed) and variable informations
	 * @param S numerical expression
	 * @param P numerical expression
	 * @param O numerical expression
	 */
	public IntegerPattern(Integer S, Integer P, Integer O){
		this.s = S;
		this.p = P;
		this.o = O;
	}
	
	public Integer getS(){
		return s;
	}

	public Integer getP(){
		return p;
	}

	public Integer getO(){
		return o;
	}
	
	public String toString(){
		return this.s + " " + this.p + " " + this.o;
	}
}
