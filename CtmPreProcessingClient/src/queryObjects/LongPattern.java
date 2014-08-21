package queryObjects;

public class LongPattern {
	private Long s;
	private Long p;
	private Long o;
	
	/**
	 * Creates a pattern with numerical expressions(compressed) and variable informations
	 * @param S numerical expression
	 * @param P numerical expression
	 * @param O numerical expression
	 */
	public LongPattern(Long S, Long P, Long O){
		this.s = S;
		this.p = P;
		this.o = O;
	}
	
	public Long getS(){
		return s;
	}

	public Long getP(){
		return p;
	}

	public Long getO(){
		return o;
	}
	
	public String toString(){
		return this.s + " " + this.p + " " + this.o;
	}
}
