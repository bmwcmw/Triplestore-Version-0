package queryProcessor;

public abstract class Pattern {
	private String s;
	private String p;
	private String o;
	private String str;
	
	public Pattern(String S, String P, String O){
		this.s = S;
		this.p = P;
		this.o = O;
	}
	
	public Pattern(String something){
		this.str = something;
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
	
}
