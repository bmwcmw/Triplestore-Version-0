package queryObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * A list containing pairs of objects(key, value)
 * @author CEDAR
 */
public class BiList<K, V> {
	
	/**
	 * Internal class for pairs
	 * @author Cedar
	 */
	public class BiListPair{
		private K O1;
		private V O2;
		
		public BiListPair(K o1, V o2){
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
	
	private List<K> list1;
	private List<V> list2;
	
	public BiList(){
		list1 = new ArrayList<K>();
		list2 = new ArrayList<V>();
	}
	
	/**
	 * Adds two Objects = a pair
	 * @param s1
	 * @param s2
	 */
	public void add(K s1, V s2){
		list1.add(s1);
		list2.add(s2);
	}
	
	/**
	 * Gets the i-th pair
	 * @param i
	 * @return BiListPair
	 */
	public BiListPair get(int i){
		BiListPair resp = new BiListPair(list1.get(i), list2.get(i));
		return resp;
	}
	
	public int size(){
		return list1.size();
	}
}
