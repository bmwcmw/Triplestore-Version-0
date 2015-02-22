package queryObjects;

/**
 * Class for pairs
 * @author Cedar
 */
public class BiPair<K,V>{
	
	private K O1;
	private V O2;
	
	public BiPair(K o1, V o2){
		O1 = o1;
		O2 = o2;
	}
	
	public K elem1(){
		return O1;
	}
	
	public V elem2(){
		return O2;
	}
	
}